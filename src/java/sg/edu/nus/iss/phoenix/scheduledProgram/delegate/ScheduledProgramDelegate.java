/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.delegate;


import java.sql.SQLException;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;

import sg.edu.nus.iss.phoenix.scheduledProgram.service.ScheduledProgramService;

/**
 * * @author Mugunthan
 */
public class ScheduledProgramDelegate {

    private ScheduledProgramService service;

    /**
     * 
     */
    public ScheduledProgramDelegate() {
        service = new ScheduledProgramService();
    }
    
    /**
     * 
     * @param year
     * @param week
     * @return
     * @throws AnnualSchedueNotExistException 
     */
      
    public  void PorcessCreateAnnualSchedule(AnnualSchedule as){       
        
            service.processCreateAnnualSchedule(as);
    }
    
    public void ProcessCreate(ProgramSlot prd)
    {
        service.PorcessCreate(prd);
    }

    public void processDelete(ProgramSlot programSlot) throws NotFoundException, SQLException {
        service.processDelete(programSlot);
    }
    
}
