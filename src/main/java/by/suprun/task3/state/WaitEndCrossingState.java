package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class WaitEndCrossingState implements VehicleState {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void next(Vehicle vehicle) {
        try {
            TimeUnit.SECONDS.sleep(1);
            vehicle.setState(new UnloadFromFerryState());
            LOGGER.info(vehicle + " is unloading from ferry now.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
