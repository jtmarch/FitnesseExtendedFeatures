package com.ilionx.nl.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;

/**
 * Javamail mail reader fixture.
 */
public class MailHandlerFixture {

    private static final String MAIL_STORE_PROTOCOL = "mail.store.protocol";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_IMAP_PORT = "mail.imap.port";
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

            // Now set the actual message
            message.setText(content);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
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
    public boolean mailReceivedWhichContainsFromOnDate(final String expression, final String sender, final String date) throws ParseException {
        this.mailWrapper = readMail(expression);
        if (this.mailWrapper == null) {
            throw new AssertionError("No email found with expression: " + expression);
        }
        if (!sender.equalsIgnoreCase(this.mailWrapper.getProperty("from"))) {
            throw new AssertionError("Email has different sender: " + this.mailWrapper.getProperty("from") + " vs " + sender);
        }
        if (!date.equalsIgnoreCase(this.mailWrapper.getProperty("date"))) {
            throw new AssertionError("Email has different date: " + this.mailWrapper.getProperty("date") + " vs " + date);
        }
        return true;
    }

    /**
     * | $result= | get external link from mail with label | <i>label</i> |
     * @param label label van de externe link.
     * @return external link or <b>null</b> otherwise
     */
    public String getExternalLinkFromMailWithLabel(final String label){
        Map<String, String> externalLinks = this.mailWrapper.getExternalLinks();
        for(String key : externalLinks.keySet()){
            if(key.contains(label)){
                return externalLinks.get(key);
            }
        }

        throw new AssertionError("No link found in e-mail with label: " + label + " within the content: " + this.mailWrapper.getProperty("content"));
    }

    public boolean mailContainsFieldWithValue(final String field, final String expectedValue){
        if(this.mailWrapper == null){
            throw new AssertionError("First receive a mail before checking its values");
        }

        String val = this.mailWrapper.getProperty(field);
        if(val != null && val.contains(expectedValue)){
            return true;
        }

        throw new AssertionError("Value: " + val + " and expected value: " + expectedValue + " do not match.");
    }

    private MailWrapper readMail(final String expression) {
        if (protocol == null) {
            throw new AssertionError("Protocol must be set");
        }

        try {
            int attempts = 5;
            while (attempts != 0) {
                Store store = connectToMailServer();
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                for (int y = inbox.getMessageCount(); y > 0; y--) {
                    Message msg = inbox.getMessage(y);
                    String body = getBody(msg);

                    if (body.contains(expression)) {
                        return getMailWrapper(msg, body);
                    }
                }
                attempts--;
                Thread.sleep(1000);
            }
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        return null;
    }

    private String getBody(Message msg) throws IOException, MessagingException {
        Object messageContent = msg.getContent();
        if (messageContent instanceof Multipart) {
            Multipart mp = (Multipart) messageContent;
            StringBuffer body = new StringBuffer();
            for (int x = 0; x < mp.getCount(); x++) {
                body.append(mp.getBodyPart(x).getContent().toString());
            }
            return body.toString();
        }
        if (messageContent instanceof String) {
            return (String) messageContent;
        }
        throw new AssertionError("Unsupported message content found: " + messageContent.getClass());
    }

    private Store connectToMailServer() throws MessagingException {
        Properties props = new Properties();
        props.setProperty(MAIL_STORE_PROTOCOL, protocol);
        props.setProperty(MAIL_IMAP_PORT, receivingPort);
        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect(host, user, password);
        return store;
    }

    private MailWrapper getMailWrapper(final Message msg, final String body) throws MessagingException {
        MailWrapper mw = new MailWrapper();
        mw.setDate(msg.getSentDate());
        mw.setSubject(msg.getSubject());
        mw.setContent(body);

        mw.setFrom(getAddressesAsString(msg.getFrom()));
        mw.setTo(getAddressesAsString(msg.getRecipients(Message.RecipientType.TO)));
        mw.setReplyTo(getAddressesAsString(msg.getReplyTo()));
        mw.setBcc(getAddressesAsString(msg.getRecipients(Message.RecipientType.BCC)));
        mw.setCc(getAddressesAsString(msg.getRecipients(Message.RecipientType.CC)));
        return mw;
    }

    private String getAddressesAsString(Address[] addresses) throws MessagingException {
        if(addresses == null){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(int x=0;x<addresses.length;x++){
            if(stringBuffer.length() > 0){
                stringBuffer.append(",");
            }
            stringBuffer.append(((InternetAddress) addresses[x]).getAddress());
        }
        return stringBuffer.toString();
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
