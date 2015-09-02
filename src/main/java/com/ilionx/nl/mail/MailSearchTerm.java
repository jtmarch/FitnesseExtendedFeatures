package com.ilionx.nl.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MailSearchTerm extends SearchTerm {

    private static final Logger LOG = LoggerFactory.getLogger(MailSearchTerm.class);

    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private final List<String> alreadyChecked = new ArrayList<>();

    private final List<Matcher> matchers;

    private int countSkipped = 0;

    public MailSearchTerm(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    public boolean match(final Message message) {

        try {
            String key = getKey(message);
            if (!alreadyChecked.contains(key)) {
                alreadyChecked.add(key);

                for (Matcher matcher : matchers) {
                    boolean match = matcher.match(message);
                    if (!match) {
                        return false;
                    }
                }
            } else {
                countSkipped++;
                return false;
            }
        } catch (MessagingException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return true;
    }

    public static class Builder {

        private List<Matcher> matchers = new ArrayList<>();

        public Builder withDate(String date) {
            if(date != null && !date.isEmpty()){
                this.matchers.add(new DateMatcher(date));
            }
            return this;
        }

        public Builder withSender(String sender) {
            if(sender != null && !sender.isEmpty()){
                this.matchers.add(new FromMatcher(sender));
            }
            return this;
        }

        public Builder withBody(String expression) {
            if(expression != null && !expression.isEmpty()){
                this.matchers.add(new BodyMatcher(expression));
            }
            return this;
        }

        public MailSearchTerm build() {
            return new MailSearchTerm(this.matchers);
        }
    }

    public static interface Matcher {
        boolean match(Message message) throws MessagingException;
    }

    /**
     * Match the received date from a message (dd-Mm-yyyy)
     */
    private static class DateMatcher implements Matcher {

        private final String date;

        public DateMatcher(String date) {
            this.date = date;
        }

        public boolean match(Message message) throws MessagingException {
            LOG.debug("Checking date: " + message.getReceivedDate());
            if (dateFormat.format(message.getReceivedDate()).equals(date)) {
                LOG.info("Found date: " + date);
                return true;
            }
            return false;
        }
    }

    /**
     * Match the From/Sender from a email message
     */
    private static class FromMatcher implements Matcher {

        private final String sender;

        public FromMatcher(String sender) {
            this.sender = sender;
        }

        public boolean match(Message message) throws MessagingException {
            if (message.getFrom() != null && message.getFrom().length > 0) {
                for (Address address : message.getFrom()) {
                    if (((InternetAddress) address).getAddress().equalsIgnoreCase(sender)) {
                        LOG.info("Found sender: " + sender);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Match the body from a email message
     */
    private static class BodyMatcher implements Matcher {

        private final String body;

        public BodyMatcher(String body) {
            this.body = body;
        }

        public boolean match(Message message) throws MessagingException {
            try {
                if (getBody(message).contains(body)) {
                    LOG.info("Found body: " + body);
                    return true;
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            return false;
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
    }

    private String getKey(Message message) throws MessagingException {
        String sender = (message.getFrom() != null && message.getFrom().length > 0) ? message.getFrom()[0].toString() : "unknown_sender";
        return message.getSubject() + sender + message.getReceivedDate();
    }

    public int getCountSkipped() {
        return countSkipped;
    }
}
