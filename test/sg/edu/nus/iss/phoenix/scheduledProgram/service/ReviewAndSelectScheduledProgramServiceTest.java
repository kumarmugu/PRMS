/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;

/**
 *
 * @author Mugunthan
 */
public class ReviewAndSelectScheduledProgramServiceTest {

//    @Mock
    private DAOFactoryImpl factory;

//    @Mock
    private ScheduleDAO spdao;

    ReviewAndSelectScheduledProgramService reviewSelectScheduledService;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * This method will execute before test case execute. This method mock the
     * ScheduleDAO, DAOFactoryImpl. Also it mocks methods of ScheduleDAO with
     * some outputs.
     *
     * @throws SQLException
     */
    @Before
    public void setUp() throws SQLException {
        spdao = mock(ScheduleDAO.class);
        factory = mock(DAOFactoryImpl.class);
        //MockitoAnnotations.initMocks(this);
        reviewSelectScheduledService = new ReviewAndSelectScheduledProgramService();
        reviewSelectScheduledService.factory = factory;
        reviewSelectScheduledService.spdao = spdao;

        Calendar cal = Calendar.getInstance();

        WeeklySchedule ws1 = new WeeklySchedule(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
        when(spdao.getAnnualSchedule(ws1)).thenReturn(new AnnualSchedule(cal.get(Calendar.YEAR), "user1"));
        ws1.setStartDate(new Date());
        when(spdao.loadWeekInfo(ws1)).thenReturn(ws1);
        ArrayList<ProgramSlot> programSlots1 = new ArrayList<ProgramSlot>();
        for (int i = 0; i < 5; i++) {
            programSlots1.add(new ProgramSlot());
        }
        ws1.setProgramSlots(programSlots1);
        when(spdao.loadAllScheduleForWeek(ws1)).thenReturn(ws1);

        WeeklySchedule ws2 = new WeeklySchedule(2017, 12);
        when(spdao.getAnnualSchedule(ws2)).thenReturn(new AnnualSchedule(2017, "user2"));
        when(spdao.loadWeekInfo(ws2)).thenReturn(ws2);
        when(spdao.loadAllScheduleForWeek(ws2)).thenReturn(ws2);
//
        WeeklySchedule ws3 = new WeeklySchedule(2018, 10);
        when(spdao.getAnnualSchedule(ws3)).thenReturn(null);

        ArrayList<ProgramSlot> programSlots2 = new ArrayList<ProgramSlot>();
        WeeklySchedule ws4 = new WeeklySchedule(2016, 10);
        ws3.setProgramSlots(programSlots2);
        when(spdao.getAnnualSchedule(ws4)).thenReturn(new AnnualSchedule(2016, "user2"));
        when(spdao.loadWeekInfo(ws4)).thenThrow(SQLException.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * This is the testReviewSelectScheduledProgramWithNullValues test method
     * for reviewSelectScheduledProgram method in reviewSelectScheduledService
     * This method checks the size of the program slot in the output, when the
     * method arguments is equal to null
     *
     * @throws AnnualSchedueNotExistException
     * @throws SQLException
     */
    @Test
    public void testReviewSelectScheduledProgramWithNullValues() throws AnnualSchedueNotExistException, SQLException {

        WeeklySchedule wx = reviewSelectScheduledService.reviewSelectScheduledProgram(null, null);
        assertEquals(5, wx.getProgramSlots().size());

    }

    /**
     * This is the testReviewSelectScheduledProgramNoProgramSlots test method
     * for reviewSelectScheduledProgram method in reviewSelectScheduledService
     * This method expect the empty program slot list in the output when
     * arguments are 2017 and 12
     *
     * @throws AnnualSchedueNotExistException
     * @throws SQLException
     */
    @Test
    public void testReviewSelectScheduledProgramNoProgramSlots() throws AnnualSchedueNotExistException, SQLException {

        WeeklySchedule wx = reviewSelectScheduledService.reviewSelectScheduledProgram("2017", "12");
        assertEquals(0, wx.getProgramSlots().size());

    }

    /**
     * This is the
     * testReviewSelectScheduledProgramThrowAnnualSchedueNotExistException test
     * method for reviewSelectScheduledProgram method in
     * reviewSelectScheduledService This method expect
     * AnnualSchedueNotExistException when it try to fetch the schedule data
     * from DB for non exist year
     *
     * @throws AnnualSchedueNotExistException
     * @throws SQLException
     */
    @Test(expected = AnnualSchedueNotExistException.class)
    public void testReviewSelectScheduledProgramThrowAnnualSchedueNotExistException() throws AnnualSchedueNotExistException, SQLException {

        WeeklySchedule wx = reviewSelectScheduledService.reviewSelectScheduledProgram("2018", "10");

    }

    /**
     * This is the
     * testReviewSelectScheduledProgramThrowAnnualSchedueNotExistException test
     * method for reviewSelectScheduledProgram method in
     * reviewSelectScheduledService This method expect to get the current week
     * schedule when wrong arguments pass as parameters
     *
     * @throws AnnualSchedueNotExistException
     * @throws SQLException
     */
    @Test
    public void testReviewSelectScheduledProgramWithWrongInput() throws AnnualSchedueNotExistException, SQLException {

        WeeklySchedule wx = reviewSelectScheduledService.reviewSelectScheduledProgram("abc", "aa");
        assertEquals(5, wx.getProgramSlots().size());

    }

    /**
     * This is the
     * testReviewSelectScheduledProgramThrowAnnualSchedueNotExistException test
     * method for reviewSelectScheduledProgram method in
     * reviewSelectScheduledService This method expect SQLException where data
     * is inconsistency in the database
     *
     * @throws AnnualSchedueNotExistException
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void testReviewSelectScheduledProgramThrowSQLException() throws AnnualSchedueNotExistException, SQLException {
        WeeklySchedule wx = reviewSelectScheduledService.reviewSelectScheduledProgram("2016", "10");
        fail();
    }

}
