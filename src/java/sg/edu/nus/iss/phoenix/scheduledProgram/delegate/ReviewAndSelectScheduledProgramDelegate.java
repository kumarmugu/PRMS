/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.delegate;

import java.util.ArrayList;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.service.ReviewSelectScheduledProgramService;

/**
 * * @author Mugunthan
 */
public class ReviewAndSelectScheduledProgramDelegate {

    private ReviewSelectScheduledProgramService service;

    public ReviewAndSelectScheduledProgramDelegate() {
        service = new ReviewSelectScheduledProgramService();
    }

    public ArrayList<ProgramSlot> reviewSelectScheduledProgram(String year, String week) throws AnnualSchedueNotExistException {
        return service.reviewSelectScheduledProgram(year, week);
    }
}
