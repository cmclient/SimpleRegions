package pl.kuezese.region.helper;

import java.util.*;

public final class StringHelper {

    public static String join(Collection<?> collection, String var1) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            if (builder.length() != 0) {
                builder.append(var1);
            }
            builder.append(o);
        }
        return builder.toString();
    }
    
    public static boolean isInteger(String string) {
        return string.matches("-?\\d+");
    }
}
