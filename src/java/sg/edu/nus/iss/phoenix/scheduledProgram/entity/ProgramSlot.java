/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.sql.Time;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import sg.edu.nus.iss.phoenix.util.DateUtil;
import sg.edu.nus.iss.phoenix.util.ValidationResult;

/**
 *
 * @author Mugunthan
 */
//Note:: This class does not include all the parameters
public class ProgramSlot {

    public ProgramSlot() {
        this.startTime = new Date();
        this.duration = new Time(3600000);
        this.endTime = DateUtil.AddDateTime(startTime, duration);// new Date( startTime.getTime() + duration.getTime() );
    }
    
    public ProgramSlot(Date startTime, Date endTime, String programName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.programName = programName;
        this.duration = DateUtil.getDateDiff(startTime, endTime);
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
        ValidationResult<Boolean> validation = new ValidationResult(isTimeValid && isProgramNameValid);
        if (validation.result == false) {
            if (isTimeValid == false) {
                validation.reasons.add("Invalid Time.");
            }
            if (isProgramNameValid == false) {
                validation.reasons.add("Invalid Program.");
            }
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

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getPresenterId() {
        return presenterId;
    }

    public void setPresenterId(String presenterId) {
        this.presenterId = presenterId;
    }

    public Date getweeekStartDate() {
        return this.weekStartDate;
    }

    public void setweekStartDate(Date weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public int getYear() {
        return DateUtil.getYear(startTime);
    }

    public int getWeek() {
        return DateUtil.getWeekOfYear(startTime);
    }

    public Date getduration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getDay() {
        return DateUtil.getDayOfWeek(startTime);
    }

    public String getupdatedBy() {
        return updatedBy;
    }

    public void setupdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getupdatedOn() {
        return this.updatedOn;
    }

    public void setupdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public long getID() {
        return startTime.getTime();
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
    
    @Override
    public int hashCode() {
        return (int)getID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProgramSlot other = (ProgramSlot) obj;
        if (this.getID() != other.getID()) {
            return false;
        }

        return true;
    }
}
