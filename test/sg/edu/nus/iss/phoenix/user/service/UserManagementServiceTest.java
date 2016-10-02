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
       
        //Test create user ( manager, presenter ) 
        User createTestUser1 = new User("userIdcreate1");
        createTestUser1.setName("userNamecreate1");
        createTestUser1.setPassword("userPasscreate1");
        
        Role createTestPresentR1 = new Role("presenter");
        Role createTestManagerR2 = new Role("manager");
        ArrayList<Role> cretaeTestRoles = new ArrayList<Role>();
        cretaeTestRoles.add(createTestManagerR2);
        cretaeTestRoles.add(createTestPresentR1);
        
        createTestUser1.setRoles( cretaeTestRoles);
        
        when(userdao.getObject("userIdcreate1")).thenReturn(createTestUser1);
        Mockito.doThrow(new SQLException()).when(userdao).create(new User("createTestUser1"));
        Mockito.doThrow(new UserProgramConstraintsException("User ProgramConstraints Exception")).when(userdao).delete(new User("createTestUser1"));
        
    }
    
    @Test(expected = SQLException.class)
    public void testCreateUserWithPresenterMangerRoles() throws SQLException{
        usermanagmentService.processCreateUser(new User("createTestUser1"));
        fail();
    }
    
    @Test(expected = UserProgramConstraintsException.class)
    public void testDeleteUserWithPresenterMangerRoles() throws SQLException,UserProgramConstraintsException,NotFoundException{
        usermanagmentService.processDeletUser("createTestUser1");
        fail();
    }
}
