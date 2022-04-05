package by.suprun.task3;

import by.suprun.task3.entity.Ferry;
import by.suprun.task3.entity.Vehicle;
import by.suprun.task3.entity.VehicleType;
import by.suprun.task3.service.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {

    static boolean isDaemon = true;
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Ferry ferryInstance = Ferry.getFerryInstance();
        Service service = new Service();
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
        service.runWithExecutors(listThreadVehicle, isDaemon);
        ferryInstance.ferryStartWork();

    }
}