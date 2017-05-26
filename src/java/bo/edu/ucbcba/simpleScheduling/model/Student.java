/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucbcba.simpleScheduling.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author da_20
 */
public class Student {
    private int studentId;
    private String lastName;
    private String firstName;
    private List<String> classCodes;

    public Student() {
        lastName=firstName="";
        classCodes = new ArrayList<>();
    }

    public Student(int studentId, String lastName, String firstName) {
        this.studentId = studentId;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<String> getClassCodes() {
        return classCodes;
    }

    public void setClassCodes(List<String> classCodes) {
        this.classCodes = classCodes;
    }

    
}
