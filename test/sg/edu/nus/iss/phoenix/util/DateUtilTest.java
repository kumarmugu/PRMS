/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Mugunthan
 */
public class DateUtilTest {
 
    /**
     * This is the
     * testgetStartDateOfWeekwithStringParam Return Start Date of Week Value test case
     * method for getStartDateOfWeek method in
     * 2nd Oct 2016 This method  return value expect
     * @param year 2016
     * @param week 41
     * @return 2nd Oct 2016
     *
     */
    @Test 
    public void testgetStartDateOfWeekwithStringParam() throws ParseException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     
     
        Date expectedSDate =  sdf.parse(sdf.format(new Date()));
        Date startDate = DateUtil.getStartDateOfWeek("2016", "41");
        Assert.assertEquals(expectedSDate, startDate);

    }
    
    
    
     /**
     * This is the
     * testgetStartDateOfWeekwithIntegerParam Return Start Date of Week Value test case
     * method for getStartDateOfWeek method in
     * 2nd Oct 2016 This method  return value expect
     * @param year 2016
     * @param week 41
     * @return 2nd Oct 2016
     *
     */
    @Test 
    public void testgetStartDateOfWeekwithIntegerParam() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
       
        Date expectedSDate =  sdf.parse(sdf.format(new Date()));
        Date startDate = DateUtil.getStartDateOfWeek(2016, 41);
        Assert.assertEquals(expectedSDate, startDate);
 
    }
    
     /**
     * This is the
     * testgetStartDateOfWeekwithInvalidIntegerParam Return AssertionError test case
     * method for getStartDateOfWeek method in
     * 2nd Oct 2016 This method  return value expect
     * @param year 0
     * @param week 41
     * @return AssertionError
     *
     */
     @Test(expected = AssertionError.class)
    public void testgetStartDateOfWeekwithInvalidIntegerParam() throws ParseException {
           
       java.lang.AssertionError expectedErr = new AssertionError();
        //Date expectedSDate = null;
       
        Date startDate = DateUtil.getStartDateOfWeek(0, 41);
        Assert.assertEquals(expectedErr, startDate);
 
    }
   
   
   
    /**
     * This is the
     * testgetStartDateOfWeekwithInvalidIntegerParam Return AssertionError test case
     * method for getStartDateOfWeek method in
     * 2nd Oct 2016 This method  return value expect
     * @param year 0
     * @param week 41
     * @return 2nd Oct 2016
     *
     */
     @Test(expected = AssertionError.class)
    public void testgetStartDateOfWeekwithInvalidStringParam() throws ParseException {
           
       java.lang.AssertionError expectedErr = new AssertionError();
        String year = null;
       
        Date startDate = DateUtil.getStartDateOfWeek(year, "41");
        Assert.assertEquals(expectedErr, startDate);
 
    }
    
    /**
     * This is the
     * testgetDateDiffwithInvalidParam Return AssertionError test case
     * method for getStartDateOfWeek method in
     * 2nd Oct 2016 This method  return value expect
     * @param year 0    
     * @return AssertionError
     *
     */
     @Test(expected = AssertionError.class)
    public void testgetDateDiffwithInvalidParam() throws ParseException {
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
       
        Date startdatetime =  sdf.parse(sdf.format("2016-10-04 18:30:00"));
        Date enddatetime =  sdf.parse(sdf.format("2016-10-04 19:30:00"));
        Date expectedSDate =  sdf.parse(sdf.format("2016-10-04 01:00:00"));
        
       java.lang.AssertionError expectedErr = new AssertionError();      
       Date resultDate = DateUtil.getDateDiff(enddatetime,startdatetime);
       Assert.assertEquals(expectedSDate, resultDate);
 
    }
    
     
}
