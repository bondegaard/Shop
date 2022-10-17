package com.bondegaard.shop.utils;

import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class StringUtil {

    private final static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

    static {
        numberFormat.setMaximumFractionDigits(5);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String uncolorize(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> colorize(List<String> stringList) {
        if (stringList == null)
            return null;
        for (int i = 0; i < stringList.size(); i++)
            stringList.set(i, colorize(stringList.get(i)));
        return stringList;
    }

    public static String formatNum(double input) {
        return numberFormat.format(input);
    }

    public static String formatNum(BigDecimal input) {
        return numberFormat.format(input);
    }

    public static String formatList(List<String> strings, String placeholder) {
        if (strings.isEmpty())
            return placeholder;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            if (i != 0)
                sb.append((i == strings.size() - 1) ? " og " : ", ");
            sb.append(strings.get(i));
        }
        return sb.toString();
    }

    public static boolean endsWith(String paramString, String... paramStrings) {
        for (String s : paramStrings) {
            if (paramString.substring(paramString.length() - s.length()).equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public static boolean isAlphabetical(String s) {
        if (s == null)
            return false;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isLetter(s.charAt(i)))
                return false;
        }
        return true;
    }

    public static String getCapitalized(String value) {
        List<String> list = new ArrayList<>();
        for (String s : value.split(" "))
            list.add(s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
        return String.join(" ", (Iterable)list);
    }
}
