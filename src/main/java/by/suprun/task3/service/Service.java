package by.suprun.task3.service;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


public class Service {
    private static final Logger LOGGER = LogManager.getLogger();

    public void runWithExecutors(List<Vehicle> listThreadVehicle, boolean isDaemon) {
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable vehicle) {
                Thread thread = new Thread(vehicle);
                if (isDaemon) {
                    thread.setDaemon(true);
                }
                return thread;
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(listThreadVehicle.size(), factory);

        for (Vehicle vehicle : listThreadVehicle) {
            executorService.submit(vehicle);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Error in executorService.awaitTermination()" + e);
        }
    }
}
