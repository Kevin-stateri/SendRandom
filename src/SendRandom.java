import java.io.*;
import java.util.Properties;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.smtp.*;

public class SendRandom {

    public static void main(String[] args) throws InterruptedException, MessagingException {
        File source = new File("source.txt");

        String Email = args[0];
        String Password = args[1];
        String Receiver = args[2];

        int numLines = NumberLines(source);

        String Line = "";

        Random rand = new Random(System.currentTimeMillis());

        do {
            Line = GetLine(source, rand.nextInt(numLines) + 1);
            System.out.println(Line);
            Send(Line, Email, Password, Receiver);
            TimeUnit.SECONDS.sleep(600);
        } while (true);
    }

    public static int NumberLines(File source) {

        int numLines = 0;

        try {
            LineNumberReader numReader = new LineNumberReader(new FileReader(source));
            String curLine;
            do {
                curLine = numReader.readLine();
            } while (curLine != null);
            numLines = numReader.getLineNumber();
            numReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return numLines;
    }

    public static String GetLine(File source, long num){

        String Line = "";

        try {
            LineNumberReader Reader = new LineNumberReader(new FileReader(source));
            String curLine = "";
            while (Reader.getLineNumber() != num) {
                curLine = Reader.readLine();
            }
            Line = curLine;
            Reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Line;

    }

    public static void Send(String Line, String Email, String Password, String Receiver) throws MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Email, Password);
                    }
                });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(Email));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(Receiver));
        msg.setSubject("A random line from a text file!");
        msg.setText(Line);
        msg.setSentDate(new Date());

        Transport.send(msg);

    }

}
