package by.suprun.task3;

import by.suprun.task3.entity.Vehicle;
import by.suprun.task3.entity.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class App {
    private static final Phaser PHASER = new Phaser(1);
    public static void main(String[] args) {
        List<Vehicle> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            boolean temp = random.nextBoolean();
            if (temp) {
                list.add(new Vehicle(i, VehicleType.CAR));
            } else {
                list.add(new Vehicle(i, VehicleType.TRUCK));
            }
        }
        ExecutorService service = Executors.newFixedThreadPool(list.size());
        for (Vehicle vehicle : list) {
            vehicle.start();
        }

    }
}
