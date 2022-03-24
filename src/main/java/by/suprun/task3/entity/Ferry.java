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
        if (area == null || bearingCapacity == null) {
            area = new AtomicInteger(157);
            bearingCapacity = new AtomicInteger(2000);
            logger.info("Created ferry with default parameters area = " + area + " bearing capacity = " + bearingCapacity);
        }
        freeArea = new AtomicInteger(area.get());
        freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
    }

    public boolean loadVehicle(Vehicle vehicle) {
        reentrantLock.lock();
        logger.info("Try load: " + Thread.currentThread().getName());
        boolean result = false;
        try {
            if (isLoading.get()) {
                if (vehicle.getArea() < freeArea.get() & vehicle.getWeight() < freeBearingCapacity.get()) {
                    int tempFreeBeringAreaCapacity = freeBearingCapacity.get() - vehicle.getWeight();
                    int tempFreeArea = freeArea.get() - vehicle.getArea();
                    freeArea.getAndSet(tempFreeArea);
                    freeBearingCapacity.getAndSet(tempFreeBeringAreaCapacity);
                    result = ferryQueue.offer(vehicle);
                    logger.info(Thread.currentThread().getName() + vehicle.toString() + " is loaded to queue.");
                    if ((tempFreeArea < area.get() * 0.1)
                            || (tempFreeBeringAreaCapacity < bearingCapacity.get() * 0.1)) {
                        isLoading.getAndSet(false);
                    }
                } else {
                    isLoading.getAndSet(false);
                }
            } else waitQueue.offer(vehicle);
        } finally {
            reentrantLock.unlock();
        }
        return result;
    }

    public boolean runToUnload() {
        reentrantLock.lock();
        boolean result = false;
        try {
            if (!isLoading.get()) {
                for (Vehicle vehicle : ferryQueue) {
                    logger.info("Car: " + vehicle.toString() + " is unload");
                    ferryQueue.remove(vehicle);
                }
                isLoading.getAndSet(true);
                freeArea = new AtomicInteger(area.get());
                freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
                result = true;
                logger.info("ferry is unload");
            }
        } finally {
            reentrantLock.unlock();
        }
        return result;

    }
}