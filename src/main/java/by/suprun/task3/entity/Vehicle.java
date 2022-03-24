package by.suprun.task3.entity;

import java.util.concurrent.TimeUnit;

public class Vehicle extends Thread {
    private int vehicleNumber;
    private VehicleType vehicleType;

    public Vehicle(int vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    @Override
    public void run() {
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
    }

    public int getVehicleNumber() {
        return vehicleNumber;
    }

    public int getArea() {
        return vehicleType.getArea();
    }

    public int getWeight() {
        return vehicleType.getWeight();
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
