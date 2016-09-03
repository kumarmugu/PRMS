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
public class SiteLinks implements Cloneable, Serializable  {
    private String ID;
    private String SiteLink;
    private String URL;
    
    public SiteLinks(){}

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return the SiteLink
     */
    public String getSiteLink() {
        return SiteLink;
    }

    /**
     * @param SiteLink the SiteLink to set
     */
    public void setSiteLink(String SiteLink) {
        this.SiteLink = SiteLink;
    }

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        this.URL = URL;
    }
    
}
