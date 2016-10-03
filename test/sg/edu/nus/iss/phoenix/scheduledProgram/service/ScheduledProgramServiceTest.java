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
    
    
    private Answer createPS, updatePS, deletePS, loadWeeklyPS ;
    
    
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
        
        createPS = new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) throws SQLException, NotFoundException {
            ProgramSlot ps = invocation.getArgumentAt(0, ProgramSlot.class);
            if (ScheduledProgramServiceTest.programSlots.containsKey(ps.getID()) == false) {
                ScheduledProgramServiceTest.programSlots.put(ps.getID(), ps);
                
                Mockito.doThrow(new SQLException()).when(spDao).create(ps);
                Mockito.doAnswer(updatePS).when(spDao).update(ps);
                Mockito.doAnswer(deletePS).when(spDao).delete(ps);
            }
            else {
                throw new NotFoundException();
            }
            return true;
        } };
        
        updatePS = new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) throws NotFoundException {
            ProgramSlot ps = invocation.getArgumentAt(0, ProgramSlot.class);
            if (ScheduledProgramServiceTest.programSlots.containsKey(ps.getID()) == true) {
                ScheduledProgramServiceTest.programSlots.put(ps.getID(), ps);
            }
            else {
                throw new NotFoundException();
            }
            return null;
        } };
        
        deletePS = new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) throws NotFoundException, SQLException {
            ProgramSlot ps = invocation.getArgumentAt(0, ProgramSlot.class);
            if (ScheduledProgramServiceTest.programSlots.containsKey(ps.getID()) == true) {
                ScheduledProgramServiceTest.programSlots.remove(ps.getID());
                Mockito.doThrow(new SQLException()).when(spDao).delete(ps);
            }
            else {
                throw new NotFoundException();
            }
            return null;
        } };
        
        loadWeeklyPS = new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) throws NotFoundException, SQLException {
            WeeklySchedule ws = invocation.getArgumentAt(0, WeeklySchedule.class);
            ArrayList<ProgramSlot> programSlots = new ArrayList<>();
            for(ProgramSlot eps : ScheduledProgramServiceTest.programSlots.values()) {
                if (eps.getWeek() == ws.getWeekNo() && 
                    eps.getYear() == ws.getYear())
                    programSlots.add(eps);
            }
            ws.setProgramSlots(programSlots);
            return ws;
        } };
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
        //MockitoAnnotations.initMocks(this);
        

        
           
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
        
        
        
        
//        
//        
//        Calendar cal = Calendar.getInstance();
//
//        WeeklySchedule ws1 = new WeeklySchedule(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
//        when(spdao.getAnnualSchedule(ws1)).thenReturn(new AnnualSchedule(cal.get(Calendar.YEAR), "user1"));
////        ws1.setStartDate(new Date());
////        when(spdao.loadWeekInfo(ws1)).thenReturn(ws1);
//        ArrayList<ProgramSlot> programSlots1 = new ArrayList<ProgramSlot>();
//        for (int i = 0; i < 5; i++) {
//            programSlots1.add(new ProgramSlot());
//        }
//        ws1.setProgramSlots(programSlots1);
//        when(spdao.loadAllScheduleForWeek(ws1)).thenReturn(ws1);
//
//        WeeklySchedule ws2 = new WeeklySchedule(2017, 12);
//        when(spdao.getAnnualSchedule(ws2)).thenReturn(new AnnualSchedule(2017, "user2"));
////        when(spdao.loadWeekInfo(ws2)).thenReturn(ws2);
//        when(spdao.loadAllScheduleForWeek(ws2)).thenReturn(ws2);
////
//        WeeklySchedule ws3 = new WeeklySchedule(2018, 10);
//        when(spdao.getAnnualSchedule(ws3)).thenReturn(null);
//
//        ArrayList<ProgramSlot> programSlots2 = new ArrayList<ProgramSlot>();
//        WeeklySchedule ws4 = new WeeklySchedule(2016, 10);
//        ws3.setProgramSlots(programSlots2);
//        when(spdao.getAnnualSchedule(ws4)).thenReturn(new AnnualSchedule(2016, "user2"));
//        when(spdao.loadAllScheduleForWeek(ws4)).thenThrow(SQLException.class);
    }
    
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

    private void createRadioPrograms(String name) throws NotFoundException, SQLException{
        RadioProgram rp = new RadioProgram();
        rp.setName(name);
        this.radioPrograms.put(name, rp);
        when(rpDao.getObject(name)).thenReturn(rp);            
    }
    
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
    
    private void setUpDaoForProgramSlot(ProgramSlot ps) throws NotFoundException, SQLException {
        Mockito.doAnswer(createPS).when(spDao).create(ps);
        when(this.spDao.getProgramSlot(ps.getStartTime())).thenReturn(ps);
    }
    
    private void recordProgramSlot(ProgramSlot ps) throws NotFoundException, SQLException {
        this.programSlots.put(ps.getID(), ps);
    }
    
    @After
    public void tearDown() {
    }
    
    //@Test(expected = SQLException.class)

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
            recordProgramSlot(ps);
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
            recordProgramSlot(ps);
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
            recordProgramSlot(ps);
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
            recordProgramSlot(ps);
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
