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
public class Presenter implements Cloneable, Serializable {
    
    private String id;
    private String name;
    private String Photo;
    private String Email;
    private String Description;

      
    public Presenter () {

    }

    public Presenter (String id,String Name,String desc) {

          this.id = id;
          this.name=Name;
          this.Description=desc;
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
     * @return the Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.name= Name;
    }

    /**
     * @return the Photo
     */
    public String getPhoto() {
        return Photo;
    }

    /**
     * @param Photo the Photo to set
     */
    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    /**
     * @return the Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param Email the Email to set
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }
}
