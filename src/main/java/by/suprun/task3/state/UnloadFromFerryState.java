package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnloadFromFerryState implements VehicleState {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void next(Vehicle vehicle) {
        LOGGER.info(vehicle + " is free and ready to go to city.");
    }
}
