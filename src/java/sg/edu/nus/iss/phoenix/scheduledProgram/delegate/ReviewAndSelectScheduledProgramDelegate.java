/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.delegate;

import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.service.ReviewAndSelectScheduledProgramService;

/**
 * * @author Mugunthan
 */
public class ReviewAndSelectScheduledProgramDelegate {

    private ReviewAndSelectScheduledProgramService service;

    public ReviewAndSelectScheduledProgramDelegate() {
        service = new ReviewAndSelectScheduledProgramService();
    }

    public WeeklySchedule reviewSelectScheduledProgram(String year, String week) throws AnnualSchedueNotExistException {
        return service.reviewSelectScheduledProgram(year, week);
    }
}
