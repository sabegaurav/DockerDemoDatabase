pipeline {
    agent any

    environment {
        MAVEN_HOME = 'C:\\apache-maven-3.9.16'
        PATH = "${MAVEN_HOME}\\bin;${env.PATH}"

        DOCKER_IMAGE_NAME = 'gaurav122002/dockerdemo'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Build with Maven') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE_NAME%:%DOCKER_IMAGE_TAG% .'
                bat 'docker tag %DOCKER_IMAGE_NAME%:%DOCKER_IMAGE_TAG% %DOCKER_IMAGE_NAME%:latest'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                        usernamePassword(
                                credentialsId: 'docker-hub-credentials',
                                usernameVariable: 'DOCKER_USER',
                                passwordVariable: 'DOCKER_PASS'
                        )
                ]) {
                    bat '''
                        echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                        docker push %DOCKER_IMAGE_NAME%:%DOCKER_IMAGE_TAG%
                        docker push %DOCKER_IMAGE_NAME%:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo '=== CD: Stopping old containers ==='
                bat 'docker-compose down || exit /b 0'

                echo '=== CD: Starting new containers ==='
                bat 'docker-compose up -d'

                echo '=== Waiting for app ==='
                bat 'timeout /t 5'

                echo '=== App deployed successfully ==='
            }
        }

        stage('Cleanup') {
            steps {
                bat '''
                    docker rmi %DOCKER_IMAGE_NAME%:%DOCKER_IMAGE_TAG% || exit /b 0
                    docker rmi %DOCKER_IMAGE_NAME%:latest || exit /b 0
                '''
            }
        }
    }

    post {
        success {
            echo '✅ CI/CD Pipeline succeeded! App deployed to Docker Compose.'
        }

        failure {
            echo '❌ Pipeline failed!'
            bat 'docker-compose logs || exit /b 0'
        }
    }
}