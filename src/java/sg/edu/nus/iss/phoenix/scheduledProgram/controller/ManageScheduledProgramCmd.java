/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ReviewAndSelectScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
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
        ScheduledProgramDelegate pdel = new ScheduledProgramDelegate();

        String useraction = req.getParameter("txtcreateAnnualSchedule");
        String year = req.getParameter("year");
        String week = req.getParameter("week");

        User user = (User) req.getSession().getAttribute("user");

        System.out.println("Session Userid : " + user.getId());
        System.out.println("++++++++ useraction : " + useraction);

        WeeklySchedule ws = new WeeklySchedule();
        AnnualSchedule as;

        if (useraction != null) {
            if (useraction.equals("1")) {
                as = new AnnualSchedule(Integer.parseInt(year), user.getId());
                System.out.println("+++++++User Slection to create annual schedule" + useraction);
                pdel.PorcessCreateAnnualSchedule(as);

            }

        }

        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("isAnnualScheduleExist", true);
        } catch (AnnualSchedueNotExistException ex) {
            Logger.getLogger(ManageScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("isAnnualScheduleExist", false);
        } catch (SQLException ex) {
            Logger.getLogger(ManageScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManageScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);

        }
        req.setAttribute("events", ws.getProgramSlots());
        Date startDate = DateUtil.getStartDateOfWeek(year, week);
        req.setAttribute("default", new ProgramSlot(startDate, startDate, ""));
        req.setAttribute("startDate", ws.getStartDate());
        req.setAttribute("weekNo", ws.getWeekNo());
        req.setAttribute("currentYear", ws.getYear());
        return "/pages/crudsp.jsp";
    }
}
