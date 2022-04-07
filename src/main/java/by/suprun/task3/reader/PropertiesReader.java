package by.suprun.task3.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertiesReader {
    private static final String AREA_PARAMETER = "default_ferry_area_in_square_meters";
    private static final String WEIGHT_CAPACITY = "default_ferry_weight_capacity";

    public static Map<String, Integer> readPropertiesParametersForFerry(String fileLocation) {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle(fileLocation);
        String ferryArea = rb.getString(AREA_PARAMETER);
        String ferryBearingCapacity = rb.getString(WEIGHT_CAPACITY);
        stringIntegerMap.put("default_ferry_area_in_square_meters", Integer.parseInt(ferryArea));
        stringIntegerMap.put("default_ferry_weight_capacity", Integer.parseInt(ferryBearingCapacity));
        return stringIntegerMap;
    }
}
