package com.springboot.springboot.utils;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nowcoder on 2016/7/15. // course@nowcoder.com NKnk66
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;    //FreeMarker的技术类
    //private FreeMarkerConfigurer freeMarkerConfigurer=null;    //FreeMarker的技术类

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    public boolean sendWithHTMLTemplate(String to, String username,String subject) {
        try {
            String nick = MimeUtility.encodeText("Wilson");         //发件人的昵称
            InternetAddress from = new InternetAddress(nick + "<zysong0709@foxmail.com>");  //发件人是谁
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            String result =getMailText(username);// 使用模板生成html邮件内容
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    //通过模板构造邮件内容，参数username将替换模板文件中的${username}标签。
    private String getMailText(String username){
        String htmlText="";
        try {
            //通过指定模板名获取FreeMarker模板实例
            Template tpl=freeMarkerConfigurer.getConfiguration().getTemplate("mailMessage.html");
            //FreeMarker通过Map传递动态数据
            Map map=new HashMap();
            map.put("username",username); //注意动态数据的key和模板标签中指定的属性相匹配
            //解析模板并替换动态数据，最终username将替换模板文件中的${username}标签。
            htmlText= FreeMarkerTemplateUtils.processTemplateIntoString(tpl,map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return htmlText;
    }

    //邮箱的基础设置
    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("zysong0709@foxmail.com");
        mailSender.setPassword("wrzizdhsoszjdhec");                //这个是你打开pop3/smtp服务时的给的那个授权码，不是邮箱的密码 一段时间之后这个授权码会过期，然后就又会显示发送邮件失败，改成新的
        mailSender.setHost("smtp.qq.com");
        //mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
