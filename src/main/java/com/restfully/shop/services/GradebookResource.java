/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restfully.shop.services;

import java.io.PrintStream;
import com.restfully.shop.domain.GradeBook;
import com.restfully.shop.domain.Student;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import redis.clients.jedis.Jedis;


/**
 *
 * @author hoff
 */
@Path("/gradebook")
public class GradebookResource {
    private Jedis jedis;
    private List<String> acceptableGrades = new ArrayList<String>();

    public GradebookResource(Jedis inJedis)
    {
        jedis = inJedis;
        
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

    /*
    Display all gradebooks (primary or secondary) on the current server (title and id only)
    */    
    @GET
    @Produces("application/xml")
    public StreamingOutput getAllGradebooks()
    {

        final Map<String,String> gradebookDB = jedis.hgetAll("gradebook");

        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                if (!gradebookDB.isEmpty())
                {
                PrintStream writer = new PrintStream(outputStream);
                writer.println("<gradebook-list>");

                for (final String string : gradebookDB.values())
                {
                    GradeBook gradebook = GradeBook.deserialize(string);
                    outputGradebook(outputStream, gradebook);
                }
                writer.println("</gradebook-list>");
                }
                else
                {
                   PrintStream writer = new PrintStream(outputStream);
                   writer.println("<gradebook-list></gradebook-list>");
                }
            }
        };
    }
    
    /*
    Create a primary gradebook
    */
    @PUT
    @POST
    @Path("/{name}")
    @Produces("application/xml")
    public Response modifyAndCreateBook(@PathParam("name") String inName)
    {
        if (inName == null)
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        inName = inName.toLowerCase();
        if (jedis.hexists("gradebook", inName))
        {
            // FIXME: return the URI of the location
            return Response.ok().build();
        }
        
        String key = jedis.get("gradebook_id");
        if (key == null)
        {
            key = "1";
            jedis.set("gradebook_id", key);
        }
        int intKey = Integer.parseInt(key);
        jedis.incr("gradebook_id");

        final GradeBook gradebook = new GradeBook(intKey, inName);
        jedis.hset("gradebook", key, gradebook.serialize());
        URI responseURI = URI.create("/gradebook/" + key);
        return Response.created(responseURI).build();
    }

    
   @DELETE
   @Path("/{id}")
   @Produces("application/xml")
    public Response deleteGradebook(@PathParam("id") int inId)
    {
        String values = jedis.hget("gradebook", Integer.toString(inId));
        if (values == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        jedis.hdel("gradebook", Integer.toString(inId));
        if (jedis.sismember("secondary", Integer.toString(inId)))
        {
            jedis.srem("secondary", Integer.toString(inId));
        }
        return Response.ok().build();
    }
    
    @GET
    @Path("/{id}/student")
    @Produces("application/xml")
    public StreamingOutput getStudents(@PathParam("id") int inId)
    {
        final Map<String,String> studentDatabase = jedis.hgetAll(Integer.toString(inId));
        
        if (studentDatabase == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return new StreamingOutput() {
           public void write(OutputStream outputStream) throws IOException, WebApplicationException {
               if (!studentDatabase.isEmpty())
               {
                   PrintStream writer = new PrintStream(outputStream);
                   writer.println("<student-list>");
                   for (final String value : studentDatabase.values())
                   {
                        final Student student = Student.deserialize(value);

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
    
    @GET
    @Path("/{id}/student/{name}")
    @Produces("applicaiton/xml")
    public StreamingOutput getStudent(@PathParam("id") int inId, @PathParam("name") String inName)
    {
        final Map<String,String> studentDatabase = jedis.hgetAll(Integer.toString(inId));
        
        if (studentDatabase == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        inName = inName.toLowerCase();
        String value = studentDatabase.get(inName);
        
        if (value == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        final Student student = Student.deserialize(value);

        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                outputStudent(outputStream, student);
            }
        };
    }
    
    @DELETE
    @Path("/{id}/student/{name}")
    @Produces("applicaiton/xml")
    public Response deleteStudent(@PathParam("id") int inId, @PathParam("name") String inName)
    {
        inName = inName.toLowerCase();
        Map<String,String> studentDatabase = jedis.hgetAll(Integer.toString(inId));
        if (studentDatabase == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        final String value = studentDatabase.get(inName);
        if (value == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        jedis.hdel(Integer.toString(inId), inName);
        // studentDatabase.remove(inName);
        // jedis.hset(Integer.toString(inId), studentDatabase);
        Map<String,String> check = jedis.hgetAll(Integer.toString(inId));

        return Response.ok().build();
    }
    
    @PUT
    @POST
    @Path("/{id}/student/{name}/grade/{grade}")
    public Response modifyStudentGrade(@PathParam("id") int inId,
            @PathParam("name") String inName,
            @PathParam("grade") String inGrade)
    {
        
        if (inName == null || inGrade == null || inName.isEmpty() || inGrade.isEmpty())
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        final Map<String,String> studentDatabase = jedis.hgetAll(Integer.toString(inId));
        if (studentDatabase == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        inGrade = inGrade.toLowerCase();
        String setName = inName;
        inName = inName.toLowerCase();
        
        if (!acceptableGrades.contains(inGrade))
        {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        final String value = studentDatabase.get(inName);
        Student student;
        if (value == null)
        {
            student = new Student();
            student.setName(inName);
        }
        else
        {
            student = Student.deserialize(value);
        }
            
        student.setGrade(inGrade);
        studentDatabase.put(inName, student.serialize());
        jedis.hset(Integer.toString(inId), studentDatabase);

        URI responseURI = URI.create("/student/" + student.getName() + "/grade/" + student.getGrade() );
        return Response.created(responseURI).build();
    }
    
    protected void outputStudent(OutputStream os, Student student) throws IOException {
            PrintStream writer = new PrintStream(os);
            writer.println("<student>");
            writer.println("    <name>" + student.getName() + "</name>");
            writer.println("    <grade>" + student.getGrade() + "</grade>");
            writer.println("</student>");
   }
    
    protected void outputGradebook(OutputStream os, GradeBook gradebook) throws IOException {
        PrintStream writer = new PrintStream(os);
        writer.println("<gradebook>");
        writer.println("    <id>" + gradebook.getId() + "</id>");
        writer.println("    <title>" + gradebook.getTitle() + "</title>");
        writer.println("</gradebook>");
    }
}
