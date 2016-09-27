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
 * @author Mugunthan
 */
public  class ScheduleDAOImpl implements ScheduleDAO {

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

 
    public  Boolean processCreateAnnualSchedule(AnnualSchedule as, ArrayList<WeeklySchedule> wsList) throws SQLException {
        
         Connection conn = dbUtil.openConnection();
         Boolean success =true;
         String sql = "";
		PreparedStatement insertAnnualSchedulestmt = null;
                PreparedStatement insertWeeklySchedulestmt = null;
		try {
			sql = "INSERT INTO `annual-schedule` ( year, assingedBy ) "
					+ " VALUES (?, ?) ";
			 insertAnnualSchedulestmt = conn.prepareStatement(sql);
                         insertAnnualSchedulestmt.setInt(1, as.getYear());
                         insertAnnualSchedulestmt.setString(2,as.getAssignedBy());
                         
                         
                         conn.setAutoCommit(false);
                            insertAnnualSchedulestmt.executeUpdate();
                                                   
                            
                            for(WeeklySchedule ws: wsList)  
                          {
                              sql = "INSERT INTO `weekly-schedule` (startDate, year, weekNo ) "
					+ " VALUES (?, ?, ?) ";
                              
                                insertWeeklySchedulestmt = conn.prepareStatement(sql);
                                
                                 java.sql.Date sqlgetStartDate = new java.sql.Date(ws.getStartDate().getTime());
                                
                                insertWeeklySchedulestmt.setDate(1,sqlgetStartDate);
                                insertWeeklySchedulestmt.setInt(2, ws.getYear());
                                insertWeeklySchedulestmt.setInt(3,ws.getWeekNo());
                                insertWeeklySchedulestmt.executeUpdate();
                          }
                          
                          conn.commit();                       

                    } catch (SQLException e ) {
                        
                        e.printStackTrace();
                                   
                        if (conn != null) {
                            try {
                                System.err.print("Transaction is being rolled back");
                                conn.rollback();
                                success =false;
                            } catch(SQLException excep) {      
                                
                                excep.printStackTrace();
                        }
        }
             
                    } finally {
			if (insertAnnualSchedulestmt != null)
				insertAnnualSchedulestmt.close();
                        if (insertWeeklySchedulestmt != null)
				insertWeeklySchedulestmt.close();
                        conn.close();
		}
        
                return success;
        
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
    public void create(ProgramSlot valueObject)  {
        
        
		     Connection conn = dbUtil.openConnection();
         Boolean success =true;
         String sql = "";
		PreparedStatement stmt = null;
              
		try {
			sql = "INSERT INTO `program-slot` (`duration`, `programStartDateTime`, `weekStartDate`, `producer_id`, `presenter_id`, `program-name`, `update_by`, `update_on`) VALUES "+ 
                              "(?,?,?,?,?,?,?,?); ";
			stmt = conn.prepareStatement(sql);
			stmt.setTime(1, valueObject.getduration());
                        stmt.setTimestamp(2, DateUtil.getTimestamp(valueObject.getStartTime()));
                        stmt.setDate(3, new java.sql.Date(valueObject.getweeekStartDate().getTime()));
			stmt.setString(4, valueObject.getProducerId());
                        stmt.setString(5, valueObject.getPresenterId());
                        stmt.setString(6, valueObject.getProgramName());
                        stmt.setString(7,valueObject.getupdatedBy());                       
                        stmt.setTimestamp(8,DateUtil.getTimestamp(valueObject.getupdatedOn()));
  
			int rowcount = stmt.executeUpdate();
                    } catch (SQLException e ) {
                        
                        e.printStackTrace();
              
                    } finally {
			if (stmt != null)
				try {
                                    stmt.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       
                         try {
                             conn.close();
                         } catch (SQLException ex) {
                             Logger.getLogger(ScheduleDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                         }
		}
        
               // return success;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   public  void delete(ProgramSlot valueObject)
			throws NotFoundException, SQLException{
                 if (valueObject.getStartTime() == null) {
			throw new NotFoundException("Can not delete without Primary-Key!");
		}

		String sql = "DELETE FROM `program-slot` WHERE (`programStartDateTime` = ? ); ";
		PreparedStatement stmt = null;
		Connection conn = dbUtil.openConnection();
		try {
			stmt = conn.prepareStatement(sql);
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
			if (stmt != null)
				stmt.close();
			conn.close();
		}
   }
   

   
  

}
