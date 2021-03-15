package ng.com.obkm.myvooz.utils.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateWithTimeZone {
    public static Calendar stringToCalendar(String strDate, TimeZone timezone) throws ParseException {
        String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        sdf.setTimeZone(timezone);
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
