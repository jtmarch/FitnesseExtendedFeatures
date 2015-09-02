package com.ilionx.nl.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Javamail mail reader fixture.
 */
public class MailHandlerFixture {

    private static final Logger LOG = LoggerFactory.getLogger(MailHandlerFixture.class);

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_USER = "mail.user";
    private static final String MAIL_PASSWORD = "mail.password";

    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    private String protocol;

    private String host;

    private String user;

    private String password;

    private String hostForSending;

    private String receivingPort = "993";

    private String sendingPort = "25";

    private MailWrapper mailWrapper = null;
    private MailService mailService = new MailService();

    /**
     * <p><code>
     * | set mail provider with protocol | <i>protocol</i> | host | <i>host</i> | user | <i>user</i> | and password | password | and host for sending | <i>hostForSending</i> |
     * <p/>
     * | set mail provider with protocol | imaps | host | imap.one.com | user | ilionx@4family.nl | and password | wachtwoord | and host for sending | smtp.one.com |
     * </code></p>
     *
     * @param protocol
     * @param host
     * @param user
     * @param password
     */
    public boolean setMailProviderWithProtocolHostUserAndPasswordAndHostForSending(final String protocol, final String host, final String user, final String password, final String hostForSending) {
        this.protocol = protocol;

        String[] receivingHost = host.split(":");
        this.host = receivingHost[0];
        if (receivingHost.length > 1) {
            this.receivingPort = receivingHost[1];
        }

        this.user = user;
        this.password = password;

        String[] sendingHost = hostForSending.split(":");
        this.hostForSending = sendingHost[0];
        if (sendingHost.length > 1) {
            this.sendingPort = sendingHost[1];
        }
        return true;
    }

    /**
     * | send mail to | <i>to</i> | from | <i>from</i> | with subject | <i>subject</i> | and content | <i>content</i> |
     *
     * @param to
     * @param from
     * @param subject
     * @param content
     * @return true if succesfull
     */
    public boolean sendMailToFromWithSubjectAndContent(final String to, final String from, final String subject, final String content) {
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty(MAIL_SMTP_HOST, this.hostForSending);
        properties.setProperty(MAIL_SMTP_PORT, sendingPort);
        properties.setProperty(MAIL_USER, this.user);
        properties.setProperty(MAIL_PASSWORD, this.password);
        properties.setProperty(MAIL_SMTP_AUTH, "true");
        // Get the default Session object.
        //Session session = Session.getDefaultInstance(properties);
        Session session = Session.getInstance(properties, new Authenticator(this.user, this.password));
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Set date: header field
            message.setSentDate(new Date());

            // Now set the actual message
            message.setText(content);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            LOG.error(mex.getMessage(), mex);
        }
        return true;
    }

    /**
     * <p><code>
     * | ensure | mail received which contains | <i>expression</i> | from | <i>sender</i> | on date | <i>date</i> |
     * </code></p>
     *
     * @param expression
     * @param sender
     * @param date
     * @return
     */
    public boolean mailReceivedWhichContainsFromOnDate(final String expression, final String sender, final String date) throws ParseException, MessagingException {
        if (protocol == null) {
            throw new AssertionError("Protocol must be set");
        }
        return findMail(expression, sender, date);
    }

    private boolean findMail(String expression, String sender, String date) throws MessagingException {
        long start = new Date().getTime();
        Store store = mailService.connectToMailServer(protocol, receivingPort, host, user, password);
        this.mailWrapper = mailService.readMail(store, expression, sender, date);
        long end = new Date().getTime();

        LOG.debug("Time for reading mail: " + (end - start) + "ms");

        if (this.mailWrapper == null) {
            return false;
        }
        return true;
    }

    /**
     * <p><code>
     * | ensure | no mail received which contains | <i>expression</i> | from | <i>sender</i> | on date | <i>date(dd-MM-yyyy)</i> | within timeout | <i>milliseconds(####)</i> |
     * </code></p>
     *
     * @param expression
     * @param sender
     * @param date
     * @return
     */
    public boolean noMailReceivedWhichContainsFromOnDateWithinSeconds(final String expression, final String sender, final String date, final String seconds) throws ParseException, MessagingException {
        long startTime = System.currentTimeMillis();
        long timeout = Long.parseLong(seconds, 10) * 1000;

        while (false || (System.currentTimeMillis() - startTime) < timeout) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        return findMail(expression, sender, date);
    }

    /**
     * <p><code>
     * | ensure | mail received which contains | <i>expression</i> | from | <i>sender</i> | after timestamp | <i>date+time(dd-MM-yyyy HH:mm:ss)</i> |
     * </code></p>
     *
     * @param expression
     * @param sender
     * @param time
     * @return
     */
    public boolean mailReceivedWhichContainsFromAfterTimestamp(final String expression, final String sender, final String time) throws ParseException, MessagingException {
        boolean result = findMail(expression, sender, null);
        if (result) {
            Date expectedTime = DATE_TIME_FORMAT.parse(time);
            if (!this.mailWrapper.getDateProperty("time").after(expectedTime)) {
                //try one more time
                result = findMail(expression, sender, null);
                if (result && !this.mailWrapper.getDateProperty("time").after(expectedTime)) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * | $result= | get external link from mail with label | <i>label</i> |
     *
     * @param label label van de externe link.
     * @return external link or <b>null</b> otherwise
     */
    public String getExternalLinkFromMailWithLabel(final String label) {
        Map<String, String> externalLinks = this.mailWrapper.getExternalLinks();
        for (String key : externalLinks.keySet()) {
            if (key.contains(label)) {
                return externalLinks.get(key);
            }
        }

        throw new AssertionError("No link found in e-mail with label: " + label + " within the content: " + this.mailWrapper.getProperty("content"));
    }

    /**
     * @param field
     * @param expectedValue
     * @return
     */
    public boolean mailContainsFieldWithValue(final String field, final String expectedValue) {
        if (this.mailWrapper == null) {
            throw new AssertionError("First receive a mail before checking its values");
        }

        String val = this.mailWrapper.getProperty(field);
        if (val != null && val.contains(expectedValue)) {
            return true;
        }

        throw new AssertionError("Value: " + val + " and expected value: " + expectedValue + " do not match.");
    }

    private class Authenticator extends javax.mail.Authenticator {
        private PasswordAuthentication authentication;

        public Authenticator(String user, String pass) {
            authentication = new PasswordAuthentication(user, pass);
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }
}
