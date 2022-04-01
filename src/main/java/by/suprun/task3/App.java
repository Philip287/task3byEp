package by.suprun.task3;

import by.suprun.task3.entity.Ferry;
import by.suprun.task3.entity.Vehicle;
import by.suprun.task3.entity.VehicleType;
import by.suprun.task3.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {

    static boolean isDaemon = true;

    public static void main(String[] args) {
        Service service = new Service();

        List<Vehicle> listThreadVehicle = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            boolean temp = random.nextBoolean();
            if (temp) {
                listThreadVehicle.add(new Vehicle(i + 1, VehicleType.CAR));
            } else {
                listThreadVehicle.add(new Vehicle(i + 1, VehicleType.TRUCK));
            }
        }
        service.runWithExecutors(listThreadVehicle, isDaemon);
        Ferry ferry = Ferry.getFerryInstance();
        ferry.loadVehicleToFerryAndTransport();
        ferry.runToUnload();

    }


}
