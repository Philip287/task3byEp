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
    private static AtomicInteger ferryAreaInSquareMeters;
    private static AtomicInteger ferryWeightCapacity;
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    private static Ferry ferryInstance;
    private static AtomicBoolean isInstanceHas = new AtomicBoolean(false);
    private AtomicInteger ferryFreeAreaInSquareMeters;
    private AtomicInteger freeFerryWeightCapacity;
    private Queue<Vehicle> ferryQueue;
    private Queue<Vehicle> waitQueue;
    private AtomicBoolean ferryIsLoading = new AtomicBoolean(true);

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
        if (ferryAreaInSquareMeters == null || ferryWeightCapacity == null) {
            ferryAreaInSquareMeters = new AtomicInteger(DEFAULT_MAX_OF_AREA);
            ferryWeightCapacity = new AtomicInteger(DEFAULT_MAX_OF_BEARING_CAPACITY);
            LOGGER.info("Created ferry with default parameters area = " + ferryAreaInSquareMeters + " bearing capacity = "
                    + ferryWeightCapacity);
        }
        ferryFreeAreaInSquareMeters = new AtomicInteger(ferryAreaInSquareMeters.get());
        freeFerryWeightCapacity = new AtomicInteger(ferryWeightCapacity.get());
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
            loadVehiclesFromWaitQueueToFerry();
            ferryIsCross();
            ferryStartUnload();
        }
    }

    private boolean loadVehiclesFromWaitQueueToFerry() {
        boolean result = false;
        for (Vehicle vehicle : waitQueue) {
            LOGGER.info("Ferry can load: " + vehicle);
            result = loadVehicleToFerry(result, vehicle);
        }
        return result;
    }

    private boolean loadVehicleToFerry(boolean result, Vehicle vehicle) {
        while (ferryIsLoading.get()) {
            if (vehicle.getArea() < ferryFreeAreaInSquareMeters.get() & vehicle.getWeight() < freeFerryWeightCapacity.get()) {
                registerChangFullFerry(vehicle);
                result = ferryQueue.offer(vehicle);
                if (result) {
                    vehicle.getState().next();
                    vehicle.getState().next();
                    waitQueue.remove(vehicle);
                    LOGGER.info("Ferry: " + Thread.currentThread().getName() + vehicle.toString() + " is loaded to ferry.");
                }
            } else {
                ferryIsLoading.getAndSet(false);
            }
        }
        return result;
    }

    private void registerChangFullFerry(Vehicle vehicle) {
        int tempFreeBeringAreaCapacity = freeFerryWeightCapacity.get() - vehicle.getWeight();
        int tempFreeArea = ferryFreeAreaInSquareMeters.get() - vehicle.getArea();
        ferryFreeAreaInSquareMeters.getAndSet(tempFreeArea);
        freeFerryWeightCapacity.getAndSet(tempFreeBeringAreaCapacity);
        checkLoadFerry(tempFreeBeringAreaCapacity, tempFreeArea);
    }

    private void checkLoadFerry(int tempFreeBeringAreaCapacity, int tempFreeArea) {
        if ((tempFreeArea < ferryAreaInSquareMeters.get() * COEFFICIENT)
                || (tempFreeBeringAreaCapacity < ferryWeightCapacity.get() * COEFFICIENT)) {
            ferryIsLoading.getAndSet(false);
        }
    }

    private void ferryIsCross() {
        for (Vehicle vehicle : ferryQueue) {
            vehicle.getState().next();
        }
    }

    private boolean ferryStartUnload() {
        boolean result = false;
        for (Vehicle vehicle : ferryQueue) {
            if (!ferryIsLoading.get()) {
                LOGGER.info("Ferry:  " + Thread.currentThread().getName() + vehicle.toString() + " can unloaded from ferry.");
                vehicle.getState().next();
                result = ferryQueue.remove(vehicle);
                if (ferryQueue.isEmpty()) {
                    ferryFreeAreaInSquareMeters = new AtomicInteger(ferryAreaInSquareMeters.get());
                    freeFerryWeightCapacity = new AtomicInteger(ferryWeightCapacity.get());
                    LOGGER.info("Ferry is unload");
                    ferryIsLoading.getAndSet(true);
                }
            }
        }
        return result;
    }
}
