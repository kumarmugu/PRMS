/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.util.Calendar;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mugunthan
 */



//Note:: This class does not include all the parameters
public class ProgramSlot {

     private static final Map<Integer, String> myDayMap = new HashMap<Integer, String>(){
        {
            put(0, "Unknown");
            put(1, "Sunday");
            put(2, "Monday");
            put(3, "Tuesday");
            put(4, "Wednesday");
            put(5, "Thursday");
            put(6, "Friday");
            put(6, "Saturday");            
        }
    };
     
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
    
    
    public int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.YEAR);
    }
    public int getWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public Time getduration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }
    
    public String getDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return myDayMap.get(cal.get(Calendar.DAY_OF_WEEK));
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
    
    public long getID() {
        return startTime.getTime();
    }
}
