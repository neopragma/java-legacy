package com.neopragma.legacy.round14;

import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.ResourceBundle;

public class NameFactory {

    public static Name newInstance(Locale locale, String...nameParts) {
        ResourceBundle rb = ResourceBundle.getBundle("locale", locale);
        String nameClassName = rb.getString("name.class");
        try {
            Class nameClass = Class.forName(nameClassName);
            Constructor constructor =
                    nameClass.getDeclaredConstructor(String[].class);
            return (Name) constructor.newInstance((Object) nameParts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
