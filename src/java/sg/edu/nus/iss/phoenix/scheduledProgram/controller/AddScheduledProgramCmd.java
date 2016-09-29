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
@Action("addsp")
public class AddScheduledProgramCmd implements Perform {

    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
       
       String year = req.getParameter("year");
       String week = req.getParameter("week");
       /*       
       String useraction = req.getParameter("txtcreateAnnualSchedule");
       
        User user= (User) req.getSession().getAttribute("user");
        
        ProgramSlot  srd = new ProgramSlot();
      
         String expectedPattern = "dd/MM/yyyy HH:mm:ss";
         SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);       
         String programStartDateime= req.getParameter("date") + " "+ req.getParameter("startTime")+":00";
         
         System.out.println("programStartDateime"+programStartDateime);
         
        Date srdStartDate = null;
        try {
                srdStartDate = formatter.parse(programStartDateime);            
                System.out.println("srdStartDate: " + srdStartDate);

        } catch (ParseException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //getTimeDiff(String starttime,String endtime) 
        
        String starttime = req.getParameter("startTime");
        String endtime=req.getParameter("endTime");
         
        srd.setProgramName(req.getParameter("program"));   
        // srd.setProgramName("MM News");
         System.out.println("==============================================");
         System.out.println("req.getParameter(\"program\"):  "+req.getParameter("program"));
        srd.setPresenterId(req.getParameter("presenterId")); 
        //srd.setPresenterId("mozert"); 
        System.out.println("srd.getPresenterId():  "+srd.getPresenterId());
        srd.setProducerId(req.getParameter("producerId"));
         System.out.println("srd.getProducerId():  "+srd.getProducerId());
        srd.setupdatedBy(user.getId());
        srd.setupdatedOn(new Date());        
        srd.setStartTime(srdStartDate);
        
        srd.setweekStartDate(DateUtil.getStartDateOfWeek(year, week));
        try {
            srd.setDuration(DateUtil.getTimeDiff(starttime, endtime));
        } catch (ParseException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }*/
                
        ScheduledProgramDelegate srddel= new ScheduledProgramDelegate();
        ProgramSlot  srd = null;
        String mode = "create", msg;
        try {
            srd = srddel.getProgramSlot(req);
            srddel.ProcessCreate(srd);
            mode = "";
            msg = "Successfully created.";            
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
        req.setAttribute("events", ws.getProgramSlots());
        req.setAttribute("default", srd);
        req.setAttribute("startDate", ws.getStartDate());
        req.setAttribute("weekNo", ws.getWeekNo());
        req.setAttribute("currentYear", ws.getYear());
        req.setAttribute("mode", mode);
        req.setAttribute("msg", msg);    
        return "/pages/crudsp.jsp";
    }
}
