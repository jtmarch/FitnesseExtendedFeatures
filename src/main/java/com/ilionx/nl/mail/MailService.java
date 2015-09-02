package com.ilionx.nl.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Properties;

public class MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private static final String MAIL_STORE_PROTOCOL = "mail.store.protocol";
    private static final String MAIL_IMAP_PORT = "mail.imap.port";
    private static final String INBOX = "INBOX";

    public MailWrapper readMail(final Store store, final String expression, final String sender, final String date) {
        Folder inbox = null;
        try {
            MailSearchTerm searchTerm = new MailSearchTerm.Builder().withDate(date).withSender(sender).withBody(expression).build();
            int attempts = 5;

            while (attempts != 0) {
                inbox = store.getFolder(INBOX);
                inbox.open(Folder.READ_ONLY);
                int newMessages = inbox.getNewMessageCount();
                Message[] messages = inbox.getMessages(1, newMessages);
                LOG.debug("Nr of messages found: " + messages.length);
                FetchProfile fp = new FetchProfile();
                fp.add(FetchProfile.Item.ENVELOPE);
                inbox.fetch(messages, fp);

                Message[] messagesFound = inbox.search(searchTerm, messages);
                if (messagesFound.length > 0) {
                    return getMailWrapper(messagesFound[0], getBody(messagesFound[0]));
                }

                attempts--;
                LOG.debug(attempts + " attempts left.");
                Thread.sleep(1000);
            }
            inbox.close(false);
            store.close();
        } catch (Exception mex) {
            LOG.error(mex.getMessage(), mex);
        }
        return null;
    }

    public Store connectToMailServer(String protocol, String receivingPort, String host, String user, String password) throws MessagingException {
        Properties props = new Properties();
        props.setProperty(MAIL_STORE_PROTOCOL, protocol);
        props.setProperty(MAIL_IMAP_PORT, receivingPort);
        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect(host, user, password);
        return store;
    }

    public String getBody(Message msg) throws IOException, MessagingException {
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

    public MailWrapper getMailWrapper(final Message msg, final String body) throws MessagingException {
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

    public String getAddressesAsString(Address[] addresses) throws MessagingException {
        if (addresses == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int x = 0; x < addresses.length; x++) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append(",");
            }
            stringBuffer.append(((InternetAddress) addresses[x]).getAddress());
        }
        return stringBuffer.toString();
    }


}
