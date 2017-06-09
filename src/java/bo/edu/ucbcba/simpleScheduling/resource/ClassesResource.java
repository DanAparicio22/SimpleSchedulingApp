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
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author da_20
 */
@Path("v1/classes")
public class ClassesResource {

    
    //patch creation
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @HttpMethod("PATCH")
    public @interface PATCH{
    
    }
    
    
    
    
    @Context
    private UriInfo context;
    private final Gson gson = new Gson();
    
    public ClassesResource() {
    }
    
    @GET
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClass(@PathParam("code") String code){
        //search student
        MyClass myClass=GenericResource.getClass(code);
        if(myClass!=null){
            return Response.ok(gson.toJson(myClass),MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();   
    }
    
     @GET
    @Produces(MediaType.APPLICATION_JSON)
        public Response getClasses(){
        List<MyClass> listClasses=GenericResource.getAllClasses(); 
        if(!listClasses.isEmpty()){
            return Response.ok(gson.toJson(listClasses),MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();  
    }

        
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createClass(String jsonString){
        MyClass myClass = gson.fromJson(jsonString,MyClass.class);
        String code = myClass.getCode();
        if(GenericResource.getClass(code)!=null){
            ErrorResponse errorReponse=new ErrorResponse(UUID.randomUUID(),Response.Status.BAD_REQUEST,"ERR 001",
                "Creation failed",
                "Class was not created",
                Arrays.asList("La clase ya existe"));
            return Response.ok(gson.toJson(errorReponse),MediaType.APPLICATION_JSON).status(Response.Status.BAD_REQUEST).build();
            
        }else{
            List<Integer> classCodesf=new ArrayList();
             List<Integer> aux2=myClass.getStudentIds();
            for(Integer c:aux2){
                if(GenericResource.getStudent(c)==null){
                    classCodesf.add(c);
                   
                }
            }
            if(classCodesf.isEmpty()){
                GenericResource.putClass(myClass);
        return Response.ok(gson.toJson(myClass),MediaType.APPLICATION_JSON).status(Response.Status.CREATED).build();
            }else{
                List<Integer> aux=myClass.getStudentIds();
                for(Integer c:classCodesf){
                    aux.remove(c);
                }
                myClass.setStudentIds(aux);
                GenericResource.putClass(myClass); 
                List<String> errorList=new ArrayList();
                for(Integer c:classCodesf){
                    errorList.add("Student "+c+" not exist");
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
    @Path("{code}")
    public Response deleteClass(@PathParam("code") String code){
        MyClass myClass=GenericResource.getClass(code);
        if(myClass!=null){
            List<Integer> sl=GenericResource.getClass(code).getStudentIds();
            if(!sl.isEmpty()){
                for(Integer c:sl){
                    Student s=GenericResource.getStudent(c);
                    List<String> l=s.getClassCodes();
                    l.remove(code);
                    s.setClassCodes(l);
                }
                
            }
            GenericResource.deleteClass(code);
            
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    
    
    //update all student
    @PUT
    public Response updateClass(String jsonString){
        //search student
        MyClass myClass = gson.fromJson(jsonString,MyClass.class);
        String code = myClass.getCode();
        String title = myClass.getTitle();
        String description = myClass.getDescription();
        List<Integer>studentIds=myClass.getStudentIds();
        MyClass myClass2=GenericResource.getClass(code);
        if(myClass2!=null){
            myClass2.setDescription(description);
            myClass2.setTitle(title);
            String auxCode = myClass2.getCode();
            myClass2.setCode(code);
            myClass2.setStudentIds(studentIds);
          
            List<Integer> classCodesf=new ArrayList();
            List<Integer> aux2=studentIds;
            for(Integer c:aux2){
                if(GenericResource.getStudent(c)==null){
                    classCodesf.add(c);
                }
            }
            if(classCodesf.isEmpty()){  
                List<Integer> aux=myClass.getStudentIds();
                List<Integer> auxl2=myClass2.getStudentIds();
                
                for(Integer c:auxl2){
                    Student s=GenericResource.getStudent(c);
                    List<String> sl=s.getClassCodes();
                    sl.remove(auxCode);
                    s.setClassCodes(sl);
                }
                
                for(Integer c:aux){
                    Student s=GenericResource.getStudent(c);
                    List<String> sl=s.getClassCodes();
                    sl.add(myClass.getCode());
                    s.setClassCodes(sl);
                }
                
                myClass2.setStudentIds(studentIds);
                return Response.status(Response.Status.OK).build();
            }else{
                List<Integer> aux=myClass.getStudentIds();
                
              
                
                for(Integer c:classCodesf){
                    aux.remove(c);
                }
                
                List<Integer> auxl2=myClass2.getStudentIds();
                
                for(Integer c:auxl2){
                    Student s=GenericResource.getStudent(c);
                    List<String> sl=s.getClassCodes();
                    sl.remove(auxCode);
                    s.setClassCodes(sl);
                }
                
                for(Integer c:aux){
                    Student s=GenericResource.getStudent(c);
                    List<String> sl=s.getClassCodes();
                    sl.add(myClass.getCode());
                    s.setClassCodes(sl);
                }
                myClass2.setStudentIds(aux); 
                List<String> errorList=new ArrayList();
                for(Integer c:classCodesf){
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
    public Response updateClass1(String jsonString){
        MyClass myClass = gson.fromJson(jsonString,MyClass.class);
        String code = myClass.getCode();
        String title = myClass.getTitle();
        String description = myClass.getDescription();
        List<Integer>studentIds=myClass.getStudentIds();
        MyClass myClass2=GenericResource.getClass(code);
        if(myClass2!=null){
            if(!"".equals(title)){
                myClass2.setTitle(title);
            }
            if(!"".equals(description)){
                myClass2.setDescription(description);
            }
            if(!studentIds.isEmpty()){
                myClass2.setStudentIds(studentIds);
                List<Integer> classCodesf=new ArrayList();
                List<Integer> aux2=studentIds;
                for(Integer c:aux2){
                    if(GenericResource.getStudent(c)==null){
                        classCodesf.add(c);
                    }
                }
                if(classCodesf.isEmpty()){  
                    List<Integer> aux=myClass.getStudentIds();
                    for(Integer c:aux){
                        Student s=GenericResource.getStudent(c);
                        List<String> sl=s.getClassCodes();
                        sl.add(myClass.getCode());
                        s.setClassCodes(sl);
                    }
                    myClass2.setStudentIds(studentIds); 
                }else{
                    List<Integer> aux=myClass.getStudentIds();
                    for(Integer c:classCodesf){
                        aux.remove(c);
                    }
                    for(Integer c:aux){
                        Student s=GenericResource.getStudent(c);
                        List<String> sl=s.getClassCodes();
                        sl.add(myClass.getCode());
                        s.setClassCodes(sl);
                    }
                    myClass2.setStudentIds(aux); 
                    List<String> errorList=new ArrayList();
                    for(Integer c:classCodesf){
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
