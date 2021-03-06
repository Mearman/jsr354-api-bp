/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money;

import java.io.Serializable;

/**
 * This class models the spec/configuration for a rounding, modeled as {@link MonetaryRounding} in a
 * platform independent way. Each RoundingContext instance hereby has a {@code roundingId}, which links
 * to the {@link javax.money.spi.RoundingProviderSpi} that must of the according rounding instance. The
 * <i>default</i> {@code roundingId} is <code>default</code>.
 *
 *
 * A RoundingContext can take up arbitrary attributes that must be documented by the
 * {@link javax.money.spi.RoundingProviderSpi} implementations.
 *
 *
 * Examples for such additional attributes are
 * {@link java.math.RoundingMode}, {@link java.math.MathContext}, additional regional information,
 * e.g. if a given rounding is targeting cash rounding.
 *
 *
 * This class is immutable, serializable, thread-safe.
 *
 * @author Anatole Tresch
 */
public final class RoundingContext extends AbstractContext implements Serializable, CurrencySupplier {

    private static final long serialVersionUID = -1879443887564347935L;

    /**
     * Attribute key used for the rounding name.
     */
    static final String KEY_ROUNDING_NAME = "roundingName";

    /**
     * Constructor, used from the {@link RoundingContextBuilder}.
     *
     * @param builder the corresponding builder, not null.
     */
    RoundingContext(RoundingContextBuilder builder) {
        super(builder);
    }

    /**
     * Get the (custom) rounding id.
     *
     * @return the rounding id, or null.
     */
    public String getRoundingName() {
        return getText(KEY_ROUNDING_NAME);
    }

    /**
     * Get the basic {@link CurrencyUnit}, which is based for this rounding type.
     *
     * @return the target CurrencyUnit, or null.
     */
    public CurrencyUnit getCurrency() {
        return get(CurrencyUnit.class);
    }

    /**
     * Allows to convert a instance into the corresponding {@link CurrencyContextBuilder}, which allows
     * to change the values and of another {@link CurrencyContext} instance.
     *
     * @return a new Builder instance, preinitialized with the values from this instance.
     */
    public RoundingContextBuilder toBuilder() {
        return RoundingContextBuilder.of(this);
    }

}
