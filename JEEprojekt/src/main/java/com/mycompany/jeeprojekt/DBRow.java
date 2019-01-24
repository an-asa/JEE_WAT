/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jeeprojekt;

import java.sql.Date;

/**
 *
 * @author PRO
 */
public class DBRow {
    
    private int id;
    private String user;
    private String description;
    private String filename;
    private Date date;

    public DBRow() {
    }

    public DBRow(String user,String description) {
        this.user = user;
        this.description = description;
    }

    public DBRow(int id,String user,String description,Date date,String filename) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.date = date;
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
        
}
