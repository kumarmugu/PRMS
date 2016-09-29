/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import sg.edu.nus.iss.phoenix.util.DateUtil;

/**
 *
 * @author Mugunthan
 */
public class WeeklySchedule {

    Date startDate;
    int year;
    int weekNo;

    ArrayList<ProgramSlot> programSlots = new ArrayList<ProgramSlot>();

    /**
     *
     */
    public WeeklySchedule() {
    }

    /**
     *
     * @param year
     * @param weekNo
     */
    public WeeklySchedule(int year, int weekNo) {
        this.year = year;
        this.weekNo = weekNo;
        this.startDate = DateUtil.getStartDateOfWeek(year, weekNo);
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.year;
        hash = 83 * hash + this.weekNo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeeklySchedule other = (WeeklySchedule) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.weekNo != other.weekNo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WeeklySchedule{" + "startDate=" + startDate + ", year=" + year + ", weekNo=" + weekNo + ", programSlots=" + programSlots + '}';
    }

}
