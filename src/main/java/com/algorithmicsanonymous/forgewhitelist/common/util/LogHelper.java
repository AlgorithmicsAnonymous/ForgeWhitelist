package com.algorithmicsanonymous.forgewhitelist.common.util;

import com.algorithmicsanonymous.forgewhitelist.ModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    private static Logger logger = LogManager.getLogger(ModInfo.MOD_NAME);

    public static void log(Level logLevel, Object message) {
        logger.log(logLevel, message.toString());
    }

    public static void all(Object message) {
        log(Level.ALL, message.toString());
    }

    public static void debug(Object message) {
        log(Level.DEBUG, message.toString());
    }

    public static void trace(Object message) {
        log(Level.TRACE, message.toString());
    }

    public static void fatal(Object message) {
        log(Level.FATAL, message.toString());
    }

    public static void error(Object message) {
        log(Level.ERROR, message.toString());
    }

    public static void warn(Object message) {
        log(Level.WARN, message.toString());
    }

    public static void info(Object message) {
        log(Level.INFO, message.toString());
    }

    public static void off(Object message) {
        log(Level.OFF, message.toString());
    }

    public static void internal(Object message) {
        if (Platform.isDevEnv())
            log(Level.INFO, message.toString());
    }
}
