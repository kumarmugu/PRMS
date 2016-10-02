/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.entity;

import java.util.Objects;

/**
 *
 * @author Mugunthan
 */
public class AnnualSchedule {
    private int year;
    private String assignedBy;

    /**
     * 
     * @param year
     * @param assignedBy 
     */
    public AnnualSchedule(int year, String assignedBy) {
        this.year = year;
        this.assignedBy = assignedBy;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final AnnualSchedule other = (AnnualSchedule) obj;
        if (this.year != other.year) {
            return false;
        }
        if (!Objects.equals(this.assignedBy, other.assignedBy)) {
            return false;
        }
        return true;
    }
    
    
    
}
