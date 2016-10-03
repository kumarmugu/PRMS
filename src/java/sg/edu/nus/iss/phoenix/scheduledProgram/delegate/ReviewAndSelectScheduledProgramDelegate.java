/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.delegate;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.service.ReviewAndSelectScheduledProgramService;

/**
 * * @author Mugunthan, Zehua, Mi Zaw, Thiri
 */
public class ReviewAndSelectScheduledProgramDelegate {

    private ReviewAndSelectScheduledProgramService service;

    /**
     * 
     */
    public ReviewAndSelectScheduledProgramDelegate() {
        service = new ReviewAndSelectScheduledProgramService();
    }
    
    /**
     * 
     * @param year
     * @param week
     * @return
     * @throws AnnualSchedueNotExistException 
     */
    public WeeklySchedule reviewSelectScheduledProgram(String year, String week) throws AnnualSchedueNotExistException, SQLException {
        return service.reviewSelectScheduledProgram(year, week);
    }
    
    
}
