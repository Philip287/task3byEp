package by.suprun.task3.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ResourceBundle;
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
            ResourceBundle resourceBundle = ResourceBundle.getBundle("property.default_ferry");
            String ferryArea = resourceBundle.getString("default_ferry_area");
            String ferryBearingCapacity = resourceBundle.getString("default_ferry_bearing_capacity");
            Ferry.ferryConfiguration(Integer.parseInt(ferryArea), Integer.parseInt(ferryBearingCapacity));
            logger.info("Created ferry with default parameters area = " + area + " bearing capacity = " + bearingCapacity);
        }
        freeArea = new AtomicInteger(area.get());
        freeBearingCapacity = new AtomicInteger(bearingCapacity.get());
    }

    private static void ferryConfiguration(int ferryArea, int ferryBearingCapacity) {
        logger.info("Create ferry with parameters area = " + ferryArea + " bearing capacity = " + ferryBearingCapacity);
        bearingCapacity = new AtomicInteger(ferryBearingCapacity);
        area = new AtomicInteger(ferryArea);
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
                    logger.info(Thread.currentThread().getName() + " loaded");
                    if ((tempFreeArea < (int) (area.get() * 0.1))
                            || (tempFreeBeringAreaCapacity < (int) (bearingCapacity.get() * 0.1))) {
                        isLoading.getAndSet(false);
                    }
                } else {
                    isLoading.getAndSet(false);
                }
            }
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
                ferryQueue.clear();
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