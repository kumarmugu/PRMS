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
 * 
 * Producer Value Object. This class is value object representing database table
 * role This class is intended to be used together with associated Dao object.
 */
public class Producer implements Cloneable, Serializable  {
    /**
	 * Persistent Instance variables. 
     */
     private String id;
    private String name;
    
    /**
	 * Constructors. The first one takes no arguments and provides the most
	 * simple way to create object instance. 
	 */
    public Producer(){}
    
    public Producer( String id,String name){
        this.id=id;
        this.name=name;
    }

    /**
	 * Get- and Set-methods for persistent variables. The default behaviour does
	 * not make any checks against malformed data, so these might require some
	 * manual additions.
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
