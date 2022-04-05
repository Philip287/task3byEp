package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;

public class StartLoadToFerryState extends AbstractVehicleState {

    public StartLoadToFerryState(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void next() {
        vehicle.vehicleStartLoadToFerry();
        vehicle.changeState(new WaitEndCrossingState(vehicle));
    }
}
