/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * This is the constructor method of ReviewAndSelectScheduledProgramService
     */
    public ReviewAndSelectScheduledProgramService() {
        super();
        factory = new DAOFactoryImpl();
        spdao = factory.getScheduleDAO();
        userDAO = factory.getUserDAO();
    }

    /**
     * This method will return all the scheduled programs for a week. As default
     * this method will return all the scheduled program for current week(year
     * and week parameters are null).
     *
     * If the year is not exist in the application it will throw
     * AnnualSchedueNotExistException
     *
     * @param year - Current year will be used if this parameter is null
     * @param week - Current week will be used if this parameter is null
     * @return WeeklySchedule - Includes list of program slot for a week
     * @throws AnnualSchedueNotExistException - If annual schedule not exist
     */
    public WeeklySchedule reviewSelectScheduledProgram(String year, String week) throws AnnualSchedueNotExistException, SQLException {
        WeeklySchedule ws = null;
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(1);
        cal.setMinimalDaysInFirstWeek(1);
        int yearToSearch = cal.get(Calendar.YEAR);
        int weekToSearch = cal.get(Calendar.WEEK_OF_YEAR);
        try {
            if (year != null && year.matches("^-?\\d{4}+$") && week != null && week.matches("^-?\\d+$")) {
                yearToSearch = Integer.parseInt(year);
                weekToSearch = Integer.parseInt(week);
            }
            ws = new WeeklySchedule(yearToSearch, weekToSearch);
            ws.setStartDate(DateUtil.getStartDateOfWeek(yearToSearch, weekToSearch));
            AnnualSchedule as = spdao.getAnnualSchedule(ws);

            if (as != null) {
                ws = spdao.loadAllScheduleForWeek(ws);
                for (ProgramSlot ps : ws.getProgramSlots()) {
                    try {
                        ps.setPresenterName(userDAO.getObject(ps.getPresenterId()).getName());
                        ps.setProducerName(userDAO.getObject(ps.getProducerId()).getName());
                    } catch (NotFoundException ex) {
                        Logger.getLogger(ReviewAndSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                throw new AnnualSchedueNotExistException("Annual Schedule not exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReviewAndSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return ws;
    }
}
