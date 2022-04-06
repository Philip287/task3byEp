package by.suprun.task3.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DataReader {
    public static Map<String, Integer> readFileToParametersForFerry(String fileLocation) {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle(fileLocation);
        String ferryArea = rb.getString("default_ferry_area");
        String ferryBearingCapacity = rb.getString("default_ferry_bearing_capacity");
        stringIntegerMap.put("MAX_OF_FERRY_AREA_IN_SQUARE_METERS", Integer.parseInt(ferryArea));
        stringIntegerMap.put("MAX_OF_FERRY_WEIGHT_CAPACITY", Integer.parseInt(ferryBearingCapacity));
        return stringIntegerMap;
    }
}
