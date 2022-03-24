package by.suprun.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Vehicle implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private int vehicleNumber;
    private VehicleType vehicleType;

    public Vehicle(int vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    @Override
    public void run() {
        final Phaser START = new Phaser();
        Thread.currentThread().setName(vehicleType.toString() + " Number = " + vehicleNumber);
        Ferry ferry = Ferry.getFerryInstance();
        boolean load = false;
        while (!load) {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            load = ferry.loadVehicle(this);
        }
        START.arriveAndDeregister();
        START.awaitAdvance(0);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error(e);
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.error("UnloadThread exception" + e);
        }
        logger.info("Daemon starts work");
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

    public int getVehicleNumber() {
        return vehicleNumber;
    }

    public int getArea() {
        return vehicleType.getArea().get();
    }

    public int getWeight() {
        return vehicleType.getWeight().get();
    }

    @Override
    public int hashCode() {
        int result = vehicleNumber;
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vehicle vehicle = (Vehicle) obj;
        if (vehicleNumber != vehicle.vehicleNumber) return false;
        return vehicleType == vehicle.vehicleType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vehicle Number = ").append(vehicleNumber).append(" ");
        sb.append("Type = ").append(vehicleType).append(".");
        return sb.toString();
    }
}
