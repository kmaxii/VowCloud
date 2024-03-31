package me.kmaxi.vowcloud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL11;

public class Loggers {

    private static final String LOG_PREFIX = "Vowcloud - ";
    private static final Logger LOGGER = LogManager.getLogger(LOG_PREFIX + "General");
    private static final Logger ERROR_LOGGER = LogManager.getLogger(LOG_PREFIX + "Error");

    public static void log(String message){
        LOGGER.info(LOGGER.getName() + ": " + message);
    }

    public static void log(String message, Object... args){
        LOGGER.info(LOGGER.getName() + ": " + message, args);

    }
    public static void error(String message){ERROR_LOGGER.error(ERROR_LOGGER.getName() + " " + message);}

    public static void logALError(String errorMessage) {
        int error = AL11.alGetError();
        if (error == AL11.AL_NO_ERROR) {
            return;
        }

        String errorName = switch (error) {
            case AL11.AL_INVALID_NAME -> "AL_INVALID_NAME";
            case AL11.AL_INVALID_ENUM -> "AL_INVALID_ENUM";
            case AL11.AL_INVALID_VALUE -> "AL_INVALID_VALUE";
            case AL11.AL_INVALID_OPERATION -> "AL_INVALID_OPERATION";
            case AL11.AL_OUT_OF_MEMORY -> "AL_OUT_OF_MEMORY";
            default -> Integer.toString(error);
        };

        LOGGER.error("{}: OpenAL error {}", errorMessage, errorName);
    }
}
