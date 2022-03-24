package by.suprun.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class UnloadThread extends Thread {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.error("UnloadThread exception" + e);
        }
        logger.info("Daemon starts work");
        Ferry ferry = Ferry.getFerryInstance();
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (ferry.runToUnload()) {
                logger.info("Ferry is unloading");
            }
        }
    }
}
