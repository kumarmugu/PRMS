/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;
import sg.edu.nus.iss.phoenix.radioprogram.dao.ProgramDAO;
import sg.edu.nus.iss.phoenix.radioprogram.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.DateUtil;

/**
 *
 * @author Rong
 */
public class ScheduledProgramServiceTest {
    
    //    @Mock
    private DAOFactoryImpl factory;

    //    @Mock
    private ScheduleDAO spDao;
    private ProgramDAO rpDao;
    private PresenterDAO preDao;
    private ProducerDAO proDao;
    private UserDao userDAO;
    
    
    private final Map<AnnualSchedule, ArrayList<WeeklySchedule>> annualsWeeks;   
    private final Map<String, RadioProgram> radioPrograms;
    private final Map<String, User> users;
    private final Map<String, Presenter> presenters;
    private final Map<String, Producer> producers;
    private static final Map<Long, ProgramSlot> programSlots = new HashMap<>(); 
    
    
    private final Answer createPS, updatePS, deletePS;
    private final Answer loadWeeklyPS;
    
    ScheduledProgramService service;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    public ScheduledProgramServiceTest() {
        this.annualsWeeks = new HashMap<>();
        this.radioPrograms = new HashMap<>();
        this.users = new HashMap<>();
        this.presenters = new HashMap<>();
        this.producers = new HashMap<>();
        
        updatePS = (Answer<Object>) (InvocationOnMock invocation) -> {
            ProgramSlot ps = invocation.getArgumentAt(0, ProgramSlot.class);
            if (ScheduledProgramServiceTest.programSlots.containsKey(ps.getID()) == true) {
                ScheduledProgramServiceTest.programSlots.put(ps.getID(), ps);
            }
            else {
                throw new NotFoundException();
            }
            return null;
        };
        
        deletePS = (Answer<Object>) (InvocationOnMock invocation) -> {
            ProgramSlot ps = invocation.getArgumentAt(0, ProgramSlot.class);
            if (ScheduledProgramServiceTest.programSlots.containsKey(ps.getID()) == true) {
                ScheduledProgramServiceTest.programSlots.remove(ps.getID());
                Mockito.doThrow(new SQLException()).when(spDao).delete(ps);
            }
            else {
                throw new NotFoundException();
            }
            return null;
        };
        
        createPS = (Answer<Object>) (InvocationOnMock invocation) -> {
            ProgramSlot ps = invocation.getArgumentAt(0, ProgramSlot.class);
            if (ScheduledProgramServiceTest.programSlots.containsKey(ps.getID()) == false) {
                ScheduledProgramServiceTest.programSlots.put(ps.getID(), ps);
                
                Mockito.doThrow(new SQLException()).when(spDao).create(ps);
                Mockito.doAnswer(updatePS).when(spDao).update(ps);
                Mockito.doAnswer(deletePS).when(spDao).delete(ps);
                when(spDao.getProgramSlot(ps.getStartTime())).thenReturn(ps);
            }
            else {
                throw new NotFoundException();
            }
            return true;
        };
        
        loadWeeklyPS = (Answer<Object>) (InvocationOnMock invocation) -> {
            WeeklySchedule ws = invocation.getArgumentAt(0, WeeklySchedule.class);
            ArrayList<ProgramSlot> programSlots1 = new ArrayList<>();
            ScheduledProgramServiceTest.programSlots.values().stream().filter((eps) -> (eps.getWeek() == ws.getWeekNo() && 
                    eps.getYear() == ws.getYear())).forEach((eps) -> {
                        programSlots1.add(eps);
            });
            ws.setProgramSlots(programSlots1);
            return ws;
        };
    }

    /**
     * This method will execute before test case execute. This method mock the
     * ScheduleDAO, DAOFactoryImpl. Also it mocks methods of ScheduleDAO with
     * some outputs.
     *
     * @throws SQLException
     */
    @Before
    public void setUp() throws SQLException, NotFoundException {
        spDao = mock(ScheduleDAO.class);
        rpDao = mock(ProgramDAO.class);
        preDao = mock(PresenterDAO.class);
        proDao = mock(ProducerDAO.class);
        userDAO = mock(UserDao.class);
        factory = mock(DAOFactoryImpl.class);
        
        service = new ScheduledProgramService();
        service.factory = factory;       
        service.spDao = spDao;       
        service.rpDao = rpDao;       
        service.preDao = preDao;       
        service.proDao = proDao;       
        service.userDAO = userDAO;     
        
        Mockito.doNothing().when(spDao).setManualCommitRequired(true);
        Mockito.doNothing().when(spDao).setManualCommitRequired(false);
        Mockito.doNothing().when(spDao).complete();      

        createUser("user1", false, false);
        createUser("user2", false, true);
        createUser("user3", true, true);
        createUser("user4", true, false);

        createWeeklySchedules(2016, "user1");
        createWeeklySchedules(2017, "user1");
        
        createRadioPrograms("Radio Program 1");
        createRadioPrograms("Radio Program 2");
        createRadioPrograms("Radio Program 3");
        createRadioPrograms("Radio Program 4");
    }
    
    /**
     * Test Data Operation: 
     * Create weekly schedule for the whole year
     * @param year - year in integer
     * @param name - name of the creator/user
     * @throws SQLException
     * @throws NotFoundException 
     */
    private void createWeeklySchedules(int year, String name) throws SQLException, NotFoundException {
        AnnualSchedule as = new AnnualSchedule(year, name);
        ArrayList<WeeklySchedule> wsList = new ArrayList<>();
        WeeklySchedule ws;
        
        for (int i = 1; i < 54; i++) {
            ws = new WeeklySchedule();
            if (i == 1) {
                ws.setStartDate(DateUtil.getFirstDayOfYear(as.getYear()));
            } else {
                ws.setStartDate(DateUtil.getStartDateOfWeek(as.getYear(), i));
            }
            ws.setWeekNo(i);
            ws.setYear(as.getYear());
            wsList.add(ws);
            
            when(spDao.getScheduleForWeek(year, i)).thenReturn(ws);
            when(spDao.getAnnualSchedule(ws)).thenReturn(as);
            
            Mockito.doAnswer(loadWeeklyPS).when(spDao).loadAllScheduleForWeek(ws);
        }
        annualsWeeks.put(as, wsList);
    }

    /**
     * Test Data Operation:
     * Create Radio programs
     * @param name - radio program name
     * @throws NotFoundException
     * @throws SQLException 
     */
    private void createRadioPrograms(String name) throws NotFoundException, SQLException{
        RadioProgram rp = new RadioProgram();
        rp.setName(name);
        this.radioPrograms.put(name, rp);
        when(rpDao.getObject(name)).thenReturn(rp);            
    }
    
    /**
     * Test Data Operation:
     *  Create User Data
     * @param userID   - user id
     * @param isPresenter - if it is presenter
     * @param isProducer  - if it is producer
     * @throws NotFoundException
     * @throws SQLException 
     */
    private void createUser(String userID, 
            boolean isPresenter, 
            boolean isProducer) throws NotFoundException, SQLException{
        User user = new User(userID);
        user.setName("Test user " + userID);
        
        this.users.put(userID, user);
        when(userDAO.getObject(userID)).thenReturn(user);   
        
        if(isPresenter) {
            Presenter presenter  = new Presenter(user.getId(), user.getName(), "test presenter");
            this.presenters.put(userID, presenter);
            List<Presenter> list = new ArrayList<>();
            list.add(presenter);
            when(this.preDao.findPresenter(user.getName())).thenReturn(list);   
        }
        
        if(isProducer) {
            Producer producer  = new Producer(user.getId(), user.getName());
            this.producers.put(userID, producer);
            List<Producer> list = new ArrayList<>();
            list.add(producer);
            when(this.proDao.findProducer(user.getName())).thenReturn(list);   
        }
    }    
    
    /**
     * Test Data Operation:
     *  Set up initial mock answer for DAO operation
     * @param ps
     * @throws NotFoundException
     * @throws SQLException 
     */
    private void setUpDaoForProgramSlot(ProgramSlot ps) throws NotFoundException, SQLException {
        Mockito.doAnswer(createPS).when(spDao).create(ps);
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test Normal process flow
     *  1. Create One schedule Program: Date: 2016-10-05
     *  2. Copy another one
     *  3. Modify the sec one
     *  4. Delete sec one
     *  5. Delete first one
     */

    @Test   
    public void testNoramlProcess() {
        // Create, copy, modify , delete delete.
       Map<String, String[]> parameters = new HashMap<>();
       
       parameters.put("program", new String[]{radioPrograms.entrySet().iterator().next().getKey()});
       parameters.put("presenterId", new String[]{this.presenters.entrySet().iterator().next().getKey()});
       parameters.put("producerId", new String[]{this.producers.entrySet().iterator().next().getKey()});
       parameters.put("date", new String[]{"2016-10-05"});
       parameters.put("startTime", new String[]{"01:00"});
       parameters.put("endTime", new String[]{"02:00"});
       
       User user = this.users.entrySet().iterator().next().getValue();
       
       List<Long> ids = new ArrayList<>();
       
       try {
            ProgramSlot ps = service.constructProgramSlot(parameters, user);
            setUpDaoForProgramSlot(ps);
            service.PorcessCreate(ps);
            ids.add(ps.getID());
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       
       parameters.put("startTime", new String[]{"02:00"});
       parameters.put("endTime", new String[]{"04:00"});
       try {
            ProgramSlot ps = service.constructProgramSlot(parameters, user);
            setUpDaoForProgramSlot(ps);
            service.PorcessCopy(ps);
            ids.add(ps.getID());
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       parameters.put("startTime", new String[]{"03:00"});
       parameters.put("endTime", new String[]{"09:00"});
       try {
            ProgramSlot newPs = service.constructProgramSlot(parameters, user);
            long id = ids.get(ids.size() - 1);
            setUpDaoForProgramSlot(newPs);
            ProgramSlot oldPs = service.getProgramSlot(id);
            service.processModify(oldPs, newPs);
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       try {
            ProgramSlot newPs = service.constructProgramSlot(parameters, user);
            service.processDelete(newPs);
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       parameters.put("startTime", new String[]{"01:00"});
       parameters.put("endTime", new String[]{"02:00"});
       try {
            ProgramSlot newPs = service.constructProgramSlot(parameters, user);
            service.processDelete(newPs);
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       assertTrue(programSlots.size() == 0);
    }
    
    /**
     * Test Stress Test process flow
     *  1. Create One schedule Program: Date: 2016-10-05
     *  2. Copy another one twice, expect exception on sec copy
     *  3. Modify the sec one, to overlapping with first
     *  4. Delete sec one
     *  5. Delete first one
     */
    @Test   
    public void testStressTestProcess() {
        // Create, copy, modify , delete delete.
       Map<String, String[]> parameters = new HashMap<>();
       
       parameters.put("program", new String[]{radioPrograms.entrySet().iterator().next().getKey()});
       parameters.put("presenterId", new String[]{this.presenters.entrySet().iterator().next().getKey()});
       parameters.put("producerId", new String[]{this.producers.entrySet().iterator().next().getKey()});
       parameters.put("date", new String[]{"2016-10-05"});
       parameters.put("startTime", new String[]{"01:00"});
       parameters.put("endTime", new String[]{"03:00"});
       
       User user = this.users.entrySet().iterator().next().getValue();
       
       List<Long> ids = new ArrayList<>();
       
       try {
            ProgramSlot ps = service.constructProgramSlot(parameters, user);
            setUpDaoForProgramSlot(ps);
            service.PorcessCreate(ps);
            ids.add(ps.getID());
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       
       parameters.put("startTime", new String[]{"03:00"});
       parameters.put("endTime", new String[]{"09:00"});
       try {
            ProgramSlot ps = service.constructProgramSlot(parameters, user);
            setUpDaoForProgramSlot(ps);
            service.PorcessCopy(ps);
            ids.add(ps.getID());
            try {
                service.PorcessCopy(ps); //Create at same time slot
                fail();
            }catch (Exception ex) {
                //expected fails due to already exists
                String message = ex.getMessage();
            }
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }       
       
       parameters.put("startTime", new String[]{"02:00"});
       parameters.put("endTime", new String[]{"04:00"});
       try {
            ProgramSlot newPs = service.constructProgramSlot(parameters, user);
            long id = ids.get(ids.size() - 1);
            setUpDaoForProgramSlot(newPs);
            ProgramSlot oldPs = service.getProgramSlot(id);
            service.processModify(oldPs, newPs);
            fail(); 
        } catch (Exception ex) {
            //expected fails due to overlapping
            String message = ex.getMessage();
            //Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }       
              
       parameters.put("startTime", new String[]{"03:00"});
       try {
            ProgramSlot newPs = service.constructProgramSlot(parameters, user);
            service.processDelete(newPs);
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       parameters.put("startTime", new String[]{"01:00"});
       parameters.put("endTime", new String[]{"02:00"});
       try {
            ProgramSlot newPs = service.constructProgramSlot(parameters, user);
            service.processDelete(newPs);
        } catch (Exception ex) {
            Logger.getLogger(ScheduledProgramServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
       assertTrue(programSlots.size() == 0);
    }
}
