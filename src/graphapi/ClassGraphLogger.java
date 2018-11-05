package graphapi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassGraphLogger {

    private static com.intellij.openapi.diagnostic.Logger logger;

    static {
        logger = com.intellij.openapi.diagnostic.Logger.getInstance("graphapi");
    }

    public static void debug(String message) {
        logger.debug(getPrefix() + message);
    }

    private static String getPrefix() {
        return "[" + getTime() + "] ";
    }

    private static String getTime() {
        String pattern = "HH:mm:ss.SSS";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date now = new Date();
        return format.format(now);
    }
}
