package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartLoadToFerryState extends AbstractVehicleState {
    private static final Logger logger = LogManager.getLogger();

    public StartLoadToFerryState(Vehicle vehicle) {
        super(vehicle);
        vehicle.startLoadVehicle(vehicle);
    }

    @Override
    public void next() {
        vehicle.changeState(new WaitEndCrossingState(vehicle));
    }

    @Override
    public void prev() {
        vehicle.changeState(new RegisterToWaitQueueState(vehicle));
    }

    @Override
    public void printStatus() {
        logger.info(vehicle + "Start load to ferry");
    }
}