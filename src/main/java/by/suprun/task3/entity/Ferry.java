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
    private static final int DEFAULT_MAX_OF_FERRY_AREA_IN_SQUARE_METERS = 130;
    private static final int DEFAULT_MAX_OF_FERRY_WEIGHT_CAPACITY = 25000;
    private final double COEFFICIENT = 0.1;
    private static AtomicInteger ferryAreaInSquareMeters;
    private static AtomicInteger ferryWeightCapacity;
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    private static Ferry ferryInstance;
    private static AtomicBoolean isInstanceHas = new AtomicBoolean(false);
    private AtomicInteger ferryFreeAreaInSquareMeters;
    private AtomicInteger freeFerryWeightCapacity;
    private Queue<Vehicle> ferryVehicleOnBoard;
    private Queue<Vehicle> ferryWaitQueue;
    private AtomicBoolean isFerryNotLoaded = new AtomicBoolean(true);

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
        ferryVehicleOnBoard = new ArrayDeque<>();
        ferryWaitQueue = new ArrayDeque<>();
        if (ferryAreaInSquareMeters == null || ferryWeightCapacity == null) {
            ferryAreaInSquareMeters = new AtomicInteger(DEFAULT_MAX_OF_FERRY_AREA_IN_SQUARE_METERS);
            ferryWeightCapacity = new AtomicInteger(DEFAULT_MAX_OF_FERRY_WEIGHT_CAPACITY);
        }
        ferryFreeAreaInSquareMeters = new AtomicInteger(ferryAreaInSquareMeters.get());
        freeFerryWeightCapacity = new AtomicInteger(ferryWeightCapacity.get());
        LOGGER.info("Created ferry with default parameters area = " + ferryAreaInSquareMeters + " bearing capacity = "
                + ferryWeightCapacity);
    }

    public void loadVehicleToFerryWaitQueue(Vehicle vehicle) {
        reentrantLock.lock();
        try {
            TimeUnit.MILLISECONDS.sleep(25);
            boolean result = ferryWaitQueue.offer(vehicle);
            if (result) {
                LOGGER.info("Ferry: " + vehicle + " is added to wait queue");
            } else {
                LOGGER.info("Ferry: " + vehicle + " not added to wait queue");
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void ferryStartWork() {
        while (!ferryWaitQueue.isEmpty()) {
            loadVehiclesFromWaitQueueToFerry();
            moveFerryToAnotherSide();
            unloadVehiclesFromFerry();
        }
    }

    private void loadVehiclesFromWaitQueueToFerry() {
        reentrantLock.lock();
        try {
            LOGGER.info("LOADING FERRY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for (Vehicle vehicle : ferryWaitQueue) {
                LOGGER.info("Ferry is trying to load vehicle: " + vehicle);
                loadVehicleOnFerry(vehicle);
            }
            LOGGER.info("FERRY IS LOADED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void loadVehicleOnFerry(Vehicle vehicle) {
        if (isFerryNotLoaded.get()) {
            if (vehicle.getArea() < ferryFreeAreaInSquareMeters.get() & vehicle.getWeight() < freeFerryWeightCapacity.get()) {
                ferryVehicleOnBoard.offer(vehicle);
                registerChangOfFreeSpaceFerry(vehicle);
                vehicle.nextState();
                LOGGER.info("Ferry: " + Thread.currentThread().getName() + vehicle + " is loaded to ferry.");
                ferryWaitQueue.remove(vehicle);
            }
        } else {
            isFerryNotLoaded.getAndSet(false);
            LOGGER.info(vehicle + " is not loaded");
        }
    }

    private void registerChangOfFreeSpaceFerry(Vehicle vehicle) {
        int tempFreeBeringAreaCapacity = freeFerryWeightCapacity.get() - vehicle.getWeight();
        int tempFreeArea = ferryFreeAreaInSquareMeters.get() - vehicle.getArea();
        ferryFreeAreaInSquareMeters.getAndSet(tempFreeArea);
        freeFerryWeightCapacity.getAndSet(tempFreeBeringAreaCapacity);
        checkOnFullLoadFerry(tempFreeBeringAreaCapacity, tempFreeArea);
    }

    private void checkOnFullLoadFerry(int tempFreeBeringAreaCapacity, int tempFreeArea) {
        if ((tempFreeArea < ferryAreaInSquareMeters.get() * COEFFICIENT)
                || (tempFreeBeringAreaCapacity < ferryWeightCapacity.get() * COEFFICIENT)) {
            isFerryNotLoaded.getAndSet(false);
        }
    }

    private void moveFerryToAnotherSide() {
        LOGGER.info("Moving ferry to another side!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Vehicle vehicle : ferryVehicleOnBoard) {
            vehicle.nextState();
        }
    }

    private void unloadVehiclesFromFerry() {
        LOGGER.info("UNLOADING CARS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Vehicle vehicle : ferryVehicleOnBoard) {
            LOGGER.info("Ferry:  " + Thread.currentThread().getName() + vehicle + " can unloaded from ferry.");
            vehicle.nextState();
            ferryVehicleOnBoard.remove(vehicle);
            if (ferryVehicleOnBoard.isEmpty()) {
                ferryFreeAreaInSquareMeters = new AtomicInteger(ferryAreaInSquareMeters.get());
                freeFerryWeightCapacity = new AtomicInteger(ferryWeightCapacity.get());
                LOGGER.info("Ferry is free!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
        isFerryNotLoaded.getAndSet(true);
    }

    public static void setFerryAreaInSquareMeters(AtomicInteger ferryAreaInSquareMeters) {
        Ferry.ferryAreaInSquareMeters = ferryAreaInSquareMeters;
    }

    public static void setFerryWeightCapacity(AtomicInteger ferryWeightCapacity) {
        Ferry.ferryWeightCapacity = ferryWeightCapacity;
    }
}
