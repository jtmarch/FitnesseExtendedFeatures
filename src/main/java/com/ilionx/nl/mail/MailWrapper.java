package com.ilionx.nl.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Mail wrapper
 */
public class MailWrapper {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    private Map<String, String> valueHolder = new HashMap<>();

    public String getProperty(final String param) {
        return this.valueHolder.get(param);
    }

    public void setFrom(final String from) {
        this.valueHolder.put("from", from);
    }

    public void setDate(Date date) {
        this.valueHolder.put("date", getDateAsString(date));
    }

    public void setCc(String cc) {
        this.valueHolder.put("cc", cc);
    }

    public void setSubject(final String subject) {
        this.valueHolder.put("subject", subject);
    }

    public void setContent(final String content) {
        this.valueHolder.put("content", content);
    }

    public void setReplyTo(final String replyTo) {
        this.valueHolder.put("replyTo", replyTo);
    }

    public void setBcc(String bcc) {
        this.valueHolder.put("bcc", bcc);
    }

    public void setTo(String to) {
        this.valueHolder.put("to", to);
    }

    public String getDateAsString(Date date) {
        if (date != null) {
            return formatter.format(date);
        }
        return "";
    }

    public Map<String, String> getExternalLinks() {
        Map<String, String> externalLinks = new HashMap();

        String bodyContent = this.valueHolder.get("content");
        int startA = bodyContent.indexOf("<a");

        while (startA >= 0) {
            int endA = bodyContent.indexOf("</a");
            if (endA < 0) {
                break;
            }
            String subContent = bodyContent.substring(startA, endA);

            int hrefStart = subContent.indexOf("href=");
            int hrefEnd = subContent.indexOf("\"", hrefStart+6);
            String linkAddress = subContent.substring(hrefStart + 6, hrefEnd);

            int labelStart = subContent.indexOf(">");
            String linkName = subContent.substring(labelStart + 1, subContent.length());

            externalLinks.put(linkName, linkAddress);

            bodyContent = bodyContent.substring(endA+4);
            startA = bodyContent.indexOf("<a");

        }

        return externalLinks;
    }
}
