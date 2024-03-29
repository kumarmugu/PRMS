/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.core.dao.DBConnection;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.DateUtil;

/**
 *
 * @author Mugunthan, Zehua, Phyu Me Zaw, Thiri
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

    @Override
    public void setManualCommitRequired(boolean manual) throws SQLException {
        dbUtil.setAutoCommit(!manual);
    }

    @Override
    public boolean getManualCommitRequired() throws SQLException {
        return !dbUtil.isAutoCommit();
    }

    public void complete() throws SQLException {
        if (getManualCommitRequired()) {
            dbUtil.commit();
        }
        dbUtil.closeConnection();
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
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM `program-slot` where weekStartDate = (select startDate from `weekly-schedule` where year=? and weekNo= ?); ";
        stmt = dbUtil.prepareStatement(sql);
        stmt.setInt(1, ws.getYear());
        stmt.setInt(2, ws.getWeekNo());
        ws = listQuery(stmt, ws);
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
        ResultSet result = null;
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM `annual-schedule` where year = ?; ";
        stmt = dbUtil.prepareStatement(sql);
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
        }
        return as;
    }

    @Override
    public WeeklySchedule getScheduleForWeek(int year, int weekNo) throws NotFoundException, SQLException {
        PreparedStatement stmt = null;
        String sql = "select startDate from `weekly-schedule` where year=? and weekNo= ?; ";
        stmt = dbUtil.prepareStatement(sql);
        stmt.setInt(1, year);
        stmt.setInt(2, weekNo);
        ResultSet result = null;
        WeeklySchedule ws = null;
        try {
            result = stmt.executeQuery();
            if (result.next()) {
                ws = new WeeklySchedule();
                ws.setStartDate(result.getDate("startDate"));
                ws.setYear(year);
                ws.setWeekNo(weekNo);
            } else {
                throw new NotFoundException("WeeklySchedule not found.");
            }
        } finally {
            if (result != null) {
                result.close();
            }
            stmt.close();

        }
        return ws;
    }

    @Override
    public ProgramSlot getProgramSlot(Date theStartTime) throws NotFoundException, SQLException {
        ResultSet result = null;
        PreparedStatement stmt = null;
        String sql = "SELECT * FROM `program-slot` where programStartDateTime = ?;";
        stmt = dbUtil.prepareStatement(sql);
        stmt.setTimestamp(1, new java.sql.Timestamp(theStartTime.getTime()));
        ProgramSlot ps = null;
        try {
            result = stmt.executeQuery();
            if (result.next()) {
                ps = constructProgramSlot(result);
            } else {
                throw new NotFoundException("ProgramSlot not found.");
            }
        } finally {
            if (result != null) {
                result.close();
            }
            stmt.close();
        }
        return ps;
    }

    private ProgramSlot constructProgramSlot(ResultSet result) {
        ProgramSlot ps = null;
        try {
            Date startTime = new Date(result.getTimestamp("programStartDateTime").getTime());
            String programName = result.getString("program-name");
            Timestamp duration = result.getTimestamp("duration");
            Date endTime = DateUtil.AddDateTime(startTime, duration);
            ps = new ProgramSlot(startTime, endTime, programName);
            ps.setDuration(duration);
            ps.setweekStartDate(new Date(result.getDate("weekStartDate").getTime()));
            ps.setProducerId(result.getString("producerid"));
            ps.setPresenterId(result.getString("presenterid"));
            ps.setupdatedBy(result.getString("update_by"));
            ps.setupdatedOn(new Date(result.getTimestamp("update_on").getTime()));
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ps;
    }

    private void constructProgramSlotSQLStatement(PreparedStatement stmt, ProgramSlot valueObject) {
        try {
            stmt.setTime(1, new java.sql.Time(valueObject.getduration().getTime()));
            stmt.setTimestamp(2, DateUtil.getTimestamp(valueObject.getStartTime()));
            stmt.setDate(3, new java.sql.Date(valueObject.getweeekStartDate().getTime()));
            stmt.setString(4, valueObject.getProducerId());
            stmt.setString(5, valueObject.getPresenterId());
            stmt.setString(6, valueObject.getProgramName());
            stmt.setString(7, valueObject.getupdatedBy());
            stmt.setTimestamp(8, DateUtil.getTimestamp(valueObject.getupdatedOn()));
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected WeeklySchedule listQuery(PreparedStatement stmt, WeeklySchedule ws) throws SQLException {

        ArrayList<ProgramSlot> searchResults = new ArrayList<>();
        ResultSet result = null;
        try {
            result = stmt.executeQuery();
            while (result.next()) {
                ProgramSlot ps = constructProgramSlot(result); // Refactored by zehua
                if (ps != null) {
                    searchResults.add(ps);
                }
                //Need to refactor 
                //ProgramSlot temp = createValueObject();
                //Timestamp startDate = result.getTimestamp("programStartDateTime");
                //Time duration = result.getTime("duration");
                //long endDate = startDate.getTime() + (duration.getHours() * 60 + duration.getMinutes()) * 60 * 1000; // Need to modify
                //temp.setStartTime(result.getTimestamp("programStartDateTime"));
                //temp.setEndTime(new Date(endDate));
                //temp.setProgramName(result.getString("program-name"));
                ws.setStartDate(result.getDate("weekStartDate"));
                //searchResults.add(temp);
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

    public Boolean processCreateAnnualSchedule(AnnualSchedule as, ArrayList<WeeklySchedule> wsList) throws SQLException {

        Boolean success = true;
        String sql = "";
        PreparedStatement insertAnnualSchedulestmt = null;
        PreparedStatement insertWeeklySchedulestmt = null;
        try {
            sql = "INSERT INTO `annual-schedule` ( year, assingedBy ) "
                    + " VALUES (?, ?) ";
            insertAnnualSchedulestmt = dbUtil.prepareStatement(sql);
            insertAnnualSchedulestmt.setInt(1, as.getYear());
            insertAnnualSchedulestmt.setString(2, as.getAssignedBy());

            dbUtil.setAutoCommit(false);
            insertAnnualSchedulestmt.executeUpdate();

            for (WeeklySchedule ws : wsList) {
                sql = "INSERT INTO `weekly-schedule` (startDate, year, weekNo ) "
                        + " VALUES (?, ?, ?) ";

                insertWeeklySchedulestmt = dbUtil.prepareStatement(sql);

                java.sql.Date sqlgetStartDate = new java.sql.Date(ws.getStartDate().getTime());

                insertWeeklySchedulestmt.setDate(1, sqlgetStartDate);
                insertWeeklySchedulestmt.setInt(2, ws.getYear());
                insertWeeklySchedulestmt.setInt(3, ws.getWeekNo());
                insertWeeklySchedulestmt.executeUpdate();
            }

            dbUtil.commit();

        } catch (SQLException e) {

            e.printStackTrace();

            try {
                System.err.print("Transaction is being rolled back");
                dbUtil.rollback();
                success = false;
            } catch (SQLException excep) {

                excep.printStackTrace();
            }
            throw (e);

        } finally {
            if (insertAnnualSchedulestmt != null) {
                insertAnnualSchedulestmt.close();
            }
            if (insertWeeklySchedulestmt != null) {
                insertWeeklySchedulestmt.close();
            }
        }

        return success;

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean create(ProgramSlot valueObject) throws SQLException {
        Boolean success = true;
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "INSERT INTO `program-slot` (`duration`, `programStartDateTime`, `weekStartDate`, `producerid`, `presenterid`, `program-name`, `update_by`, `update_on`) VALUES "
                    + "(?,?,?,?,?,?,?,?); ";
            stmt = dbUtil.prepareStatement(sql);
            constructProgramSlotSQLStatement(stmt, valueObject);
            int rowcount = stmt.executeUpdate();
            return rowcount == 1;
        } catch (SQLException e) {

            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void create(WeeklySchedule ws)
            throws SQLException {
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "INSERT INTO `weekly-schedule` (startDate, year, weekNo ) "
                    + " VALUES (?, ?, ?) ";

            stmt = dbUtil.prepareStatement(sql);

            java.sql.Date sqlgetStartDate = new java.sql.Date(ws.getStartDate().getTime());

            stmt.setDate(1, sqlgetStartDate);
            stmt.setInt(2, ws.getYear());
            stmt.setInt(3, ws.getWeekNo());
            stmt.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
            throw e;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(ProgramSlot valueObject) throws NotFoundException, SQLException {
        Boolean success = true;
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "UPDATE `program-slot` SET `duration` = ?, `programStartDateTime` = ?, `weekStartDate` = ?, `producerid` = ?, `presenterid` = ?, `program-name` = ?, `update_by` = ?, `update_on` =? "
                    + "WHERE (`programStartDateTime` = ?); ";
            stmt = dbUtil.prepareStatement(sql);
            constructProgramSlotSQLStatement(stmt, valueObject);
            stmt.setTimestamp(9, new java.sql.Timestamp(valueObject.getStartTime().getTime()));
            int rowcount = stmt.executeUpdate();
            if (rowcount == 0) {
                throw new NotFoundException("ProgramSlot not found.");
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw e;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void delete(ProgramSlot valueObject)
            throws NotFoundException, SQLException {
        if (valueObject.getStartTime() == null) {
            throw new NotFoundException("Can not delete without Primary-Key!");
        }

        String sql = "DELETE FROM `program-slot` WHERE (`programStartDateTime` = ? ); ";
        PreparedStatement stmt = null;
        try {
            stmt = dbUtil.prepareStatement(sql);
            System.out.println(new java.sql.Timestamp(valueObject.getStartTime().getTime()));
            stmt.setTimestamp(1, new java.sql.Timestamp(valueObject.getStartTime().getTime()));

            int rowcount = stmt.executeUpdate();
            if (rowcount == 0) {
                // System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException(
                        "Object could not be deleted! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                // System.out.println("PrimaryKey Error when updating DB! (Many objects were deleted!)");
                throw new SQLException(
                        "PrimaryKey Error when updating DB! (Many objects were deleted!)");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

}
