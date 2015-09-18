package com.ilionx.nl;

import com.ilionx.nl.mail.MailHandlerFixture;
import com.ilionx.nl.products.ProductsFixture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Extended Selenium driver fixtures to add support for mail, dates.
 */
public class ExtendedSeleniumDriverFixtures extends MailHandlerFixture {

    /**
     * <p><code>
     * | $converterdDate= | convert datum | <i>datum</i> | with format | <i>incomingformat</i> | to format | <i>outgoingformat</i> |
     * </code></p>
     *
     * @param datum
     * @param incomingformat
     * @param outgoingformat
     * @return datum in new format
     */
    public String convertDatumWithFormatToFormat(final String datum, final String incomingformat, final String outgoingformat) throws ParseException {
        return new SimpleDateFormat(outgoingformat).format(new SimpleDateFormat(incomingformat).parse(datum));
    }

    /**
     * <p><code>
     * | $converterdDate= | convert datum | <i>datum</i> | with format | <i>incomingformat</i> | with local | <i>local</i> | to format | <i>outgoingformat</i> | with local | <i>local</i> |
     * </code></p>
     *
     * @param datum
     * @param incomingformat
     * @param local
     * @param outgoingformat
     * @param outLocal
     * @return datum in new format
     */
    public String convertDatumWithFormatWithLocalToFormatWithLocal(final String datum, final String incomingformat, final String local, final String outgoingformat, final String outLocal) throws ParseException {
        return new SimpleDateFormat(outgoingformat, new Locale(outLocal)).format(new SimpleDateFormat(incomingformat, new Locale(local)).parse(datum));
    }

}
