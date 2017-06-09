/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucbcba.simpleScheduling.resource;


import bo.edu.ucbcba.simpleScheduling.model.MyClass;
import bo.edu.ucbcba.simpleScheduling.model.Student;
import bo.edu.ucbcba.simpleScheduling.response.ErrorResponse;
import com.google.gson.Gson;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * REST Web Service
 *
 * @author da_20
 */
@Path("v1/students")
public class StudentsResource {

    
    //patch creation
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("PATCH")
    public @interface PATCH{
    
    }
    
    
    @Context
    private UriInfo context;
    private final Gson gson = new Gson();
    
    
    public StudentsResource() {
    }
    
    //get student
    @GET
    @Path("{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("studentId") Integer studentId){
        //search student
        Student student=GenericResource.getStudent(studentId);
        if(student!=null){
            return Response.ok(gson.toJson(student),MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();   
    }
    
    //get all students
    @GET
    @Produces(MediaType.APPLICATION_JSON)
        public Response getStudents(){
        List<Student> listStudents=GenericResource.getAllStudents(); 
        if(!listStudents.isEmpty()){
            return Response.ok(gson.toJson(listStudents),MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();  
    }
    
    //create student
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStudent(String jsonString){
        Student student = gson.fromJson(jsonString,Student.class);
        int studentId = student.getStudentId();
        if(GenericResource.getStudent(studentId)!=null){
            ErrorResponse errorReponse=new ErrorResponse(UUID.randomUUID(),Response.Status.BAD_REQUEST,"ERR 001",
                "Creation failed",
                "Student was not created",
                Arrays.asList("El usuario ya existe"));
            return Response.ok(gson.toJson(errorReponse),MediaType.APPLICATION_JSON).status(Response.Status.BAD_REQUEST).build();
        }else{
            List<String> classCodesf=new ArrayList();
          
            List<String> aux2=student.getClassCodes();
            for(String c:aux2){
                if(GenericResource.getClass(c)==null){
                    classCodesf.add(c);
                }
            }
            if(classCodesf.isEmpty()){
                List<String> aux=student.getClassCodes();
                for(String c:aux){
                    MyClass mc=GenericResource.getClass(c);
                    List<Integer> sl=mc.getStudentIds();
                    sl.add(student.getStudentId());
                    mc.setStudentIds(sl);
                }
                GenericResource.putStudent(student);            
                return Response.ok(gson.toJson(student),MediaType.APPLICATION_JSON).status(Response.Status.CREATED).build();
            }else{
                List<String> aux=student.getClassCodes();
                for(String c:classCodesf){
                    aux.remove(c);
                }
                for(String c:aux){
                    MyClass mc=GenericResource.getClass(c);
                    List<Integer> sl=mc.getStudentIds();
                    sl.add(student.getStudentId());
                    mc.setStudentIds(sl);
                }
                student.setClassCodes(aux);
                GenericResource.putStudent(student); 
                List<String> errorList=new ArrayList();
                for(String c:classCodesf){
                    errorList.add("Class "+c+" not exist");
                }
                ErrorResponse errorReponse=new ErrorResponse(UUID.randomUUID(),Response.Status.BAD_REQUEST,"ERR 02",
                "Creation failed",
                "Student was not created",
                errorList);
                return Response.ok(gson.toJson(errorReponse),MediaType.APPLICATION_JSON).status(Response.Status.BAD_REQUEST).build();
            }
        }
        
    }
    
    
    //delete student
    @DELETE
    @Path("{studentId}")
    public Response deleteStudent(@PathParam("studentId") Integer studentId){
        Student student=GenericResource.getStudent(studentId);
        if(student!=null){
            List<String> sl=GenericResource.getStudent(studentId).getClassCodes();
            if(!sl.isEmpty()){
                for(String c:sl){
                    MyClass s=GenericResource.getClass(c);
                    List<Integer> l=s.getStudentIds();
                    l.remove(studentId);
                    s.setStudentIds(l);
                }
            }
            GenericResource.deleteStudent(studentId);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    
    
    //update all student
    @PUT
    public Response updateStudent(String jsonString){
        //search student
        Student student = gson.fromJson(jsonString,Student.class);
        int studentId = student.getStudentId();
        String lastName = student.getLastName();
        String firstName = student.getFirstName();
        List<String>classCodes=student.getClassCodes();
        Student student2=GenericResource.getStudent(studentId);
         if(student2!=null){
            student2.setLastName(lastName);
            student2.setFirstName(firstName);
            Integer auxId=student2.getStudentId();
            student2.setStudentId(studentId);
            List<String> classCodesf=new ArrayList();
            List<String> aux2=classCodes;
            for(String c:aux2){
                if(GenericResource.getClass(c)==null){
                    classCodesf.add(c);
                }
            }
            //Aqui agrega y quita clases
            if(classCodesf.isEmpty()){
                List<String> aux=student.getClassCodes();
                List<String> auxl2=student2.getClassCodes();
                
                 for(String c:auxl2){
                    MyClass mc=GenericResource.getClass(c);
                    List<Integer> sl=mc.getStudentIds();
                    sl.remove(auxId);
                    mc.setStudentIds(sl);
                }
                
                for(String c:aux){
                    MyClass mc=GenericResource.getClass(c);
                    List<Integer> sl=mc.getStudentIds();
                    sl.add(student.getStudentId());
                    mc.setStudentIds(sl);
                }
                
                student2.setClassCodes(classCodes);
                return Response.status(Response.Status.OK).build();
            }else{
                List<String> aux=student.getClassCodes();
                
                
                for(String c:classCodesf){
                    aux.remove(c);
                }
                
                
                List<String> auxl2=student2.getClassCodes();
                
                 for(String c:auxl2){
                    MyClass mc=GenericResource.getClass(c);
                    List<Integer> sl=mc.getStudentIds();
                    sl.remove(auxId);
                    mc.setStudentIds(sl);
                }
                
                for(String c:aux){
                    MyClass mc=GenericResource.getClass(c);
                    List<Integer> sl=mc.getStudentIds();
                    sl.add(student.getStudentId());
                    mc.setStudentIds(sl);
                }
                student2.setClassCodes(aux);
                List<String> errorList=new ArrayList();
                for(String c:classCodesf){
                    errorList.add("Class "+c+" not exist");
                }
                ErrorResponse errorReponse=new ErrorResponse(UUID.randomUUID(),Response.Status.BAD_REQUEST,"ERR 02",
                "Creation failed",
                "Student was not created",
                errorList);
                return Response.ok(gson.toJson(errorReponse),MediaType.APPLICATION_JSON).status(Response.Status.BAD_REQUEST).build();
            }
            
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    
    //update student 
    @PATCH
    public Response updateStudent1(String jsonString){
        Student student = gson.fromJson(jsonString,Student.class);
        int studentId = student.getStudentId();
        String lastName = student.getLastName();
        String firstName = student.getFirstName();
        List<String>classCodes=student.getClassCodes();
        Student student2=GenericResource.getStudent(studentId);
        if(student2!=null){
            if(!"".equals(lastName)){
                student2.setLastName(lastName);
            }
            if(!"".equals(firstName)){
                student2.setFirstName(firstName);
            }
            if(!classCodes.isEmpty()){
                List<String> classCodesf=new ArrayList();
                List<String> aux2=classCodes;
                for(String c:aux2){
                    if(GenericResource.getClass(c)==null){
                        classCodesf.add(c);
                    }
                }
                if(classCodesf.isEmpty()){  
                    List<String> aux=student.getClassCodes();
                    for(String c:aux){
                        MyClass mc=GenericResource.getClass(c);
                        List<Integer> sl=mc.getStudentIds();
                        sl.add(student.getStudentId());
                        mc.setStudentIds(sl);
                    }
                    student2.setClassCodes(classCodes);
                }else{
                    List<String> aux=student.getClassCodes();
                    for(String c:classCodesf){
                        aux.remove(c);
                    }
                    for(String c:aux){
                        MyClass mc=GenericResource.getClass(c);
                        List<Integer> sl=mc.getStudentIds();
                        sl.add(student.getStudentId());
                        mc.setStudentIds(sl);
                    }
                    student2.setClassCodes(aux);
                    List<String> errorList=new ArrayList();
                    for(String c:classCodesf){
                        errorList.add("Class "+c+" not exist");
                    }
                    ErrorResponse errorReponse=new ErrorResponse(UUID.randomUUID(),Response.Status.BAD_REQUEST,"ERR 02",
                    "Creation failed",
                    "Student was not created",
                    errorList);
                    return Response.ok(gson.toJson(errorReponse),MediaType.APPLICATION_JSON).status(Response.Status.BAD_REQUEST).build();
                }
            }
            return Response.status(Response.Status.OK).build(); 
        }
        return Response.status(Response.Status.NOT_FOUND).build(); 
    }
}
