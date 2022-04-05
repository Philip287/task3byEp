package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;

public class RegisterToWaitQueueState extends AbstractVehicleState {

    public RegisterToWaitQueueState(Vehicle vehicle) {
        super(vehicle);
        vehicle.addVehicleToWaitQueueFerry();
    }

    @Override
    public void next() {
        vehicle.changeState(new StartLoadToFerryState(vehicle));
    }

}