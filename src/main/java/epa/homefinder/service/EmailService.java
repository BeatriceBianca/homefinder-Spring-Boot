package epa.homefinder.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
    private MailContentBuilder mailContentBuilder;

//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

    public void sendEmail(List<String> receivers, String subject,  String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        if (receivers.size() > 0) {
            mimeMessageHelper.setTo(receivers.get(0));
            mimeMessageHelper.setSubject(subject);
            if (receivers.size() > 1) {
                receivers.remove(0);
                mimeMessageHelper.setBcc(receivers.toArray(new String[0]));
            }
            String content = mailContentBuilder.build(text);
            mimeMessageHelper.setText(content, true);
//            mailSender.send(mimeMessage);
        }
    }
}
