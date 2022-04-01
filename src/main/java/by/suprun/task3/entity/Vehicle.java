package by.suprun.task3.entity;

import by.suprun.task3.state.AbstractVehicleState;
import by.suprun.task3.state.RegisterToWaitQueueState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Vehicle implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    private int vehicleNumber;
    private VehicleType vehicleType;
    private AbstractVehicleState vehicleState;
    private Ferry ferry;

    public Vehicle(int vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        ferry = Ferry.getFerryInstance();
    }

    public void changeState(AbstractVehicleState vehicleState) {
        this.vehicleState = vehicleState;
    }

    public AbstractVehicleState getState() {
        return vehicleState;
    }

    @Override
    public void run() {
        changeState(new RegisterToWaitQueueState(this));
    }

    public void addVehicleToWaitQueueFerry() {
        reentrantLock.lock();
        Thread.currentThread().setName(vehicleType.toString() + " Number = " + vehicleNumber);
        try {
            TimeUnit.SECONDS.sleep(1);
            ferry.loadVehicleToWaitQueue(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void startLoadVehicle(Vehicle vehicle) {
        reentrantLock.lock();
        Thread.currentThread().setName(vehicleType.toString() + " Number = " + vehicleNumber);
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info(vehicleType.toString() + " Number = " + vehicleNumber + " start load to ferry.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void startUnloadVehicle(Vehicle vehicle) {
        reentrantLock.lock();
        try {
            TimeUnit.SECONDS.sleep(2);
            logger.info(vehicleType.toString() + " Number = " + vehicleNumber + " start to unload from ferry.");
        } catch (InterruptedException e) {
            logger.error(e);
        } finally {
            reentrantLock.unlock();
        }
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