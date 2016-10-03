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
 * SiteLinks Value Object. This class is value object representing database table
 * role This class is intended to be used together with associated Dao object.
 */
public class SiteLinks implements Cloneable, Serializable  {
    /**
	 * Persistent Instance variables. 
     */
    private String ID;
    private String SiteLink;
    private String URL;
    
    /**
	 * Constructors. The first one takes no arguments and provides the most
	 * simple way to create object instance. 
	 */
    public SiteLinks(){}

    /**
	 * Get- and Set-methods for persistent variables. The default behaviour does
	 * not make any checks against malformed data, so these might require some
	 * manual additions.
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
