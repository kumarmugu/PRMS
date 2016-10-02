/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionManagement;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.DateUtil;

/**
 *
 * @author Mugunthan
 */
public class ReviewAndSelectScheduledProgramService {

    DAOFactoryImpl factory;
    ScheduleDAO spdao;
    UserDao userDAO;

    /**
     * 
     */
    public ReviewAndSelectScheduledProgramService() {
        super();
        factory = new DAOFactoryImpl();
        spdao = factory.getScheduleDAO();
        userDAO = factory.getUserDAO();
    }

    /**
     * 
     * 
     */
    public void processDelete(String id) {
    }
    
    // public abstract Boolean PorcessCreateAnnualSchedule(AnnualSchedule as) throws SQLException;

    public  void PorcessCreateAnnualSchedule(AnnualSchedule as){
        
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
                  
                   spdao.processCreateAnnualSchedule(as,wsList);
                
                  
              }
            
         catch (SQLException ex) {
            Logger.getLogger(ReviewAndSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   
    
    /**
     * 
     * @param year
     * @param week
     * @return
     * @throws AnnualSchedueNotExistException 
     */
    
    public WeeklySchedule reviewSelectScheduledProgram(String year, String week) throws AnnualSchedueNotExistException, SQLException {
        WeeklySchedule ws = null;
        Calendar cal = Calendar.getInstance();
        int yearToSearch = cal.get(Calendar.YEAR);
        int weekToSearch  = cal.get(Calendar.WEEK_OF_YEAR);
        try {
            if (year != null && year.matches("^-?\\d{4}+$") && week != null && week.matches("^-?\\d+$")) {
                yearToSearch = Integer.parseInt(year);
                weekToSearch = Integer.parseInt(week);
            }
            ws = new WeeklySchedule(yearToSearch, weekToSearch);
            ws.setStartDate(DateUtil.getStartDateOfWeek(yearToSearch, weekToSearch));
            AnnualSchedule as = spdao.getAnnualSchedule(ws);
            
            if(as != null)
            {
                ws = spdao.loadAllScheduleForWeek(ws);
                for( ProgramSlot ps : ws.getProgramSlots()) {                    
                    try {
                            ps.setPresenterName(userDAO.getObject(ps.getPresenterId()).getName());
                            ps.setProducerName(userDAO.getObject(ps.getProducerId()).getName());
                    } catch (NotFoundException ex) {
                            Logger.getLogger(ReviewAndSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
            else 
                 throw new AnnualSchedueNotExistException("Annual Schedule not exist");
        } catch (SQLException ex) {
            Logger.getLogger(ReviewAndSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return ws;
    }
}
