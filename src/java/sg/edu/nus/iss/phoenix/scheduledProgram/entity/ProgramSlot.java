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
import java.util.concurrent.TimeUnit;
import sg.edu.nus.iss.phoenix.util.ValidationResult;

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
        this.startTime = new Date( );
        this.duration = new Time(3600000);
        this.endTime = AddDateTime(startTime, duration);// new Date( startTime.getTime() + duration.getTime() );
    };
    
    public ProgramSlot(Date startTime, Date endTime, String programName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.programName = programName;
        long timeMs = getDateDiff(startTime, endTime, TimeUnit.SECONDS);
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0, (int)timeMs / 3600, (int)timeMs % 3600 / 60, (int)timeMs %60 );
        //cal.setTimeInMillis(timeMs);
        this.duration = cal.getTime();        
    }
    
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    private Date startTime;
    private Date endTime;
    private String programName;
    
    private String producerId, producerName;
    private String presenterId, presenterName;
    private Date weekStartDate;
    private Date duration;
    private String updatedBy;
    private Date updatedOn;
    
    public ValidationResult valdiate() {
        boolean isTimeValid = startTime.getTime() < endTime.getTime();
        
        //boolean isDurationValid = AddDateTime(startTime, duration).getTime() == endTime.getTime();
        boolean isProgramNameValid = programName != null; 
        ValidationResult<Boolean> validation = new ValidationResult(isTimeValid  && isProgramNameValid);
        if (validation.result == false){
            if (isTimeValid == false) validation.reasons.add("Invalid Time.");
            if (isProgramNameValid == false) validation.reasons.add("Invalid Program.");
        }
        return validation;
    }
    
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
    
    public void setweekStartDate(Date weekStartDate )
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
    
    public Date getduration() {
        return duration;
    }

    public void setDuration(Date duration) {
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
    
    public static Date AddDateTime(Date d1, Date durationLessThan1Day) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(durationLessThan1Day);
       //cal1.add(Calendar.YEAR, cal2.get(Calendar.YEAR) - 1970);
       // cal1.add(Calendar.MONTH, cal2.get(Calendar.MONTH));
       // cal1.add(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH) );
        cal1.add(Calendar.HOUR, cal2.get(Calendar.HOUR));
        cal1.add(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        cal1.add(Calendar.SECOND, cal2.get(Calendar.SECOND));
        cal1.add(Calendar.MILLISECOND, cal2.get(Calendar.MILLISECOND));
        return cal1.getTime();
    }

    public void setPresenterName(String presenterName) {
        this.presenterName = presenterName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getPresenterName() {
        return presenterName;
    }

    public String getProducerName() {
        return producerName;
    }
}
