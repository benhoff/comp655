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
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student implements Serializable
{
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
        return this.grade + " " + this.name;
    }
    
    static public Student deserialize(String in)
    {
        String[] split = in.split(" ", 2);
        Student stud = new Student();
        stud.setName(split[1]);
        stud.setGrade(split[0]);
        return stud;
    }
    
    
}
