package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;

public abstract class AbstractVehicleState {
    Vehicle vehicle;

    AbstractVehicleState(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public abstract void next();

}
