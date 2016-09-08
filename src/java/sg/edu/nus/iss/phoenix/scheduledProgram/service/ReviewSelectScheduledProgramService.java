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
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;

/**
 *
 * @author Mugunthan
 */
public class ReviewSelectScheduledProgramService {

    DAOFactoryImpl factory;
    ScheduleDAO spdao;

    public ReviewSelectScheduledProgramService() {
        super();
        factory = new DAOFactoryImpl();
        spdao = factory.getScheduleDAO();
    }

    public void processDelete(String id) {
    }

    public WeeklySchedule reviewSelectScheduledProgram(String year, String week) throws AnnualSchedueNotExistException {
        WeeklySchedule ws = null;
        try {
            if (year != null && year.matches("^-?\\d{4}+$") && week != null && week.matches("^-?\\d+$")) {
                ws = new WeeklySchedule(Integer.parseInt(year), Integer.parseInt(week));
                ws = spdao.loadAllForWeek(ws);
            } else {
                Calendar cal = Calendar.getInstance();
                ws = new WeeklySchedule(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
                ws = spdao.loadAllForWeek(ws);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReviewSelectScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ws;
    }
}
