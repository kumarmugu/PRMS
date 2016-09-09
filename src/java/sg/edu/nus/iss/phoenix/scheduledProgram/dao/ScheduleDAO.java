/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.dao;

import java.sql.SQLException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;

/**
 *
 * @author Mugunthan
 */
public interface ScheduleDAO {

    public abstract ProgramSlot createValueObject();

    public abstract WeeklySchedule loadAllScheduleForWeek(WeeklySchedule ws) throws SQLException;
    
    public abstract AnnualSchedule getAnnualSchedule(WeeklySchedule ws) throws SQLException;
}
