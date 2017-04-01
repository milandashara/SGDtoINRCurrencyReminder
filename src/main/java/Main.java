
import com.ritaja.xchangerate.api.CurrencyConverter;
import com.ritaja.xchangerate.api.CurrencyConverterBuilder;
import com.ritaja.xchangerate.util.Currency;
import com.ritaja.xchangerate.util.Strategy;


import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by milanashara on 3/26/17.
 */
public class Main {

    static int maxValue = 0;


    public static void main(String arg[]) throws Exception{

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {


            public void run() {
                try {
                    myjob();
                }catch (Exception e){
                 e.printStackTrace();
                }

            }
        }, 0, 1, TimeUnit.HOURS);


    }

    public static void myjob() throws Exception{
        // create the appropriate servive object
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.YAHOO_FINANCE_FILESTORE)
                .buildConverter();
        converter.setRefreshRateSeconds(0);
        BigDecimal value = converter.convertCurrency(new BigDecimal("100"), Currency.SGD, Currency.INR);
        System.out.println(value);


        if (value.intValue() > maxValue){
            //send mail
            // Recipient's email ID needs to be mentioned.
            String to = "xx@gmail.com";//change accordingly

            // Sender's email ID needs to be mentioned
            String from = "xx@gmail.com";//change accordingly
            final String username = "xxx@gmail.com";//change accordingly
            final String password = "XXX";//change accordingly

            // Assuming you are sending email through relay.jangosmtp.net
            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {
                // Create a default MimeMessage object.
                javax.mail.Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.setRecipients(javax.mail.Message.RecipientType.TO,
                        InternetAddress.parse(to));

                // Set Subject: header field
                message.setSubject("SGD to INR" + value);

                // Now set the actual message
                message.setText("SGD to INR" + value);

                // Send message
                Transport.send(message);

                System.out.println("Sent message successfully...." + value);

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }


        }

        if (value.intValue()>maxValue){
            maxValue=value.intValue();
        }
    }

}
