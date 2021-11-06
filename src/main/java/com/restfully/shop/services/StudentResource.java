/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restfully.shop.services;

import com.restfully.shop.domain.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Node;
/**
 *
 * @author hoff
 */

@Path("/student")
public class StudentResource {
    private Map<String, Student> customerDB = new ConcurrentHashMap<String, Student>();
    private List<String> acceptableGrades = new ArrayList<String>();
    
    public StudentResource()
    {
        acceptableGrades.add("a");
        acceptableGrades.add("a+");
        acceptableGrades.add("a-");
        acceptableGrades.add("b");
        acceptableGrades.add("b+");
        acceptableGrades.add("b-");
        acceptableGrades.add("c");
        acceptableGrades.add("c+");
        acceptableGrades.add("c-");
        acceptableGrades.add("d");
        acceptableGrades.add("d+");
        acceptableGrades.add("d-");
        acceptableGrades.add("e");
        acceptableGrades.add("f");
        acceptableGrades.add("i");
        acceptableGrades.add("w");
        acceptableGrades.add("z");
    }
    
   @GET
   @Produces("application/xml")
   public StreamingOutput getAllStudents( ) {
       return new StreamingOutput() {
           public void write(OutputStream outputStream) throws IOException, WebApplicationException {
               if (!customerDB.isEmpty())
               {
                   PrintStream writer = new PrintStream(outputStream);
                   writer.println("<student-list>");
                   for (final Student student : customerDB.values())
                   {
                       outputStudent(outputStream, student);
                   }
                   writer.println("</student-list>");
               }
               else
               {
                   PrintStream writer = new PrintStream(outputStream);
                   writer.println("<student-list></student-list>");
               }
           } 
       };
  }
    @PUT
    @POST
    @Path("/{name}/grade/{grade}")
    @Produces("application/xml")
    public Response modifyAndCreateGrade(@PathParam("name") String inName, @PathParam("grade")String inGrade)
    {
        if (inName == null || inGrade == null || inName.isEmpty() || inGrade.isEmpty())
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        inGrade = inGrade.toLowerCase();
        String setName = inName;
        inName = inName.toLowerCase();
        
        if (!acceptableGrades.contains(inGrade))
        {
            System.out.println("bad grade " + inGrade);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Student student = new Student();
        student.setName(setName);
        student.setGrade(inGrade);
        customerDB.put(inName, student);
        final Student finalStudent = student;

        /*
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                outputStudent(outputStream, finalStudent);
            }
        };
        */
        URI responseURI = URI.create("/student/" + finalStudent.getName() + "/grade/" + finalStudent.getGrade() );
        return Response.created(responseURI).build();
    }
    
    @GET
    @Path("/{name}")
    @Produces("application/xml")
    public StreamingOutput getGrade(@PathParam("name") String inName)
    {
        inName = inName.toLowerCase();
        final Student student = customerDB.get(inName);
        
        if (student == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
                
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                outputStudent(outputStream, student);
            }
        };
    }
    
    @DELETE
    @Path("/{name}")
   @Produces("application/xml")
    public Response deleteCustomer(@PathParam("name") String inName)
    {
        inName = inName.toLowerCase();
        final Student student = customerDB.get(inName);
        if (student == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        customerDB.remove(inName);
        return Response.ok().build();
    }
    
      protected void outputStudent(OutputStream os, Student student) throws IOException {
            PrintStream writer = new PrintStream(os);
            writer.println("<student>");
            writer.println("    <name>" + student.getName() + "</name>");
            writer.println("    <grade>" + student.getGrade() + "</grade>");
            writer.println("</student>");
   }
}
