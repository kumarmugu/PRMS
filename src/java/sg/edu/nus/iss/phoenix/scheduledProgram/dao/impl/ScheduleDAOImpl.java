/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.core.dao.DBConnection;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;

/**
 *
 * @author Mugunthan
 */
public class ScheduleDAOImpl implements ScheduleDAO {

    DBConnection dbUtil;

    
    public ScheduleDAOImpl() {
        dbUtil = new DBConnection();
    }

    @Override
    public ProgramSlot createValueObject() {
        return new ProgramSlot();
    }

    /**
     *
     * @param year
     * @param week
     * @return
     * @throws SQLException
     * @throws AnnualSchedueNotExistException
     */
    @Override
    public WeeklySchedule loadAllScheduleForWeek(WeeklySchedule ws) throws SQLException {
        Connection conn = dbUtil.openConnection();
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM `program-slot` where weekStartDate = (select startDate from `weekly-schedule` where year=? and weekNo= ?); ";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ws.getYear());
        stmt.setInt(2, ws.getWeekNo());
        ws = listQuery(stmt, ws);
        dbUtil.closeConnection(conn);
        return ws;
    }

    
    /**
     * 
     * @param ws
     * @return
     * @throws SQLException 
     */
    @Override
    public AnnualSchedule getAnnualSchedule(WeeklySchedule ws) throws SQLException {
        AnnualSchedule as = null;
        Connection conn = dbUtil.openConnection();
        ResultSet result = null;
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM `annual-schedule` where year = ?; ";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ws.getYear());
        try {
            result = stmt.executeQuery();
            while (result.next()) {
                as = new AnnualSchedule(result.getInt("year"), result.getString("assingedBy"));
            }
        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbUtil.closeConnection(conn);
        }
        return as;
    }
    
    @Override
    public WeeklySchedule loadWeekInfo(WeeklySchedule ws) throws SQLException{
        Connection conn = dbUtil.openConnection();
        ResultSet result = null;
        PreparedStatement stmt = null;
        String sql = "SELECT startDate FROM `weekly-schedule` where year = ? and weekNo = ?; ";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ws.getYear());
        stmt.setInt(2, ws.getWeekNo());
        try {
            result = stmt.executeQuery();
            while (result.next()) {
                ws.setStartDate((Date) result.getDate("startDate"));
            }
        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbUtil.closeConnection(conn);
        }
        return ws;
    }
    
    
    protected WeeklySchedule listQuery(PreparedStatement stmt, WeeklySchedule ws) throws SQLException {

        ArrayList<ProgramSlot> searchResults = new ArrayList<>();
        ResultSet result = null;
        try {
            result = stmt.executeQuery();

            while (result.next()) {
                //Need to refactor 
                ProgramSlot temp = createValueObject();
                Timestamp startDate = result.getTimestamp("programStartDateTime");
                Time duration = result.getTime("duration");
                long endDate = startDate.getTime() + (duration.getHours() * 60 + duration.getMinutes()) * 60 * 1000; // Need to modify
                temp.setStartTime(result.getTimestamp("programStartDateTime"));
                temp.setEndTime(new Date(endDate));
                temp.setProgramName(result.getString("program-name"));
                ws.setStartDate(result.getDate("weekStartDate"));
                searchResults.add(temp);
            }
            System.out.println("record size" + searchResults.size());
            ws.setProgramSlots((ArrayList<ProgramSlot>) searchResults);

        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return ws;
    }

}
