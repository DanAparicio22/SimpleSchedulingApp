/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucbcba.simpleScheduling.resource;

import bo.edu.ucbcba.simpleScheduling.model.MyClass;
import bo.edu.ucbcba.simpleScheduling.model.Student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author da_20
 */
public class GenericResource {
    private static final Map<Integer,Student> studentMap=new HashMap<>();
    private static final Map<String,MyClass>myClassMap=new HashMap<>();
    
    public static  void putStudent(Student student){
        if(student!=null){
            studentMap.put(student.getStudentId(), student);
        }
    }
    
    public static Student getStudent(Integer studentId){
        return studentMap.get(studentId);
    }
    
    public static void deleteStudent(Integer studentId){
        studentMap.remove(studentId);
    }
    
     public static List<Student> getAllStudents(){
         List<Student> listStudents=new ArrayList(studentMap.values());
        return listStudents;
    }
     
   public static void putClass(MyClass myClass){
       if(myClass!=null){
            myClassMap.put(myClass.getCode(), myClass);
        }
   }
   
   public static MyClass getClass(String code){
        return myClassMap.get(code);
    }
    
    public static void deleteClass(String code){
        myClassMap.remove(code);
    }
    
    public static List<MyClass> getAllClasses(){
         List<MyClass> listClasses=new ArrayList(myClassMap.values());
        return listClasses;
    }
}
