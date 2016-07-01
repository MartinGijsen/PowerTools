/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.instructions;

import org.powertools.engine.Currencies;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.powertools.engine.Currency;
import org.powertools.engine.ParameterConvertor;
import org.powertools.engine.util.PowerToolsParser;


public final class MoneyConvertor implements ParameterConvertor {
    private static final Pattern AMOUNT_AND_CURRENCY_PATTERN = Pattern.compile ("^ *(-?\\d+(,\\d+)?) *([^\\d]+)$");
    private static final Pattern CURRENCY_AND_AMOUNT_PATTERN = Pattern.compile ("^ *([^\\d]+) *(-?\\d+(,\\d+)?)$");
    private static final Matcher AMOUNT_AND_CURRENCY_MATCHER = AMOUNT_AND_CURRENCY_PATTERN.matcher ("");
    private static final Matcher CURRENCY_AND_AMOUNT_MATCHER = CURRENCY_AND_AMOUNT_PATTERN.matcher ("");
    
    private final Currencies mCurrencies;


    public MoneyConvertor (Currencies currencies) {
        mCurrencies = currencies;
    }
    
    @Override
    public Object toObject (String text) {
        String amount;
        String currencyText;
        if (AMOUNT_AND_CURRENCY_MATCHER.reset (text).matches ()) {
            amount       = AMOUNT_AND_CURRENCY_MATCHER.group (1);
            currencyText = AMOUNT_AND_CURRENCY_MATCHER.group (3).trim ();
        } else if (CURRENCY_AND_AMOUNT_MATCHER.reset (text).matches ()) {
            currencyText = CURRENCY_AND_AMOUNT_MATCHER.group (1).trim ();
            amount       = CURRENCY_AND_AMOUNT_MATCHER.group (2);
        } else {
            return null;
        }
        Currency currency = mCurrencies.get (currencyText);
        return new MoneyImpl (currency, PowerToolsParser.parseDouble (amount));
    }
}
