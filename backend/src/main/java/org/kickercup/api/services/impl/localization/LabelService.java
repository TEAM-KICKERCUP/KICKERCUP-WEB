//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Label Service (Localization)
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.localization;

import org.kickercup.api.services.impl.tournament.result.TournamentResultBuilderImpl;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LabelService {
    private static final Logger LOGGER = Logger.getLogger(TournamentResultBuilderImpl.class.getName());

    public static String getString(Locale locale, String key) {
        if (null == locale) {
            locale = Locale.ENGLISH;
        }
        try {
            ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
            return messages.getString(key);
        } catch (MissingResourceException | NullPointerException ex) {
            LOGGER.warning("A Label could not be retrieved, key=" + (null != key ? key : "null"));
            return "";
        }
    }
}
