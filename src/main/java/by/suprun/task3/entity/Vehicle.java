package by.suprun.task3.entity;

import by.suprun.task3.state.RegisterToWaitQueueState;
import by.suprun.task3.state.VehicleState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Vehicle implements Callable<Vehicle> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ReentrantLock reentrantLock = new ReentrantLock(true);
    private final int vehicleNumber;
    private final VehicleType vehicleType;
    private VehicleState vehicleState;

    public Vehicle(int vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    @Override
    public Vehicle call() {
        Thread.currentThread().setName(vehicleType.toString() + " Number = " + vehicleNumber);
        try {
            TimeUnit.SECONDS.sleep(1);
            setState(new RegisterToWaitQueueState(this));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public void setState(VehicleState vehicleState) {
        this.vehicleState = vehicleState;
    }

    public void nextState() {
        vehicleState.next(this);
    }


    public int getArea() {
        return vehicleType.getAreaInSquareMeters().get();
    }

    public int getWeight() {
        return vehicleType.getWeightInKilogram().get();
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
