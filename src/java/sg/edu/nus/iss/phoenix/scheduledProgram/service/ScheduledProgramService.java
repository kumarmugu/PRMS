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
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;
import sg.edu.nus.iss.phoenix.radioprogram.dao.ProgramDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.DateUtil;
import sg.edu.nus.iss.phoenix.util.ValidationResult;

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
        String presenter = req.getParameter("presenterId");
        String producer = req.getParameter("producerId");
        
        Date startDate = getDate(req.getParameter("date"));
        Date startTime = getTime(req.getParameter("startTime"));
        Date endTime = getTime(req.getParameter("endTime"));
        
        Date startDateTime = ProgramSlot.AddDateTime(startDate, startTime);
        Date endDateTime = ProgramSlot.AddDateTime(startDate, endTime);
        //Date duration = new Date(endDateTime.getTime() - startDateTime.getTime());
        
        User user = (User) req.getSession().getAttribute("user");
        String updateBy = user.getId();//req.getParameter("updateBy");
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
            
            ps.setPresenterName(getUser(ps.getPresenterId()).getName());            
            ps.setProducerName(getUser(ps.getProducerId()).getName());
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return ps;        
    }
    
    public ValidationResult validateProgramSlotDetail(ProgramSlot ps) {
        ValidationResult valdiation = ps.valdiate();
        if (valdiation.result == false)
        {
            return valdiation;
        }
        String reason = "";
        try {
            reason = "Radio Program Not Found.";
            rpDao.getObject(ps.getProgramName());
            
            reason = "Presenter Not Found.";
            Presenter presenter = getPresenter(ps.getPresenterId());
            
            reason = "Producer Not Found.";
            Producer producer = getProducer(ps.getProducerId());
                    
            reason = "UpdatedBy User Not Found.";
            userDAO.getObject(ps.getupdatedBy());
            reason = "Can not update past scheduled program.";
            boolean isUpdatingTimeValid = ps.getupdatedOn().getTime() < ps.getStartTime().getTime();
            if (isUpdatingTimeValid == false) {
                valdiation = new ValidationResult(false);
                valdiation.reasons.add(reason);
                return valdiation;
            }
            valdiation = new ValidationResult(true);
        } catch (NotFoundException | SQLException ex) {
            valdiation = new ValidationResult(false);
            valdiation.reasons.add(reason);
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valdiation;
    }
    
    public boolean isProgramSlotOverlapping(ProgramSlot newPs, ProgramSlot oldPs) {
        if (newPs == null) return false;
        if (oldPs == null) oldPs = newPs;
        WeeklySchedule ws;
        
        try {
            ws = spDao.getScheduleForWeek(newPs.getYear(), newPs.getWeek());
            ws = spDao.loadAllScheduleForWeek(ws);
            for(ProgramSlot eps : ws.getProgramSlots()) {
                if (eps.getID() != oldPs.getID() && eps.getDay().equals(newPs.getDay())) {
                    if (newPs.getStartTime().getTime() < eps.getEndTime().getTime() &&
                        newPs.getEndTime().getTime() > eps.getStartTime().getTime() )
                        return true;
                }
            }
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; 
    }
    
    private Date getDate(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//SimpleDateFormat("dd/MM/yyyy"); 
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
    
    private User getUser(String theID) throws NotFoundException {
        try {
            return userDAO.getObject(theID);
        } catch (SQLException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    
    private Presenter getPresenter(String theID) throws NotFoundException {
        User user = null;
        try {
            user = getUser(theID);
            if (user == null) return null;
            
            return preDao.findPresenter(user.getName()).get(0);
        } catch (Exception ex) {
            throw new NotFoundException();
        } 
    }
    
    private Producer getProducer(String theID) throws NotFoundException {
        User user = null;
        try {
            user = getUser(theID);
            if (user == null) return null;
            
            return proDao.findProducer(user.getName()).get(0);
        } catch (Exception ex) {
            throw new NotFoundException();
        } 
    }
    
}
