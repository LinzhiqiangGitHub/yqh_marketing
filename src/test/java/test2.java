import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

public class test2 {
    public static int time = 3 * 60;

    public static void main(String[] args) throws IOException {

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 3);
        Date time = calendar.getTime();
        System.out.println(date);
        System.out.println(calendar.getTime());
        long timesDis = Math.abs(date.getTime());
        long abs = Math.abs(time.getTime());
        System.out.println(abs - timesDis);



        String encode = URLEncoder.encode("验证码：","UTF-8");
        System.out.println(encode);

        String end = "您的验证码：" + "123455" + "，有效时间为15分钟。";
        String encode1 = URLEncoder.encode(end, "utf-8");
        System.out.println(encode1);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://wechat.qqyqh.com/api/sms/send_sms");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        System.out.println(httpGet);
    }

    //过期时间
    public static void out_time() {
        while (time > 0) {
            time--;
            try {
                Thread.sleep(1000);
                int mm = time / 60  % 60;
                int ss = time % 60;
                System.out.println("还剩"  + mm + "分钟" + ss + "秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
