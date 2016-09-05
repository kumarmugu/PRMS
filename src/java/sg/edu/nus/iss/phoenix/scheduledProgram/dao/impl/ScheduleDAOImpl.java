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

/**
 *
 * @author Mugunthan
 */
public class ScheduleDAOImpl implements ScheduleDAO {

    DBConnection dbUtil;
    
    public ScheduleDAOImpl(){
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
    public ArrayList<ProgramSlot> loadAllForWeek(int year, int week) throws SQLException, AnnualSchedueNotExistException {
        if (isAnnualScheduleExist(year)) {
            Connection conn = dbUtil.openConnection();
            PreparedStatement stmt = null;
            String sql = "SELECT * FROM `program-slot` where weekStartDate = (select startDate from `weekly-schedule` where year=? and weekNo= ?); ";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, year);
            stmt.setInt(2, week);
            ArrayList<ProgramSlot> searchResults = listQuery(stmt);
            dbUtil.closeConnection(conn);
            System.out.println("record size" + searchResults.size());
            return searchResults;
        } else {
            throw new AnnualSchedueNotExistException("Annual Schedule not exist");
        }
    }

    private boolean isAnnualScheduleExist(int year) throws SQLException {
        boolean isExist = false;
        Connection conn = dbUtil.openConnection();
        ResultSet result = null;
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM `annual-schedule` where year = ?; ";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, year);
        try {
            result = stmt.executeQuery();
            while (result.next()) {
                isExist = true;
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
        return isExist;
    }

    protected ArrayList<ProgramSlot> listQuery(PreparedStatement stmt) throws SQLException {

        ArrayList<ProgramSlot> searchResults = new ArrayList<>();
        ResultSet result = null;
        try {
            result = stmt.executeQuery();

            while (result.next()) {
                //Need to refactor 
                ProgramSlot temp = createValueObject();
                Timestamp startDate = result.getTimestamp("programStartDateTime");
                Time duration = result.getTime("duration");
                long endDate = startDate.getTime() + 60 * 60 * 1000; // Need to modify
                temp.setStartTime(result.getTimestamp("programStartDateTime"));
                temp.setEndTime(new Date(endDate));
                temp.setProgramName(result.getString("program-name"));

                searchResults.add(temp);
            }

        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return (ArrayList<ProgramSlot>) searchResults;
    }

    

}
