package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterToWaitQueueState extends AbstractVehicleState {
    private static final Logger logger = LogManager.getLogger();

    public RegisterToWaitQueueState(Vehicle vehicle) {
        super(vehicle);
        vehicle.addVehicleToWaitQueueFerry();
    }

    @Override
    public void next() {
        vehicle.changeState(new StartLoadToFerryState(vehicle));
        vehicle.startLoadVehicle(vehicle);
    }

    @Override
    public void prev() {
        vehicle.changeState(new RegisterToWaitQueueState(vehicle));
    }

    @Override
    public void printStatus() {
        logger.info(vehicle + " is add to wait queue ferry");
    }
}