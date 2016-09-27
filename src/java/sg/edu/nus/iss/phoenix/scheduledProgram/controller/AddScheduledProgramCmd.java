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
       
              
       String useraction = req.getParameter("txtcreateAnnualSchedule");
       String year = req.getParameter("year");
       String week = req.getParameter("week");
       
        User user= (User) req.getSession().getAttribute("user");
        
        ProgramSlot  srd = new ProgramSlot();
      
         String expectedPattern = "dd/MM/yyyy HH:mm:ss";
         SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);       
         String programStartDateime= req.getParameter("dateCreate") + " "+ req.getParameter("startTimeCreate")+":00";
         
         System.out.println("programStartDateime"+programStartDateime);
         
        Date srdStartDate = null;
        try {
                srdStartDate = formatter.parse(programStartDateime);            
                System.out.println("srdStartDate: " + srdStartDate);

        } catch (ParseException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //getTimeDiff(String starttime,String endtime) 
        
        String starttime = req.getParameter("startTimeCreate");
        String endtime=req.getParameter("endTimeCreate");
         
        srd.setProgramName(req.getParameter("programCreate"));   
        // srd.setProgramName("MM News");
         System.out.println("==============================================");
         System.out.println("req.getParameter(\"programCreate\"):  "+req.getParameter("programCreate"));
        srd.setPresenterId(req.getParameter("presenterCreateid")); 
        //srd.setPresenterId("mozert"); 
        System.out.println("srd.getPresenterId():  "+srd.getPresenterId());
        srd.setProducerId(req.getParameter("producerCreateid"));
         System.out.println("srd.getProducerId():  "+srd.getProducerId());
        srd.setupdatedBy(user.getId());
        srd.setupdatedOn(new Date());        
        srd.setStartTime(srdStartDate);
        
        srd.setweeekStartDate(DateUtil.getStartDateOfWeek(year, week));
        try {
            srd.setDuration(DateUtil.getTimeDiff(starttime, endtime));
        } catch (ParseException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        ScheduledProgramDelegate srddel= new ScheduledProgramDelegate();
        srddel.ProcessCreate(srd); 
       
        
        
         
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
        ScheduledProgramDelegate spdel = new ScheduledProgramDelegate(); 
             
        WeeklySchedule ws = new WeeklySchedule();
        AnnualSchedule as ;
        
        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("events", ws.getProgramSlots());
            req.setAttribute("startDate", ws.getStartDate());
            req.setAttribute("isAnnualScheduleExist", true);
            req.setAttribute("weekNo", ws.getWeekNo());
            req.setAttribute("currentYear", ws.getYear());
            
        } catch (AnnualSchedueNotExistException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("isAnnualScheduleExist", false);
          
        } catch (SQLException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
        return "/pages/crudsp.jsp";
    }
}
