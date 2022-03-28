package by.suprun.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class Ferry {
    private static final Logger logger = LogManager.getLogger();
    private static final int DEFAULT_MAX_OF_AREA = 130;
    private static final int DEFAULT_MAX_OF_BEARING_CAPACITY = 25000;
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
            logger.info("Created ferry with default parameters area = " + area + " bearing capacity = "
                    + bearingCapacity);
        }
        freeArea = new AtomicInteger(area.get());
        freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
    }

    public void loadVehicleToWaitQueue(Vehicle vehicle) {
        reentrantLock.lock();
        boolean result = waitQueue.offer(vehicle);
        if (result) {
            logger.info(vehicle.toString() + "is added to wait");
        }
        reentrantLock.unlock();
    }

    public boolean loadVehicleToFerryAndTransport(Vehicle vehicle) {
        reentrantLock.lock();
        boolean result = false;
        if (waitQueue.contains(vehicle)) {
            logger.info("Try load: " + Thread.currentThread().getName());
            try {
                if (isLoading.get()) {
                    if (vehicle.getArea() < freeArea.get() & vehicle.getWeight() < freeBearingCapacity.get()) {
                        int tempFreeBeringAreaCapacity = freeBearingCapacity.get() - vehicle.getWeight();
                        int tempFreeArea = freeArea.get() - vehicle.getArea();
                        freeArea.getAndSet(tempFreeArea);
                        freeBearingCapacity.getAndSet(tempFreeBeringAreaCapacity);
                        result = ferryQueue.offer(vehicle);
                        if (result) {
                            logger.info(Thread.currentThread().getName() + vehicle.toString() + " is loaded to ferry.");
                            waitQueue.remove(vehicle);
                        }
                        if ((tempFreeArea < area.get() * 0.1)
                                || (tempFreeBeringAreaCapacity < bearingCapacity.get() * 0.1)) {
                            isLoading.getAndSet(false);
                        }
                    } else {
                        isLoading.getAndSet(false);
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return result;
    }

    public boolean runToUnload(Vehicle vehicle) {
        reentrantLock.lock();
        boolean result = false;
        try {
            if (ferryQueue.contains(vehicle)) {
                if (!isLoading.get()) {
                    logger.info("Car: " + vehicle.toString() + " is unload");
                    result = ferryQueue.remove(vehicle);
                    if (ferryQueue.isEmpty()) {
                        freeArea = new AtomicInteger(area.get());
                        freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
                        logger.info("Ferry is unload");
                    }
                }
            }
        } finally {
            reentrantLock.unlock();
        }
        return result;
    }
}