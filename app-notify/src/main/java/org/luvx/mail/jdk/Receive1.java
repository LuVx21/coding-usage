package org.luvx.mail.jdk;

import org.luvx.mail.utils.MailUtils;

import javax.mail.*;

/**
 * @author: Ren, Xie
 * @desc: 微软邮箱:
 * SMTP: smtp.office365.com 587 STARTTLS
 * 读取邮件
 */
public class Receive0 {

    public static void main(String[] args) throws Exception {
        Session session = Session.getDefaultInstance(MailUtils.imap_config(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailUtils.sender, MailUtils.password);
            }
        });

        /// Session session = Session.getInstance(MailUtils.pop3_config());

        try (Store store = session.getStore()) {
            store.connect(MailUtils.POP3_SERVER, MailUtils.sender, MailUtils.password);
            try (Folder folder = store.getFolder("INBOX")) {
                folder.open(Folder.READ_ONLY);
                // Message[] messages = folder.getMessages();
                int cnt = folder.getMessageCount();
                Message[] messages = folder.getMessages(cnt - 9, cnt);
                print(messages);
            }
        }
    }

    private static void print(Message[] messages) throws Exception {
        System.out.println("The count of the Email is :" + messages.length);
        for (int i = messages.length - 1; i >= 0; i--) {
            Message message = messages[i];
            System.out.println(
                    "--------------------" + (i + 1) + "-------------------"
                            + "\nSubject: " + message.getSubject()
                            + "\nFrom: " + message.getFrom()[0]
                            + "\nText: " + message.getContent().toString()
            );
        }
    }
}
