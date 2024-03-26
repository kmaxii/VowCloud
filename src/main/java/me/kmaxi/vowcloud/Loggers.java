package me.kmaxi.vowcloud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Loggers {

    private static final String LOG_PREFIX = "Vowcloud - ";
    private static final Logger LOGGER = LogManager.getLogger(LOG_PREFIX + "General");
    private static final Logger ERROR_LOGGER = LogManager.getLogger(LOG_PREFIX + "Error");

    public static void log(String message){
        LOGGER.info(LOGGER.getName() + ": " + message);
    }
    public static void error(String message){ERROR_LOGGER.error(ERROR_LOGGER.getName() + " " + message);}
}
