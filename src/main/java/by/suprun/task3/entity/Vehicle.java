package by.suprun.task3.entity;

import by.suprun.task3.state.impl.AbstractStateVehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Vehicle implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    private int vehicleNumber;
    private VehicleType vehicleType;
    private Phaser phaser;
    private AbstractStateVehicle state;
    Ferry ferry;



    public Vehicle(int vehicleNumber, VehicleType vehicleType, Phaser phaser) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.phaser = phaser;
        ferry = Ferry.getFerryInstance();
    }
    public void changeState(AbstractStateVehicle state) {
        this.state = state;
    }

    public AbstractStateVehicle getState() {
        return state;
    }


    @Override
    public void run() {
        phaser.register();
        phaser.arriveAndAwaitAdvance();
        ferry.loadVehicleToWaitQueue(this);
        phaser.arriveAndAwaitAdvance();
        startLoadVehicle(this);
        phaser.arriveAndAwaitAdvance();
        startUnloadVehicle(this);
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();
    }



    public void setState(AbstractStateVehicle state){
        this.state = state;
    }

    private void startLoadVehicle(Vehicle vehicle) {
        reentrantLock.lock();
        Thread.currentThread().setName(vehicleType.toString() + " Number = " + vehicleNumber);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info(vehicleType.toString() + " Number = " + vehicleNumber + " start load to ferry.");
        boolean load = ferry.loadVehicleToFerryAndTransport(vehicle);
        if (load){
            logger.info(vehicleType.toString() + " Number = " + vehicleNumber + " successful load to ferry.");
        }
        reentrantLock.unlock();
    }

    private void startUnloadVehicle(Vehicle vehicle) {
        reentrantLock.lock();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.info(vehicleType.toString() + " Number = " + vehicleNumber + " start to unload from ferry.");
        boolean unload = ferry.runToUnload(vehicle);
        if (unload){
            logger.info(vehicleType.toString() + " Number = " + vehicleNumber + " successful unload from ferry.");
        }
        reentrantLock.unlock();

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