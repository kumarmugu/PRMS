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
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;

/**
 *
 * @author THIRILWIN
 */
@Action("deletesp")
public class DeleteScheduledProgramCmd implements Perform{

    @Override
    public String perform(String string, HttpServletRequest hsr, HttpServletResponse hsr1) throws IOException, ServletException {
        ProgramSlot programSlot=new ProgramSlot();
        programSlot.setProgramName("charity");
        String expectedPattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);       
        String programStartDateime= "08/09/2016 03:00:00";
        Date srdStartDate = null;
        try {
            srdStartDate = formatter.parse(programStartDateime);
            System.out.println("srdStartDate: " + srdStartDate);
        } catch (ParseException ex) {
            Logger.getLogger(AddScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        programSlot.setStartTime(srdStartDate);
        ScheduledProgramDelegate scheduleProgramDel= new ScheduledProgramDelegate();
        try {
            scheduleProgramDel.processDelete(programSlot);
        } catch (NotFoundException ex) {
            Logger.getLogger(DeleteScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DeleteScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean validateFormat()
    {
        return true;
    }
    
}
