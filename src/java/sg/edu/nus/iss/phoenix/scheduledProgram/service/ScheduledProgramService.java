/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.radioprogram.dao.ProgramDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.DateUtil;

/**
 *
 * @author Mugunthan
 */
public class ScheduledProgramService {

    DAOFactoryImpl factory;
    ScheduleDAO spDao;
    ProgramDAO rpDao;
    PresenterDAO preDao;
    ProducerDAO proDao;
    UserDao     userDAO;
    /**
     * 
     */
    public ScheduledProgramService() {
        super();
        factory = new DAOFactoryImpl();
        spDao = factory.getScheduleDAO();
        rpDao = factory.getProgramDAO();
        preDao = factory.getPresenterDAO();
        proDao = factory.getProducerDAO();     
        userDAO = factory.getUserDAO();
    }

    /**
     * 
     * 
     */
    public void processDelete(ProgramSlot programSlot) throws NotFoundException, SQLException {
        if(IsProgramSlotDeletable(programSlot))
        {
            spDao.delete(programSlot);
        }
    }
    
    // public abstract Boolean PorcessCreateAnnualSchedule(AnnualSchedule as) throws SQLException;

    public  void processCreateAnnualSchedule(AnnualSchedule as){
        
        try { 
                ArrayList<WeeklySchedule> wsList= new ArrayList<WeeklySchedule>();
                WeeklySchedule ws;
                  
                  for(int i=1;i<54;i++)
                  {
                     ws = new WeeklySchedule();
                      if(i==1){
                          ws.setStartDate(DateUtil.getFirstDayOfYear(as.getYear()));}
                      else{
                            ws.setStartDate(DateUtil.getStartDateOfWeek( String.valueOf(as.getYear()), String.valueOf(i)));}                        
                            ws.setWeekNo(i);
                            ws.setYear(as.getYear());                      
                            wsList.add(ws);
                  }
                  
                   spDao.processCreateAnnualSchedule(as,wsList);
                
                  
              }
            
         catch (SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
     public  void PorcessCreate(ProgramSlot srp)
     {
            spDao.create(srp);
           
     }

    private boolean IsProgramSlotDeletable(ProgramSlot programSlot) {
        return true;
    }
    
    
    public void processModify(ProgramSlot spOld, ProgramSlot spNew) {		
       try {
                spDao.delete(spOld);
                spDao.create(spNew);
        } catch (NotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }      
        
        //spDao.create(spNew);
    }
    
    public ProgramSlot getProgramSlot(long id) {
        try {
            return spDao.getProgramSlot(new Date(id));
        } catch (NotFoundException | SQLException ex) {
            return null;
        }
    }
    
    public ProgramSlot constructProgramSlot(HttpServletRequest req) {
        
        String programName = req.getParameter("program");
        String presenter = req.getParameter("presenter");
        String producer = req.getParameter("producer");
        
        Date startDate = getDate(req.getParameter("date"));
        Date startTime = getTime(req.getParameter("startTime"));
        Date endTime = getTime(req.getParameter("endTime"));
        
        Date startDateTime = ProgramSlot.AddDateTime(startDate, startTime);
        Date endDateTime = ProgramSlot.AddDateTime(startDate, endTime);
        //Date duration = new Date(endDateTime.getTime() - startDateTime.getTime());
                
        String updateBy = "pointyhead";//req.getParameter("updateBy");
        Date updateOn = new Date();
        
        ProgramSlot ps = new ProgramSlot(startDateTime, endDateTime, programName);        
        //ps.setDuration(duration);
        ps.setProducerId(producer);
        ps.setPresenterId(presenter);
        ps.setupdatedBy(updateBy);
        ps.setupdatedOn(new Date(updateOn.getTime()));        
        
        try {
            WeeklySchedule ws = spDao.getScheduleForWeek(ps.getYear(), ps.getWeek());
            ps.setweekStartDate(ws.getStartDate());
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return ps;        
    }
    
    public boolean validateProgramSlotDetail(ProgramSlot ps) {
        boolean isPSvalid = ps.valdiate();
        if (isPSvalid == false) return false;
        try {
            rpDao.getObject(ps.getProgramName());
            //preDao.findPresenter(ps.getPresenterId()).get(0);
            //proDao.findProducer(ps.getProducerId()).get(0);
            //userDAO.getObject(ps.getupdatedBy());
            //boolean isUpdatingTimeValid = ps.getupdatedOn().getTime() < ps.getStartTime().getTime();
            //return isUpdatingTimeValid;
            return true;
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private Date getDate(String dateString) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return date;
    }
    
    private Date getTime(String timeString) {
        DateFormat df = new SimpleDateFormat("HH:mm"); 
        Date time = null;
        try {
            time = df.parse(timeString);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return time;
    }
}
