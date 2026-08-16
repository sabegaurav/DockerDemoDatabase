pipeline {
    agent any

    environment {
        MAVEN_HOME = 'C:\\apache-maven-3.9.16'
        PATH = "${MAVEN_HOME}\\bin;${env.PATH}"

        DOCKER_IMAGE_NAME = 'gaurav122002/dockerdemo'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
        EMAIL_RECIPIENTS = 'springboot.kafka.test@gmail.com'
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

                echo '=== Waiting for app to start ==='
                bat 'ping localhost -n 6 > nul'

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
            echo '✅ CI/CD Pipeline succeeded! App deployed.'
            emailext(
                    to: '${gauravsabe23@gmail.com}',
                    subject: "✅ Deployment Success - Build #${BUILD_NUMBER}",
                    body: '''
                    <h2>Deployment Successful!</h2>
                    <p><b>Build Number:</b> ${BUILD_NUMBER}</p>
                    <p><b>Image:</b> gaurav122002/dockerdemo:${BUILD_NUMBER}</p>
                    <p><b>Status:</b> Running on docker-compose</p>
                    <p><b>Access App:</b> http://localhost:8080</p>
                    <hr>
                    <p><b>Build URL:</b> ${BUILD_URL}</p>
                    <p>Your Spring Boot application has been deployed successfully!</p>
                ''',
                    mimeType: 'text/html'
            )
        }

        failure {
            echo '❌ Pipeline failed!'
            bat 'docker-compose logs || exit /b 0'
            emailext(
                    to: '${gauravsabe23@gmail.com}',
                    subject: "❌ Deployment Failed - Build #${BUILD_NUMBER}",
                    body: '''
                    <h2>Deployment Failed!</h2>
                    <p><b>Build Number:</b> ${BUILD_NUMBER}</p>
                    <p><b>Status:</b> FAILED</p>
                    <hr>
                    <p><b>Build URL:</b> ${BUILD_URL}</p>
                    <p>Please check the logs for details.</p>
                    <p><b>Jenkins Console:</b> ${BUILD_URL}console</p>
                ''',
                    mimeType: 'text/html'
            )
        }

        always {
            echo 'Pipeline finished'
        }
    }
}