package by.suprun.task3.validator.impl;

import by.suprun.task3.validator.FileValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class FileValidatorImpl implements FileValidator {
    public static final Logger logger = LogManager.getLogger();

    @Override
    public boolean validateFile(String path) {
        boolean result = false;
        logger.info("Method of validation file called to file name: " + path);
        if (path == null || path.isEmpty() || path.trim().isEmpty()) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            if (file.length() > 0) {
                result = true;
            }
        }
        return result;
    }
}
