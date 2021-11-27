/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restfully.shop.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoff
 */
public class GradeBook implements Serializable {
    private String title;
    private Integer id;
    
    public GradeBook()
    {
        
    }
    
    public GradeBook(int inId, String inTitle)
    {
        this.id = inId;
        this.title = inTitle;
    }
    
    public void setId(int inId)
    {
        this.id = inId;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void setTitle(String inTitle)
    {
        this.title = inTitle;
    }
    
    public String getTitle()
    {
        return this.title;
    }
    
    public String serialize()
    {
        return Integer.toString(this.id) + " " + this.title;
    }
        
    static public GradeBook deserialize(String in)
    {
        String[] split = in.split(" ", 2);
        GradeBook book = new GradeBook();
        for (int i=0; i < split.length; i++)
        {
            if (i == 0)
                book.setId(Integer.parseInt(split[i]));
            if (i == 1)
                book.setTitle(split[i]);
        }
        return book;
    }
    
    
}
