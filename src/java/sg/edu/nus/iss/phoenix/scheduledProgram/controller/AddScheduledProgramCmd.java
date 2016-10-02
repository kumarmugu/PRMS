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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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
import sg.edu.nus.iss.phoenix.util.ValidationResult;

/**
 *
 * @author Mugunthan
 */
@Action("addsp")
public class AddScheduledProgramCmd implements Perform {

    ScheduledProgramDelegate spDelegate = new ScheduledProgramDelegate();
    
    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
       
       String year = req.getParameter("year");
       String week = req.getParameter("week");
       String program= req.getParameter("program");
       String programid=req.getParameter("scheduledProgramId");
       String producer=req.getParameter("producer");
       String producerid=req.getParameter("producerId");
       String presenter=req.getParameter("presenter");
       String presetnerid= req.getParameter("presenterId");
       User u = (User) req.getSession().getAttribute("user");
       
                
        ScheduledProgramDelegate srddel= new ScheduledProgramDelegate();
        ProgramSlot  srd = null;
        String mode = "create", msg;
        try {
            try {
                srd = validateFormat(req).result;                
                srddel.ProcessCreate(srd);
                mode = "";
                msg = "Successfully created.";
            } catch (Exception ex) {
                    
                msg = "Error: " + ex.getMessage();
                Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (Exception ex) {
            msg = "Fail to construct Program Slot.";            
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
             
        WeeklySchedule ws= null;
        if (srd == null) {
            srd = new ProgramSlot();
        }
        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("isAnnualScheduleExist", true);
            
            
        } catch (AnnualSchedueNotExistException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("isAnnualScheduleExist", false);
          
        } catch (SQLException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (srd == null) {
            srd = new ProgramSlot();
        }
        
        if (ws == null) {
            ws = new WeeklySchedule();
        }
        
        
        req.setAttribute("program",program);
        req.setAttribute("scheduledProgramId", programid);
        req.setAttribute("producer", "ZZZZZZZZZZZZZZZZZZZZZZZ");
        req.setAttribute("producerId", producerid);
        req.setAttribute("presenter", presenter);
        req.setAttribute("presenterId",presetnerid);
       
        
        
        req.setAttribute("events", ws.getProgramSlots());
        req.setAttribute("default", srd);
        req.setAttribute("startDate", ws.getStartDate());
        req.setAttribute("weekNo", ws.getWeekNo());
        req.setAttribute("currentYear", ws.getYear());        
        req.setAttribute("mode", mode);
        req.setAttribute("msg", msg);    
        return "/pages/crudsp.jsp";
    }
    
    private ValidationResult<ProgramSlot> validateFormat(HttpServletRequest req) throws Exception {        
        Map<String, String[]> params = req.getParameterMap();
        User u = (User) req.getSession().getAttribute("user");
        return new ValidationResult(spDelegate.getProgramSlot(params, u));
    }
}
