package org.luvx.mail.jdk;

import org.luvx.mail.utils.MailUtils;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

/**
 * @author: Ren, Xie
 * @desc:
 */
public class Send0 {
    // private static final String sender   = "foobar@hotmail.com";
    // private static final String receiver = "foobar@hotmail.com";

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        Send0.createMimeMessage(Session.getInstance(props), MailUtils.sender, MailUtils.receiver);
    }

    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        MimeMessage mail = new MimeMessage(session);

        /*
         * 也可以根据已有的eml邮件文件创建 MimeMessage 对象
         * MimeMessage mail = new MimeMessage(session, new FileInputStream("MyEmail.eml"));
         */

        // 2. From: 发件人
        //    其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        mail.setFrom(new InternetAddress(MailUtils.sender, "USER_AA", "UTF-8"));

        // 3. To: 收件人
        mail.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(MailUtils.receiver, "USER_CC", "UTF-8"));
        mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(MailUtils.receiver, "USER_DD", "UTF-8"));
        //    Cc: 抄送（可选）
        mail.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(MailUtils.receiver, "USER_EE", "UTF-8"));
        //    Bcc: 密送（可选）
        mail.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(MailUtils.receiver, "USER_FF", "UTF-8"));

        // 4. Subject: 邮件主题
        mail.setSubject("TEST邮件主题", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        mail.setContent("TEST这是邮件正文。。。", "text/html;charset=UTF-8");

        // 6. 设置显示的发件时间
        mail.setSentDate(new Date());

        // 7. 保存前面的设置
        mail.saveChanges();

        // 8. 将该邮件保存到本地
        OutputStream out = new FileOutputStream("D:\\code\\OneDrive\\Code\\luvx_trial\\app-notify\\src\\main\\java\\org\\luvx\\mail\\MyEmail.eml");
        mail.writeTo(out);
        out.flush();
        out.close();

        return mail;
    }
}
