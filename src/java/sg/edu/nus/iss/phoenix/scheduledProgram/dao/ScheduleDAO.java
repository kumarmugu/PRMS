/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;

/**
 *
 * @author Mugunthan
 */
public interface ScheduleDAO {

    public abstract ProgramSlot createValueObject();

    public abstract boolean create(ProgramSlot ValueObject)
            throws SQLException;

    public abstract void delete(ProgramSlot valueObject)
            throws NotFoundException, SQLException;

    public abstract void update(ProgramSlot valueObject)
            throws NotFoundException, SQLException;

    /**
     *
     * loadAllScheduleForWeek-method. This will read all scheduled programs from
     * database table for a particular week and assign the list of programSlot
     * to Weekly Schedule. of data.
     *
     * @param ws
     * @return
     * @throws SQLException
     */
    public abstract WeeklySchedule loadAllScheduleForWeek(WeeklySchedule ws) throws SQLException;

    public abstract AnnualSchedule getAnnualSchedule(WeeklySchedule ws) throws SQLException;

    public abstract Boolean processCreateAnnualSchedule(AnnualSchedule as, ArrayList<WeeklySchedule> wsList) throws SQLException;

    public ProgramSlot getProgramSlot(Date theStartTime) throws NotFoundException, SQLException;

    public WeeklySchedule getScheduleForWeek(int year, int weekNo) throws NotFoundException, SQLException;

    public void setManualCommitRequired(boolean manual) throws SQLException;

    public boolean getManualCommitRequired() throws SQLException;
    
    public void complete() throws SQLException;

    public void create(WeeklySchedule valueObject)
            throws SQLException;
}
