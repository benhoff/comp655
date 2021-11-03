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
    
    
}
