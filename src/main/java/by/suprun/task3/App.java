package by.suprun.task3;

import by.suprun.task3.entity.Ferry;
import by.suprun.task3.entity.Vehicle;
import by.suprun.task3.entity.VehicleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class App {
    private static final Logger LOGGER = LogManager.getLogger();
    static boolean isDaemon = true;

    public static void main(String[] args) {
        List<Vehicle> listThreadVehicle = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            boolean temp = random.nextBoolean();
            if (temp) {
                listThreadVehicle.add(new Vehicle(i + 1, VehicleType.CAR));
            } else {
                listThreadVehicle.add(new Vehicle(i + 1, VehicleType.TRUCK));
            }
        }
        createThreadWithExecutor(listThreadVehicle, isDaemon);
        Ferry ferryInstance = Ferry.getFerryInstance();
        ferryInstance.ferryStartWork();
    }

    public static void createThreadWithExecutor(List<Vehicle> listThreadVehicle, boolean isDaemon) {
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable vehicle) {
                Thread thread = new Thread(vehicle);
                thread.setDaemon(isDaemon);
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
