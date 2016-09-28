/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.delegate;


import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;

import sg.edu.nus.iss.phoenix.scheduledProgram.service.ScheduledProgramService;
import sg.edu.nus.iss.phoenix.util.ValidationResult;

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
    
    public ProgramSlot processModify(long modifyingProgramSlotId, 
                                     ProgramSlot newProgramSlot) throws Exception{
        ProgramSlot existingProgramSlot = getProgramSlot(newProgramSlot.getID());
        if (existingProgramSlot != null &&
            existingProgramSlot.getID() != newProgramSlot.getID()) {
            throw new Exception("A Program already exists in the new timeslot.");
        }
        
        ProgramSlot modifyingProgramSlot = getProgramSlot(modifyingProgramSlotId);
        if (modifyingProgramSlot == null) {
            throw new NotFoundException("Modifying Program slot cannot found.");
        }
        ValidationResult validation = service.validateProgramSlotDetail(newProgramSlot);
        if (!validation.result) {
            throw new Exception("Invalid Program Slot. " + validation.reasons.toString());
        }
        
        boolean isOverlapping = service.isProgramSlotOverlapping(newProgramSlot, modifyingProgramSlot);
        if (isOverlapping) {
            throw new Exception("New time slot is overlapping with existing program slot(s). ");
        }
        
        service.processModify(modifyingProgramSlot, newProgramSlot);        
        return newProgramSlot;        
    }
    
    public ProgramSlot ProcessCopy(ProgramSlot newProgramSlot) throws Exception
    {
        ProgramSlot existingProgramSlot = getProgramSlot(newProgramSlot.getID());
        if (existingProgramSlot != null &&
            existingProgramSlot.getID() != newProgramSlot.getID()) {
            throw new Exception("A Program already exists in the new timeslot.");
        }
        
        ValidationResult validation = service.validateProgramSlotDetail(newProgramSlot);
        if (!validation.result) {
            throw new Exception("Invalid Program Slot. " + validation.reasons.toString());
        }
        service.PorcessCreate(newProgramSlot);
        return newProgramSlot;  
    }
    
    public ProgramSlot getProgramSlot(long id) {
        return service.getProgramSlot(id);
    }
    
    public ProgramSlot getProgramSlot(HttpServletRequest req) {
        return service.constructProgramSlot(req);
    }
}
