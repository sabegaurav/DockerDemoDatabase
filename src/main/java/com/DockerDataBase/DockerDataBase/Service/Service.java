package com.DockerDataBase.DockerDataBase.Service;

import com.DockerDataBase.DockerDataBase.Model.Student;
import com.DockerDataBase.DockerDataBase.Repo.Repo;
import jakarta.websocket.server.ServerEndpoint;
import org.hibernate.annotations.Audited;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
        @Autowired
        private Repo repo;

        public List<Student> geStudents() {

            return repo.findAll();
        }

    public void addStudent(Student student) {

    }
}
