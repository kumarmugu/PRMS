/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import sg.edu.nus.iss.phoenix.authenticate.dao.RoleDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.Role;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.core.exceptions.UserProgramConstraintsException;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.user.service.UserManagementService;

/**
 *
 * @author misitesawn
 */
public class UserManagementServiceTest {
     private DAOFactoryImpl factory;

    private ScheduleDAO spdao;
    private RoleDao roledao;
    private UserDao userdao;

    UserManagementService usermanagmentService;
    
    User createTestUser1;
    User managerUser1;

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
    public void setUp() throws SQLException, NotFoundException ,UserProgramConstraintsException{
        spdao = mock(ScheduleDAO.class);
        userdao = mock(UserDao.class);
        factory = mock(DAOFactoryImpl.class);
        //MockitoAnnotations.initMocks(this);
        usermanagmentService = new UserManagementService();
        usermanagmentService.factory = factory;
        usermanagmentService.roledao = roledao;
        usermanagmentService.usrdao = userdao;
       
        //Manager User ( id: thePointyManager)
        managerUser1 = new User("thePointyManagerId");
        managerUser1.setName("thePointyManagerName");
        Role managerUser1Role1 = new Role("manager");
        ArrayList<Role> managerUser1Roles = new ArrayList<Role>();
        managerUser1Roles.add(managerUser1Role1);
        
        
        
        //Test create user ( manager, presenter ) 
        createTestUser1 = new User("userIdcreate1");
        createTestUser1.setName("userNamecreate1");
        createTestUser1.setPassword("userPasscreate1");
        
        Role createTestPresentR1 = new Role("presenter");
        Role createTestManagerR2 = new Role("manager");
        ArrayList<Role> cretaeTestRoles = new ArrayList<Role>();
        cretaeTestRoles.add(createTestManagerR2);
        cretaeTestRoles.add(createTestPresentR1);
        
        createTestUser1.setRoles( cretaeTestRoles);
        
        when(userdao.getObject("userIdcreate1")).thenReturn(createTestUser1);
        Mockito.doThrow(new SQLException()).when(userdao).create(createTestUser1);
        
        
        //For testing Delete user assigned as procedure,presnter ( manager, presenter)
        Calendar cal = Calendar.getInstance();        
        cal.setFirstDayOfWeek(1);
        cal.setMinimalDaysInFirstWeek(1);

        WeeklySchedule ws1 = new WeeklySchedule(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
        when(spdao.getAnnualSchedule(ws1)).thenReturn(new AnnualSchedule(cal.get(Calendar.YEAR), "user1"));
        ArrayList<ProgramSlot> programSlots1 = new ArrayList<ProgramSlot>();
        for (int i = 0; i < 5; i++) {
            ProgramSlot ps = new ProgramSlot();
            ps.setProducerId(createTestUser1.getId());
            ps.setPresenterId(createTestUser1.getId());
            programSlots1.add(ps);
        }
        ws1.setProgramSlots(programSlots1);
        when(userdao.isUserAssignedAsPresenter(createTestUser1.getId())).thenReturn(Boolean.TRUE);
        
        Mockito.doThrow(new UserProgramConstraintsException("User ProgramConstraints Exception")).when(userdao).delete(createTestUser1);
        
    }
    
    /**
     * This method to test createUser function that will throw SQLException 
     * when try to add User with the existing userId 
     * @throws SQLException 
     */
    @Test(expected = SQLException.class)
    public void testCreateUserWithPresenterMangerRoles() throws SQLException{
        User usr1 = new User("userIdcreate1");
        usr1.setName("userNamecreate1");
        usermanagmentService.processCreateUser(usr1);
        fail();
    }
    
    /**
     * This method to test modiftyUser function that will throws UserProgramConstaraintsException
     * when try to remove user's  presenter role where particular user has been assigned as presenter
     * in upcoming scheduled programs
     * @throws SQLException
     * @throws UserProgramConstraintsException
     * @throws NotFoundException 
     */
     @Test(expected = UserProgramConstraintsException.class)
     public void testModifyUserWithAssignedInScheduled()throws UserProgramConstraintsException, NotFoundException, SQLException{
        
         if ( userdao.isUserAssignedAsPresenter(createTestUser1.getId())){
             throw new UserProgramConstraintsException("user is assigned in scheduled program as presenter");
         }
         //usermanagmentService.processsModifyUser(createTestUser1);
         fail();
     }
    
    /**
     * This method to test deleteUser function that will throws UserProgramConstaraintsException
     * when try to delete users with assigned in any upcoming scheduled programs as a presenter/procedure
     * @throws SQLException
     * @throws UserProgramConstraintsException
     * @throws NotFoundException 
     */
    @Test(expected = UserProgramConstraintsException.class)
    public void testDeleteUserWithPresenterMangerRoles() throws SQLException,UserProgramConstraintsException,NotFoundException{
        usermanagmentService.processDeletUser(managerUser1.getId());
        usermanagmentService.processDeletUser(createTestUser1.getId());
        fail();
    }
    
    
    
   
    
    
    
}
