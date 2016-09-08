/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Mugunthan
 */
public class WeeklySchedule {

    Date startDate;
    int year;
    int weekNo;

    ArrayList<ProgramSlot> programSlots = new ArrayList<ProgramSlot>();

    public WeeklySchedule() {
    }

    public WeeklySchedule(int year, int weekNo) {
        this.year = year;
        this.weekNo = weekNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public ArrayList<ProgramSlot> getProgramSlots() {
        return programSlots;
    }

    public void setProgramSlots(ArrayList<ProgramSlot> programSlots) {
        this.programSlots = programSlots;
    }

}
