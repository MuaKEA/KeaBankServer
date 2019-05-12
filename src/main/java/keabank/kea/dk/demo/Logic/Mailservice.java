package keabank.kea.dk.demo.Logic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Mailservice {
    @Autowired
    private JavaMailSender javaMailSender;


    public Mailservice(JavaMailSender javaMailSender){
        this.javaMailSender= javaMailSender;

    }


public void sendemail(String email,String message) throws MailException {
Thread t= new Thread(()-> {
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setTo(email);
    mail.setFrom("altair2400@gmail.com");
    mail.setSubject("From Kea Bank");
    mail.setText(message);
    javaMailSender.send(mail);

});
t.start();
}

}
