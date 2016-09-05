/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ReviewAndSelectScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.util.DateUtil;

/**
 *
 * @author Mugunthan
 */
@Action("managesp")
public class ManageScheduledProgramCmd implements Perform {

    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
        ArrayList<ProgramSlot> data = new ArrayList<>();
        String year = req.getParameter("year");
        String week = req.getParameter("week");
        try {
            data = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("events", data);
            req.setAttribute("startDate", DateUtil.getStartDateOfWeek(year, week));
            req.setAttribute("isAnnualScheduleExist", true);
        } catch (AnnualSchedueNotExistException e) {
            req.setAttribute("isAnnualScheduleExist", false);
        }

        return "/pages/crudsp.jsp";
    }
}
