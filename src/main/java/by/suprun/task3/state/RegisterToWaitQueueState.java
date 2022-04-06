package by.suprun.task3.state;

import by.suprun.task3.entity.Ferry;
import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class RegisterToWaitQueueState implements VehicleState {
    private static final Logger LOGGER = LogManager.getLogger();

    public RegisterToWaitQueueState(Vehicle vehicle) {
        try {
            TimeUnit.SECONDS.sleep(1);
            Ferry.getFerryInstance().loadVehicleToFerryWaitQueue(vehicle);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void next(Vehicle vehicle) {
        vehicle.setState(new StartLoadToFerryState());
        LOGGER.info(vehicle + " is started to load on ferry.");
    }
}
