/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Mugunthan
 */



//Note:: This class does not include all the parameters
public class ProgramSlot {

    public ProgramSlot(){
        
    };
    
    public ProgramSlot(Date startTime, Date endTime, String programName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.programName = programName;
    }
    
    private Date startTime;
    private Date endTime;
    private String programName;
    
    private String producerId;
    private String presenterId;
    private Date weekStartDate;
    private Time duration;
    private String updatedBy;
    private Date updatedOn;
    
    
    
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
       
    
    public String getProducerId(){
        return producerId;
    }
    
    public void setProducerId(String producerId){
        this.producerId=producerId;
    }
    
    public String getPresenterId(){
        return presenterId;
    }
    
    public void setPresenterId(String presenterId){
        this.presenterId=presenterId;
    }
    
    public Date getweeekStartDate(){
        return this.weekStartDate;
    }
    
    public void setweeekStartDate(Date weekStartDate )
    {
        this.weekStartDate=weekStartDate;
    }
    
    
    public Time getduration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }
    
      public String getupdatedBy(){
        return updatedBy;
    }
    
    public void setupdatedBy(String updatedBy){
        this.updatedBy=updatedBy;
    }
    
    public Date getupdatedOn(){
        return this.updatedOn;
    }
    
    public void setupdatedOn(Date updatedOn )
    {
        this.updatedOn=updatedOn;
    }
    
}
