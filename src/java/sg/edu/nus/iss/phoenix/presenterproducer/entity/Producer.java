/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.entity;

import java.io.Serializable;

/**
 *
 * @author THIRILWIN
 */
public class Producer implements Cloneable, Serializable  {
     private String id;
    private String name;
    
    public Producer(){}
    
    public Producer( String id,String name){
        this.id=id;
        this.name=name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the Name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
