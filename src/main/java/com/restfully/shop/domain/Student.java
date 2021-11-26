/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restfully.shop.domain;

/**
 *
 * @author hoff
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student {
    private String name;
    private String grade;
    
    public void setName(String inName)
    {
        this.name = inName;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setGrade(String inGrade)
    {
        this.grade = inGrade;
    }
    
    public String getGrade()
    {
        return this.grade;
    }
    
    public String serialize()
    {
        ObjectOutputStream so = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            return bo.toString();
        } catch (IOException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                so.close();
            } catch (IOException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    static public Student deserialize(String in)
    {
        try {
        byte b[] = in.getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = new ObjectInputStream(bi);
        return (Student) si.readObject();
        } catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
    
    
}
