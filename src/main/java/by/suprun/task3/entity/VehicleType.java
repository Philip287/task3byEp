package by.suprun.task3.entity;

import java.util.concurrent.atomic.AtomicInteger;

public enum VehicleType {
    CAR(new AtomicInteger(7), new AtomicInteger(1000)),
    TRUCK(new AtomicInteger(14), new AtomicInteger(3500));

    private AtomicInteger areaInSquareMeters;
    private AtomicInteger weightInKilogram;

    VehicleType(AtomicInteger area, AtomicInteger weight) {
        this.areaInSquareMeters = area;
        this.weightInKilogram = weight;
    }

    public AtomicInteger getAreaInSquareMeters() {
        return areaInSquareMeters;
    }

    public AtomicInteger getWeightInKilogram() {
        return weightInKilogram;
    }
}
