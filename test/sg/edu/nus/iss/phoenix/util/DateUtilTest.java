/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.validation.constraints.AssertTrue;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Phyu Me Zaw
 */
public class DateUtilTest {
 
    /**
     * @author Phyu Me Zaw
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
     
        Date expectedSDate =  sdf.parse("2016-10-02");
        Date startDate = DateUtil.getStartDateOfWeek("2016", "41");
        Assert.assertEquals(expectedSDate, startDate);

    }
    
    
    
     /**
     * 
     * @author Phyu Me Zaw
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
       
        Date expectedSDate =  sdf.parse("2016-10-02");
        Date startDate = DateUtil.getStartDateOfWeek(2016, 41);
        Assert.assertEquals(expectedSDate, startDate);
 
    }
    
     /**
     * @author Phyu Me Zaw
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
     *@author Phyu Me Zaw
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
     * @author Phyu Me Zaw
     * This is the
     * testgetDateDiffwithInValidParam Return datetime difference test case
     * method for getDateDiff method in
     * 2nd Oct 2016 This method  return value expect
     * @param startdatetime 2016-10-04 18:30:00
     * @param enddatetime 2016-10-04 18:30:00  
     * @return Invalid Param -- 
     *
     */
     @Test
    public void testgetDateDiffwithInValidParam() throws ParseException {
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
            
       Date startdatetime =  sdf.parse("2016-10-04 19:30:00");
       Date enddatetime =  sdf.parse("2016-10-04 19:30:00");        
       Date resultDate = DateUtil.getDateDiff(startdatetime,enddatetime);      
       Assert.assertTrue("Invalid Param",resultDate.getTime()<0);
      
       
 
    }
    
     /**
     * @author Phyu Me Zaw
     * This is the
     * testAddDateTime Return addition of Date value and Time Value test case
     * method for AddDateTime, getDate and getTime in
     * 2016-10-04 18:30:00 This method  return value expect
     * @param date 2016-10-04
     * @param time  18:30:00  
     * @return 2016-10-04 18:30:00 -- 
     *
     */
     @Test
    public void testAddDateTime() throws ParseException {
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
            
       Date expectedDatetime =  sdf.parse("2016-10-04 18:30:00");     
       String paramDate = "2016-10-04";
       String time ="18:30";
       
       Date startDate = DateUtil.getDate(paramDate, "yyyy-MM-dd");
       Date startTime = DateUtil.getTime(time, "HH:mm");
       Date resultDateTime = DateUtil.AddDateTime(startDate, startTime);
        
        Assert.assertEquals(expectedDatetime, resultDateTime);
       
 
    }

      /**
     * 
     * @author Phyu Me Zaw
     * This is the
     * testgetgetDayOfWeek Return Day of Week Value test case
     * method for getgetDayOfWeek method in
     * Tuesday This method  return value expect
     * @param Date 2016-10-04     
     * @return Tuesday
     *
     */
    @Test 
    public void testgetgetDayOfWeek() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
       
        
        Date paramDate =  sdf.parse("2016-10-04");
        String exectedDay="Tuesday";
        String resultedDay = DateUtil.getDayOfWeek(paramDate);
        Assert.assertEquals(exectedDay, resultedDay);
 
    }
   
      /**
     * 
     * @author Phyu Me Zaw
     * This is the
     * testgetWeekOfYear Return Week of Year Value test case
     * method for getWeekOfYear method in
     * 41 This method  return value expect
     * @param Date 2016-10-04     
     * @return Week 41
     *
     */
    @Test 
    public void testgetWeekOfYear() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
       
        
        Date paramDate =  sdf.parse("2016-10-04");
        int exectedWeek=41;
        int resultedWeek = DateUtil.getWeekOfYear(paramDate);
        Assert.assertEquals(exectedWeek, resultedWeek);
 
    }
    
     /**
     * 
     * @author Phyu Me Zaw
     * This is the
     * testgetYear Return Year of Date Value test case
     * method for getYear method in
     * 41 This method  return value expect
     * @param Date 2016-10-04     
     * @return Year 2016
     *
     */
    @Test 
    public void testgetYear() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
       
        
        Date paramDate =  sdf.parse("2016-10-04");
        int exectedYear=2016;
        int resultedYear = DateUtil.getYear(paramDate);
        Assert.assertEquals(exectedYear, resultedYear);
 
    }
    
     
}
