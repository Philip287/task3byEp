package by.suprun.task3.state.impl;

import by.suprun.task3.entity.Vehicle;

public abstract class AbstractStateVehicle {

    private Vehicle vehicle;

    AbstractStateVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public abstract String vehicleRunning();


    public abstract String vehicleLoaded();


    public abstract String vehicleUnloaded();
}
