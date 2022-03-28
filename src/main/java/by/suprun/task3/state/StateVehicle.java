package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;

public interface StateVehicle {

    String vehicleRunning(Vehicle vehicle);

    String vehicleLoaded(Vehicle vehicle);

    String vehicleUnloaded(Vehicle vehicle);
}
