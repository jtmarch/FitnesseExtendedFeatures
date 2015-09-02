package com.ilionx.nl.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jdebruin on 6/10/15.
 */
@RunWith(JUnit4.class)
public class MailHandlerFixtureTest {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    private GreenMail greenMail;

    private MailHandlerFixture mailHandlerFixture = new MailHandlerFixture();

    @Before
    public void setUp() throws Exception {
        //Start all email servers using non-default ports.
        greenMail = new GreenMail(ServerSetupTest.ALL);
        greenMail.start();
    }

    @After
    public void tearDown() throws Exception {
        greenMail.stop();
    }

    @Ignore
    @Test
    public void testRealMailProvider() throws ParseException, MessagingException {
        String date = new Date().toString();

        mailHandlerFixture.setMailProviderWithProtocolHostUserAndPasswordAndHostForSending("imaps", "imap.one.com:993", "ilionx@4family.nl", "wachtwoord", "send.one.com");
        mailHandlerFixture.sendMailToFromWithSubjectAndContent("ilionx@4family.nl", "ilionx@4family.nl", "Test mail", "Body with date " + date + " and something");
        Assert.assertTrue(mailHandlerFixture.mailReceivedWhichContainsFromOnDate(date, "jelle@4family.nl", ""));
    }

    //@Test
    public void testExternalLinksMailProvider() throws ParseException, MessagingException {
        mailHandlerFixture.setMailProviderWithProtocolHostUserAndPasswordAndHostForSending("imaps", "imap.one.com:993", "ilionx@4family.nl", "wachtwoord", "send.one.com");
        Assert.assertTrue(mailHandlerFixture.mailReceivedWhichContainsFromOnDate("bestelformulier", "jdebruin@ilionx.com", "15-06-2015"));
        Assert.assertEquals("http://dominotst02.ilionx.com/rollade.nsf", mailHandlerFixture.getExternalLinkFromMailWithLabel("bestel"));
    }

    @Test
    public void testGreenMailProvider() throws ParseException, MessagingException {
        String date = formatter.format(new Date());

        mailHandlerFixture.setMailProviderWithProtocolHostUserAndPasswordAndHostForSending("imap", "localhost:3143", "to@localhost.com", "to@localhost.com", "localhost:3025");
        int x;
        for(x=0;x<5;x++){
            mailHandlerFixture.sendMailToFromWithSubjectAndContent("to@localhost.com", "from"+x+"@localhost.com", "Test mail"+x, "Body with date "+ x +" and something " + x);
        }
        mailHandlerFixture.sendMailToFromWithSubjectAndContent("to@localhost.com", "from@localhost.com", "Test mail", "Body with date " + date + " and something ");
        System.out.println("Done inserting mails");
        Assert.assertTrue(mailHandlerFixture.mailReceivedWhichContainsFromOnDate(date, "from@localhost.com", date));
    }

    @Test
    public void testGreenMailProviderNoResult() throws ParseException, MessagingException {
        String date = formatter.format(new Date());

        mailHandlerFixture.setMailProviderWithProtocolHostUserAndPasswordAndHostForSending("imap", "localhost:3143", "to@localhost.com", "to@localhost.com", "localhost:3025");

        for(int x=0;x<5;x++){
            mailHandlerFixture.sendMailToFromWithSubjectAndContent("to@localhost.com", "from"+x+"@localhost.com", "Test mail"+x, "Body with date " + date + " and something " + x);
        }
        System.out.println("Done inserting mails");
        Assert.assertFalse(mailHandlerFixture.mailReceivedWhichContainsFromOnDate(date, "from@localhost.com", date));
    }

}
