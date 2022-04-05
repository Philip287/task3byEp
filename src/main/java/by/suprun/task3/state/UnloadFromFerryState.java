package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;

public class UnloadFromFerryState extends AbstractVehicleState {

    public UnloadFromFerryState(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void next() {
        vehicle.vehicleStartUnloadFromFerry();
    }

}
