package com.bondegaard.shop.utils;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {
    public static String format(String message, String... replacements) {
        if (replacements.length <= 0)
            return message;
        for (int i = 0; i < replacements.length; i++)
            message = StringUtil.colorize(message.replace("{" + i + "}", replacements[i]));
        return message;
    }

    public static String[] format(String[] message, String... replacements) {
        if (replacements.length <= 0)
            return message;
        String[] cloned = (String[])message.clone();
        for (int i = 0; i < cloned.length; i++)
            cloned[i] = format(cloned[i], replacements);
        return cloned;
    }

    public static List<String> format(List<String> message, String... replacements) {
        if (replacements.length <= 0)
            return message;
        List<String> cloned = new ArrayList<>(message);
        for (int i = 0; i < cloned.size(); i++)
            cloned.set(i, format(cloned.get(i), replacements));
        return cloned;
    }

    public static String lower(String strings) {
        return strings.toLowerCase();
    }

    public static String[] lower(String[] strings) {
        String[] cloned = (String[])strings.clone();
        for (int i = 0; i < cloned.length; i++)
            cloned[i] = lower(cloned[i]);
        return cloned;
    }
}