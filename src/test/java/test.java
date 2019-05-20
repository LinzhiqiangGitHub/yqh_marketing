import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.util.RandomUtils;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class test {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date create_time_one = new Date();
        String create_time = dateFormat.format(create_time_one);
        Date update_time_one = new Date();
        String update_time = dateFormat.format(update_time_one);
        System.out.println(create_time);
        System.out.println(update_time);

        String s = RandomUtils.generateStrId();
        System.out.println(s);

        int i = 111111;
        if (i == 0) {
            System.out.println("------------------");
            System.out.println(i);
        }
        if (i == 1) {
            System.out.println("===================");
            System.out.println(i);
        }

        System.out.println("111");


        final long l = DateUtils.nowMillis();
        System.out.println(l);

       /* Date date = new Date();
        String dateString = date.toString();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date parse = dateFormat1.parse(dateString);
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
        String format = dateformat.format(new Date());
        System.out.println("今天  :" + format);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date d1 = new Date(c.getTimeInMillis());
        System.out.println("星期一:" + dateformat.format(d1));


        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date d2 = new Date(c.getTimeInMillis());
        System.out.println("星期日:" + dateformat.format(d2));

        SimpleDateFormat formatout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = formatout.format(new Date());
        Date parse = formatout.parse(format1);
        System.out.println(parse);

        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(date);
        Date parse1 = formatout.parse(date);
        System.out.println(parse1);

        final String s1 = String.valueOf(RandomUtils.generateId());
        System.out.println(s1);
        System.out.println(s1.length());

        final String registerTypePrefix = LoginUserConstant.ShortMessagePrefix.REGISTER_TYPE_PREFIX;
        System.out.println(registerTypePrefix);
    }


}