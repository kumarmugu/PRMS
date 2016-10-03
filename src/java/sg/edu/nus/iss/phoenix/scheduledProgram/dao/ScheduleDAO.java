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
 * @author Mugunthan, Zehua, Mi Zaw, Thiri
 */
public interface ScheduleDAO {

    public abstract ProgramSlot createValueObject();

    /**
     * Create Program slot in DB
     *
     * @param ValueObject - the program slot value object
     * @return true if it successes, else return false
     * @throws SQLException upon sql error (connection or insertion error)
     */
    public abstract boolean create(ProgramSlot ValueObject)
            throws SQLException;

    /**
     * Delete Program slot in DB
     *
     * @param ValueObject - the program slot value object
     * @return true if it successes, else return false
     * @throws SQLException upon sql error (connection or insertion error)
     */
    public abstract void delete(ProgramSlot valueObject)
            throws NotFoundException, SQLException;

    /**
     * Modify Program slot in DB
     *
     * @param ValueObject - the program slot value object
     * @return true if it successes, else return false
     * @throws SQLException upon sql error (connection or insertion error)
     */
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

    /**
     * Get annual schedule from DB
     *
     * @param ws - the weekly schedule
     * @return annual schedule if found
     * @throws SQLException on sql error
     */
    public abstract AnnualSchedule getAnnualSchedule(WeeklySchedule ws) throws SQLException;

    /**
     * Create new annual schedule with specific weekly schedule list
     *
     * @param as - annual schedule to be created
     * @param wsList - weekly schedule for the whole year
     * @return true upon success else return false
     * @throws SQLException up sql error
     */
    public abstract Boolean processCreateAnnualSchedule(AnnualSchedule as, ArrayList<WeeklySchedule> wsList) throws SQLException;

    /**
     * Get program slot with specific start time from DB
     *
     * @param theStartTime - the start time of the program slot
     * @return the program slot if found
     * @throws NotFoundException when not found
     * @throws SQLException when sql error
     */
    public ProgramSlot getProgramSlot(Date theStartTime) throws NotFoundException, SQLException;

    public WeeklySchedule getScheduleForWeek(int year, int weekNo) throws NotFoundException, SQLException;

    /**
     * Set DAO connection manual commit required
     *
     * @param manual - if manual required
     * @throws SQLException on sql connection error
     */
    public void setManualCommitRequired(boolean manual) throws SQLException;

    /**
     * Query current setting, if manual commit is required
     *
     * @return true when it need manual commit,, else return false
     * @throws SQLException upon sql connection error
     */
    public boolean getManualCommitRequired() throws SQLException;

    /**
     * Complete DAO transaction this will trigger db connection to commit and
     * then close the db connection.
     *
     * @throws SQLException
     */
    public void complete() throws SQLException;

    /**
     * Create weekly schedule in to DB
     *
     * @param valueObject - weekly schedule to be inserted
     * @throws SQLException upon sql error
     */
    public void create(WeeklySchedule valueObject)
            throws SQLException;
}
