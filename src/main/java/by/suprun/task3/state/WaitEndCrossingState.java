package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitEndCrossingState extends AbstractVehicleState {
    private static final Logger logger = LogManager.getLogger();

    WaitEndCrossingState(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void next() {
        vehicle.waitEndCrossing();
        vehicle.changeState(new UnloadFromFerryState(vehicle));
    }

    @Override
    public void prev() {
        logger.info("You cant load vehicle in ferry at this moment!");
    }

    @Override
    public void printStatus() {
        logger.info(vehicle + " is wait the end of crossing");
    }
}
