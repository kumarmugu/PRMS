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
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
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

    /**
     * 
     */
    public ReviewAndSelectScheduledProgramService() {
        super();
        factory = new DAOFactoryImpl();
        spdao = factory.getScheduleDAO();
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
        try {
            if (year != null && year.matches("^-?\\d{4}+$") && week != null && week.matches("^-?\\d+$")) {
                ws = new WeeklySchedule(Integer.parseInt(year), Integer.parseInt(week));
            } else {
                Calendar cal = Calendar.getInstance();
                ws = new WeeklySchedule(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR) );
            }
            AnnualSchedule as = spdao.getAnnualSchedule(ws);
            
            ws = spdao.loadWeekInfo(ws);
            
            if(as != null)
                ws = spdao.loadAllScheduleForWeek(ws);
            else 
                 throw new AnnualSchedueNotExistException("Annual Schedule not exist");
        } catch (SQLException ex) {
            Logger.getLogger(ReviewAndSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return ws;
    }
}
