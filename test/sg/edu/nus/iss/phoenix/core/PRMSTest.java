/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sg.edu.nus.iss.phoenix.scheduledProgram.service.ReviewAndSelectScheduledProgramServiceTest;
import sg.edu.nus.iss.phoenix.presenterproducer.service.ReviewSelectPresenterProducerServiceTest;
import sg.edu.nus.iss.phoenix.scheduledProgram.service.ScheduledProgramServiceTest;
import sg.edu.nus.iss.phoenix.user.service.UserManagementServiceTest;
import sg.edu.nus.iss.phoenix.util.DateUtilTest;

/**
 *
 * @author Mugunthan
 */
@RunWith(Suite.class)@Suite.SuiteClasses({ReviewAndSelectScheduledProgramServiceTest.class,
					 ReviewSelectPresenterProducerServiceTest.class,
                                         ScheduledProgramServiceTest.class,
                                         UserManagementServiceTest.class,
                                         DateUtilTest.class})
/**
 * This is the test suit which includes all the unit test of PRMS application
 */
public class PRMSTest {

}
