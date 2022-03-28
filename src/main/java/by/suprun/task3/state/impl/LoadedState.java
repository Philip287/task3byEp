package by.suprun.task3.state.impl;

import by.suprun.task3.entity.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

public class LoadedState extends AbstractStateVehicle {
    private static final Logger logger = LogManager.getLogger();
    private static ReentrantLock reentrantLock = new ReentrantLock(true);

    LoadedState(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public String vehicleRunning( ) {
        reentrantLock.lock();

        reentrantLock.unlock();
        return "Blocked";
    }

    @Override
    public String vehicleLoaded( ) {
        reentrantLock.lock();
        //vehicle.changeState();
        reentrantLock.unlock();
        return "vehicle is loaded to ferry";
    }

    @Override
    public String vehicleUnloaded( ) {
        reentrantLock.lock();

        reentrantLock.unlock();
        return "Blocked";
    }
}
