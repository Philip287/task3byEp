package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;

public class WaitEndCrossingState extends AbstractVehicleState {

    WaitEndCrossingState(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void next() {
        vehicle.transportVehicle();
        vehicle.changeState(new UnloadFromFerryState(vehicle));
    }
}
