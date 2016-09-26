/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
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
    ScheduleDAO spdao;

    /**
     * 
     */
    public ScheduledProgramService() {
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
                  
                   spdao.processCreateAnnualSchedule(as,wsList);
                
                  
              }
            
         catch (SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
     public  void PorcessCreate(ProgramSlot srp)
     {
            spdao.create(srp);
           
     }
    
    
   
    
    
}
