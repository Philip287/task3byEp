package by.suprun.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class Ferry {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int DEFAULT_MAX_OF_AREA = 130;
    private static final int DEFAULT_MAX_OF_BEARING_CAPACITY = 25000;
    private final double COEFFICIENT = 0.1;
    private static AtomicInteger area;
    private static AtomicInteger bearingCapacity;
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    private static Ferry ferryInstance;
    private static AtomicBoolean isInstanceHas = new AtomicBoolean(false);
    private AtomicInteger freeArea;
    private AtomicInteger freeBearingCapacity;
    private Queue<Vehicle> ferryQueue;
    private Queue<Vehicle> waitQueue;
    private AtomicBoolean isLoading = new AtomicBoolean(true);

    public static Ferry getFerryInstance() {
        if (!isInstanceHas.get()) {
            reentrantLock.lock();
            try {
                if (ferryInstance == null) {
                    ferryInstance = new Ferry();
                    isInstanceHas.getAndSet(true);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return ferryInstance;
    }

    private Ferry() {
        ferryQueue = new ArrayDeque<>();
        waitQueue = new ArrayDeque<>();
        if (area == null || bearingCapacity == null) {
            area = new AtomicInteger(DEFAULT_MAX_OF_AREA);
            bearingCapacity = new AtomicInteger(DEFAULT_MAX_OF_BEARING_CAPACITY);
            LOGGER.info("Created ferry with default parameters area = " + area + " bearing capacity = "
                    + bearingCapacity);
        }
        freeArea = new AtomicInteger(area.get());
        freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
    }

    public void loadVehicleToWaitQueue(Vehicle vehicle) {
        reentrantLock.lock();
        try {
            TimeUnit.MILLISECONDS.sleep(25);
            boolean result = waitQueue.offer(vehicle);
            if (result) {
                LOGGER.info("Ferry: " + vehicle.toString() + " is added to wait queue");
            } else {
                LOGGER.info("Ferry: " + vehicle.toString() + " not added to wait queue");
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void ferryStartWork() {
        while (ferryQueue.isEmpty()) {

            loadVehicleFromWaitQueueToFerry();
            ferryCross();
            ferryUnload();
        }
    }

    private boolean loadVehicleFromWaitQueueToFerry() {
        boolean result = false;
        for (Vehicle vehicle : waitQueue) {
            LOGGER.info("Ferry can load: " + vehicle);
            result = loadVehicle(result, vehicle);
        }
        return result;
    }

    private boolean loadVehicle(boolean result, Vehicle vehicle) {
        while (isLoading.get()) {
            if (vehicle.getArea() < freeArea.get() & vehicle.getWeight() < freeBearingCapacity.get()) {
                registerChangFullFerry(vehicle);
                result = ferryQueue.offer(vehicle);
                if (result) {
                    vehicle.getState().next();
                    vehicle.getState().next();
                    waitQueue.remove(vehicle);
                    LOGGER.info("Ferry: " + Thread.currentThread().getName() + vehicle.toString() + " is loaded to ferry.");
                }
            } else {
                isLoading.getAndSet(false);

            }
        }
        return result;
    }

    private void registerChangFullFerry(Vehicle vehicle) {
        int tempFreeBeringAreaCapacity = freeBearingCapacity.get() - vehicle.getWeight();
        int tempFreeArea = freeArea.get() - vehicle.getArea();
        freeArea.getAndSet(tempFreeArea);
        freeBearingCapacity.getAndSet(tempFreeBeringAreaCapacity);
        if ((tempFreeArea < area.get() * COEFFICIENT)
                || (tempFreeBeringAreaCapacity < bearingCapacity.get() * COEFFICIENT)) {
            isLoading.getAndSet(false);
        }
    }

    private void ferryCross() {
        for (Vehicle vehicle : ferryQueue) {
            vehicle.getState().next();
        }
    }

    private boolean ferryUnload() {
        boolean result = false;
        for (Vehicle vehicle : ferryQueue) {
            if (!isLoading.get()) {
                LOGGER.info("Ferry:  " + Thread.currentThread().getName() + vehicle.toString() + " can unloaded from ferry.");
                vehicle.getState().next();
                result = ferryQueue.remove(vehicle);
                if (ferryQueue.isEmpty()) {
                    freeArea = new AtomicInteger(area.get());
                    freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
                    LOGGER.info("Ferry is unload");
                    isLoading.getAndSet(true);
                }
            }
        }
        return result;
    }
}