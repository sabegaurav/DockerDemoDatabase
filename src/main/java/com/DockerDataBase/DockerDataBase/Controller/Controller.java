package com.DockerDataBase.DockerDataBase.Controller;


import com.DockerDataBase.DockerDataBase.Model.Student;
import com.DockerDataBase.DockerDataBase.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {


    @Autowired
    private Service service;

    @GetMapping("/getStd")
    public List<Student> getStudent() {

        System.out.println("Inside get");
        return service.geStudents();
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestBody Student student) {
        service.addStudent(student);

        return "Saved";
    }
}
