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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoff
 */
public class GradeBook {
    private String title;
    private Integer id;
    
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
        ObjectOutputStream so = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            return bo.toString();
        } catch (IOException ex) {
            Logger.getLogger(GradeBook.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                so.close();
            } catch (IOException ex) {
                Logger.getLogger(GradeBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
        
    static public GradeBook deserialize(String in)
    {
        try {
        byte b[] = in.getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = new ObjectInputStream(bi);
        return (GradeBook) si.readObject();
        } catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
    
    
}
