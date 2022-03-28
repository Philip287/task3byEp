package by.suprun.task3.entity;

import java.util.concurrent.atomic.AtomicInteger;

public enum VehicleType {
    CAR(new AtomicInteger(7), new AtomicInteger(1000)),
    TRUCK(new AtomicInteger(14), new AtomicInteger(3500));

    private AtomicInteger area;
    private AtomicInteger weight;

    VehicleType(AtomicInteger area, AtomicInteger weight) {
        this.area = area;
        this.weight = weight;
    }

    public AtomicInteger getArea() {
        return area;
    }

    public AtomicInteger getWeight() {
        return weight;
    }
}
