package by.suprun.task3;

import by.suprun.task3.entity.Ferry;
import by.suprun.task3.entity.Vehicle;
import by.suprun.task3.entity.VehicleType;
import by.suprun.task3.reader.PropertiesReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    private static final Logger LOGGER = LogManager.getLogger();
    static boolean isDaemon = true;
    static boolean readFromProperties = false;

    public static void main(String[] args) {
        List<Vehicle> listThreadVehicle = prepareVehiclesList();
        if (readFromProperties) {
            Map<String, Integer> stringIntegerMap = PropertiesReader.readPropertiesParametersForFerry("property.default_ferry");
            setFerryConfigurationFromProperties(stringIntegerMap);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(listThreadVehicle.size(),
                prepareVehicleThreadsFactory());
        submitThreadsByExecutorService(listThreadVehicle, executorService);
        shutdownExecutorService(executorService);

        Ferry ferryInstance = Ferry.getFerryInstance();
        ferryInstance.ferryStartWork();
    }

    private static List<Vehicle> prepareVehiclesList() {
        List<Vehicle> listThreadVehicle = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            if (random.nextBoolean()) {
                listThreadVehicle.add(new Vehicle(i + 1, VehicleType.CAR));
            } else {
                listThreadVehicle.add(new Vehicle(i + 1, VehicleType.TRUCK));
            }
        }
        return listThreadVehicle;
    }

    private static ThreadFactory prepareVehicleThreadsFactory() {
        return vehicle -> {
            Thread thread = new Thread(vehicle);
            thread.setDaemon(isDaemon);
            return thread;
        };
    }

    private static void submitThreadsByExecutorService(List<Vehicle> listThreadVehicle, ExecutorService executorService) {
        for (Vehicle vehicle : listThreadVehicle) {
            executorService.submit(vehicle);
        }
    }

    private static void shutdownExecutorService(ExecutorService executorService) {
        executorService.shutdown();

        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Error in executorService.awaitTermination()" + e);
        }
    }

    public static void setFerryConfigurationFromProperties(Map<String, Integer> parameters) {
        Ferry.setFerryAreaInSquareMeters(new AtomicInteger(parameters.get("default_ferry_area_in_square_meters")));
        Ferry.setFerryWeightCapacity(new AtomicInteger(parameters.get("default_ferry_weight_capacity")));
    }
}
