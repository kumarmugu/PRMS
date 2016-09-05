/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.util.Date;

/**
 *
 * @author Mugunthan
 */
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
    
    
    
    
}
