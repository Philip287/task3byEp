package by.suprun.task3.entity;

import by.suprun.task3.state.AbstractVehicleState;
import by.suprun.task3.state.RegisterToWaitQueueState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Vehicle implements Callable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ReentrantLock reentrantLock = new ReentrantLock(true);
    private final int vehicleNumber;
    private final VehicleType vehicleType;
    private final Ferry ferry;
    private AbstractVehicleState vehicleState;

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
    public Callable<Vehicle> call() {
        changeState(new RegisterToWaitQueueState(this));
        return null;
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

    public void vehicleStartLoadToFerry() {
        reentrantLock.lock();
        try {
            TimeUnit.SECONDS.sleep(2);
            LOGGER.info(vehicleType.toString() + " Number = " + vehicleNumber + " try load to ferry");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void transportVehicle() {
        reentrantLock.lock();
        try {
            LOGGER.info(vehicleType.toString() + " Number = " + vehicleNumber + " is crossing on ferry");
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean vehicleStartUnloadFromFerry() {
        boolean result = false;
        reentrantLock.lock();
        try {
            TimeUnit.SECONDS.sleep(1);
            LOGGER.info(vehicleType.toString() + " Number = " + vehicleNumber + " start unload from ferry");
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            reentrantLock.unlock();
        }
        return result;
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
