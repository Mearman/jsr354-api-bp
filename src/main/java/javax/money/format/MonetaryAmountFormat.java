/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money.format;

import javax.money.MonetaryAmount;
import javax.money.MonetaryQuery;
import java.io.IOException;

/**
 * Formats instances of {@code MonetaryAmount} to a {@link String} or an {@link Appendable}.
 *
 *
 * To obtain a {@code MonetaryAmountFormat} for a specific locale, including the default
 * locale, call {@link MonetaryFormats#getAmountFormat(java.util.Locale, String...)}.
 *
 * More complex formatting scenarios can be implemented by registering instances of {@link javax.money.spi
 * .MonetaryAmountFormatProviderSpi}.
 * The spi implementation creates new instances of {@link MonetaryAmountFormat} based on the
 * <i>styleId</i> and <i> (arbitrary) attributes</i> passed within the {@link AmountFormatContext}.
 *
 *
 * In general, do prefer accessing {@code MonetaryAmountFormat} instances from the {@link MonetaryFormats} singleton,
 * instead of instantiating implementations directly, since the {@code MonetaryFormats} factory
 * method may return different subclasses or may implement contextual behaviour (in a EE context).
 * If you need to customize the format object, do something like this:
 * {@code
 * MonetaryAmountFormat f = MonetaryFormats.getInstance(loc);
 * f.setStyle(f.getStyle().toBuilder().setPattern(&quot;###.##;(###.##)&quot;).build());
 * }
 *
 * <b>Special Values</b>
 *
 * Negative zero ({@code "-0"}) should always parse to
 * <ul>
 * <li>{@code 0}</li>
 * </ul>
 *
 *
 * <b><a name="synchronization">Synchronization</a></b>
 *
 * Instances of this class are not required to be thread-safe. It is recommended to of separate
 * format instances for each thread. If multiple threads access a format concurrently, it must be
 * synchronized externally.
 *
 *
 * <b>Example</b>
 * {@code
 * // Print out a number using the localized number, currency,
 * // for each locale</strong>
 * Locale[] locales = MonetaryFormats.getAvailableLocales();
 * MonetaryAmount amount = ...;
 * MonetaryAmountFormat form;
 *     System.out.println("FORMAT");
 *     for (int i = 0; i < locales.length; ++i) {
 *         if (locales[i].getCountry().length() == 0) {
 *            continue; // Skip language-only locales
 *         }
 *         System.out.print(locales[i].getDisplayName());
 *         form = MonetaryFormats.getInstance(locales[i]);
 *         System.out.print(": " + form.getStyle().getPattern());
 *         String myAmount = form.format(amount);
 *         System.out.print(" -> " + myAmount);
 *         try {
 *             System.out.println(" -> " + form.parse(form.format(myAmount)));
 *         } catch (ParseException e) {}
 *     }
 * }
 * }
 */
public interface MonetaryAmountFormat extends MonetaryQuery<String> {


    /**
     * The {@link AmountFormatContext} to be applied when a {@link javax.money.MonetaryAmount} is formatted.
     *
     * @return the {@link AmountFormatContext} used, never {@code null}.
     */
    AmountFormatContext getContext();


    /**
     * Formats the given {@link javax.money.MonetaryAmount} to a String.
     *
     * @param amount the amount to format, not {@code null}
     * @return the string printed using the settings of this formatter
     * @throws UnsupportedOperationException if the formatter is unable to print
     */
    String format(MonetaryAmount amount);

    /**
     * Formats the given {@link MonetaryAmount} to a {@code Appendable}.
     * <p>
     * Example implementations of {@code Appendable} are {@code StringBuilder}, {@code StringBuffer}
     * or {@code Writer}. Note that {@code StringBuilder} and {@code StringBuffer} never throw an
     * {@code IOException}.
     *
     * @param appendable the appendable to add to, not null
     * @param amount     the amount to print, not null
     * @throws UnsupportedOperationException if the formatter is unable to print
     * @throws java.io.IOException                   if an IO error occurs, thrown by the {@code appendable}
     * @throws MonetaryParseException        if there is a problem while parsing
     */
    void print(Appendable appendable, MonetaryAmount amount) throws IOException;

    /**
     * Fully parses the text into an instance of {@link MonetaryAmount}.
     * <p>
     * The parse must complete normally and parse the entire text. If the parse completes without
     * reading the entire length of the text, an exception is thrown. If any other problem occurs
     * during parsing, an exception is thrown.
     * <p>
     * Additionally the effective implementation type returned can be determined by the
     * {@link javax.money.MonetaryContext} applied to the .
     * This formatter will call
     * {@link javax.money.Monetary#getDefaultAmountType()} and will use the result returned
     * to access a corresponding {@link javax.money.MonetaryAmountFactory} to of the instance
     * returned.
     *
     * @param text the text to parse, not null
     * @return the parsed value, never {@code null}
     * @throws UnsupportedOperationException if the formatter is unable to parse
     * @throws MonetaryParseException        if there is a problem while parsing
     */
    MonetaryAmount parse(CharSequence text) throws MonetaryParseException;

}
