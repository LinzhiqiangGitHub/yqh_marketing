package com.yqh.marketing.basedevss.base.mail;

import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.util.email.MailSenderInfo;
import com.yqh.marketing.basedevss.base.util.email.SimpleMailSender;

import java.io.File;
import java.util.List;

public class sendMail {

    public static void sendEmailAddAttach(List<String> receivers,String title,String content,File attachFile,String aileName){
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.qqyqh.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName(GlobalConfig.get().getString("deliver.maill.username", "chenguo@qqyqh.com"));
        mailInfo.setPassword(GlobalConfig.get().getString("deliver.maill.password", "Chenarne1919"));//您的邮箱密码
        mailInfo.setFromAddress(GlobalConfig.get().getString("deliver.maill.username", "chenguo@qqyqh.com"));
        mailInfo.setToAddress(receivers);
        mailInfo.setSubject(title);
        mailInfo.setContent(content);
        if (attachFile != null){
            mailInfo.setAttachFile(attachFile);
            String[] fileNames = {aileName};
            mailInfo.setAttachFileNames(fileNames);
        }
        //这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendHtmlMailAddAttach(mailInfo);//发送html格式
    }
}