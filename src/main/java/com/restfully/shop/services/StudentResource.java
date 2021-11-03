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
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Node;
/**
 *
 * @author hoff
 */

@Path("/student")
public class StudentResource {
    private Map<String, Student> customerDB = new ConcurrentHashMap<String, Student>();
    // private AtomicInteger idCounter = new AtomicInteger();
    
   @GET
   @Produces("application/xml")
   public StreamingOutput getAllStudents( ) {

       return new StreamingOutput() {
           public void write(OutputStream outputStream) throws IOException, WebApplicationException {
               int totalValue = customerDB.size();

               for (int i=0; i < totalValue; i++)
               {
                   final Student student = customerDB.get(i + 1);
                   outputCustomer(outputStream, student);
               }
           } 
       };

  }
   
   
    @XmlRootElement(name="student-list")
    public class StudentList {
	@XmlElement(name="student")
	public List<Student> studentList = new ArrayList<Student>();
    }
    
    @GET
    @Consumes("application/xml")
    @Path("/student/{name}/{grade}")
    public Response getGrade(@PathParam("name") String inName, @PathParam("grade")String inGrade)
    {
        Student student = customerDB.get(inName);
        
        if (student == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
                
        Response result;
        return result;
    }
    
   @POST
   @Consumes("application/xml")
   public Response createCustomer(InputStream is) {
      Student student = readCustomer(is);
      customer.setId(idCounter.incrementAndGet());
      customerDB.put(customer.getId(), customer);
      System.out.println("Created customer " + customer.getId());
      URI responseURI = URI.create("/customers/" + Integer.toString(customer.getId()));
      Response result = Response.created(responseURI).build();
      return result;
   }  
      protected void outputCustomer(OutputStream os, Customer cust) throws IOException {
      PrintStream writer = new PrintStream(os);
      writer.println("<customer id=\"" + Integer.toString(cust.getId()) + "\">");
      writer.println("   <first-name>" + cust.getFirstName() + "</first-name>");
      writer.println("   <last-name>" + cust.getLastName() + "</last-name>");
      writer.println("   <street>" + cust.getStreet() + "</street>");
      writer.println("   <city>" + cust.getCity() + "</city>");
      writer.println("   <state>" + cust.getState() + "</state>");
      writer.println("   <zip>" + cust.getZip() + "</zip>");
      writer.println("   <country>" + cust.getCountry() + "</country>");
      writer.println("</customer>");
   }

   protected Student readStudent(InputStream is) {
      try {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = builder.parse(is);
         Element root = doc.getDocumentElement();
         Student stud = new Student();
         if (root.getAttribute("id") != null && !root.getAttribute("id").trim().equals(""))
            stud.setId(Integer.valueOf(root.getAttribute("id")));
         NodeList nodes = root.getChildNodes();
         for (int i = 0; i < nodes.getLength(); i++) {
            try {
            // Element element = (Element) nodes.item(i);
            Node node = nodes.item(i);
            String tagName = node.getNodeName();
            String value = node.getTextContent();
            if (tagName.equals("name")) {
               stud.setName(value);
            }
            else if (tagName.equals("grade")) {
               stud.setGrade(value);
            }
            }
            catch (Exception e) {
            }
         }
         return cust;
      }
      catch (Exception e) {
         throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
      }
   }
}
