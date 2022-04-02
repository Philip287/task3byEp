package by.suprun.task3.state;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnloadFromFerryState extends AbstractVehicleState {
    private static final Logger logger = LogManager.getLogger();

    public UnloadFromFerryState(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void next() {
        vehicle.startUnloadVehicle();
        logger.info(vehicle + " incoming at destination");
    }

    @Override
    public void prev() {
        logger.info(vehicle + " incoming at destination");
    }

    @Override
    public void printStatus() {
        logger.info(vehicle + " start to unload from ferry");
    }
}
