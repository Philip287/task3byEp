package by.suprun.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class Ferry {
    private static final Logger logger = LogManager.getLogger();
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
    private Phaser phaser;

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
        phaser = new Phaser();
        ferryQueue = new ArrayDeque<>();
        waitQueue = new ArrayDeque<>();
        if (area == null || bearingCapacity == null) {
            area = new AtomicInteger(DEFAULT_MAX_OF_AREA);
            bearingCapacity = new AtomicInteger(DEFAULT_MAX_OF_BEARING_CAPACITY);
            logger.info("Created ferry with default parameters area = " + area + " bearing capacity = "
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
                logger.info(vehicle.toString() + "is added to wait queue");
            } else {
                logger.info(vehicle.toString() + " not added to wait queue");
            }
        } catch (InterruptedException e) {
            logger.error(e);
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean loadVehicleToFerryAndTransport() {
        boolean result = false;
        for (Vehicle vehicle : waitQueue) {
            logger.info("Try load: " + vehicle);
                if (isLoading.get()) {
                    if (vehicle.getArea() < freeArea.get() & vehicle.getWeight() < freeBearingCapacity.get()) {
                        int tempFreeBeringAreaCapacity = freeBearingCapacity.get() - vehicle.getWeight();
                        int tempFreeArea = freeArea.get() - vehicle.getArea();
                        freeArea.getAndSet(tempFreeArea);
                        freeBearingCapacity.getAndSet(tempFreeBeringAreaCapacity);
                        vehicle.getState().next();
                        result = ferryQueue.offer(vehicle);
                        if (result) {
                            logger.info(Thread.currentThread().getName() + vehicle.toString() + " is loaded to ferry.");
                            waitQueue.remove(vehicle);
                        }
                        if ((tempFreeArea < area.get() * COEFFICIENT)
                                || (tempFreeBeringAreaCapacity < bearingCapacity.get() * COEFFICIENT)) {
                            isLoading.getAndSet(false);
                        }
                    } else {
                        isLoading.getAndSet(false);
                    }
                }
            }
        return result;
    }

    public boolean runToUnload() {
        boolean result = false;
            for (Vehicle vehicle : ferryQueue) {
                vehicle.getState().next();
                if (!isLoading.get()) {
                    vehicle.getState().next();
                    vehicle.getState().printStatus();
                    result = ferryQueue.remove(vehicle);
                    if (ferryQueue.isEmpty()) {
                        freeArea = new AtomicInteger(area.get());
                        freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
                        logger.info("Ferry is unload");
                    }
                }
            }
        return result;
    }
}