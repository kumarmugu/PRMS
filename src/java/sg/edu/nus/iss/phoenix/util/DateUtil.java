/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Mugunthan
 */
public class DateUtil {

    public static Date getStartDateOfWeek(String year, String week) {
        Date date = null;
        int y;
        int w;
        Calendar calendar = Calendar.getInstance();
        if (year != null && year.length() == 4 && year.matches("^-?\\d+$") && week != null && week.matches("^-?\\d+$")) {
            y = Integer.parseInt(year);
            w = Integer.parseInt(week);
        } else {
            y = calendar.get(Calendar.YEAR);
            w = calendar.get(Calendar.WEEK_OF_YEAR);
        }
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, w);
        calendar.set(Calendar.YEAR, y);
        // Now get the first day of week.
        date = calendar.getTime();
        return date;
    }
}
