package by.suprun.task3;

import by.suprun.task3.entity.Ferry;
import by.suprun.task3.entity.Vehicle;
import by.suprun.task3.entity.VehicleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class App {
    private static final Logger logger = LogManager.getLogger();
    static boolean isDaemon = true;

    public static void main(String[] args) {

        List<Vehicle> listThreadVehicle = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            boolean temp = random.nextBoolean();
            if (temp) {
                listThreadVehicle.add(new Vehicle(i, VehicleType.CAR, new Phaser()));
            } else {
                listThreadVehicle.add(new Vehicle(i, VehicleType.TRUCK, new Phaser()));
            }
        }
        runWithExecutors(listThreadVehicle, isDaemon);
        Thread ferry = new Thread(Ferry.getFerryInstance());
        ferry.start();
    }

    private static void runWithExecutors(List<Vehicle> listThreadVehicle, boolean isDaemon) {

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
            executorService.execute(vehicle);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Error in executorService.awaitTermination()" + e);
        }
    }
}
