package fr.hanquezr.automatedbarapp.utils;

import android.content.Context;

/**
 * Implement current property methods
 */
public class PropertyUtils {

    private Context context;
    private final String SHARED_PREFERENCES_NAME = "automatedbar_values";

    public PropertyUtils (Context context) {
        this.context = context;
    }

    /**
     * Save the property into the android phone
     * @param propertyKey The property key to save
     * @param propertyValue The property value to save
     */
    public void saveProperty (String propertyKey, String propertyValue) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                .putString(propertyKey, propertyValue).apply();
    }

    /**
     * Get the property save into phone
     * @param propertyKey The property key to get the value
     * @return The property value
     */
    public String getProperty (String propertyKey) {
        String propertyValue = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(propertyKey, null);

        return propertyValue != null ? propertyValue : "";
    }

}
