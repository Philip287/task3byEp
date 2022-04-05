package by.suprun.task3.parser.impl;

import by.suprun.task3.parser.DataParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParserImpl implements DataParser {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NUMBER_REGEX = "[0-9]+";
    private static final Pattern pattern = Pattern.compile(NUMBER_REGEX);
    private int ferryBearingCapacity;
    private int ferryArea;

    @Override
    public void parser(List<String> list) {
        LOGGER.info("Start parsing data");
        Matcher matcherFerryBearingCapacity = pattern.matcher(list.get(0));

        if (matcherFerryBearingCapacity.find()) {
            ferryBearingCapacity = Integer.parseInt(list.get(0).substring(matcherFerryBearingCapacity.start(), matcherFerryBearingCapacity.end()));
        }

        Matcher matcherFerryArea = pattern.matcher(list.get(1));
        if (matcherFerryArea.find()) {
            ferryArea = Integer.parseInt(list.get(1).substring(matcherFerryArea.start(), matcherFerryArea.end()));
        }
    }

    @Override
    public int getFerryBearingCapacity() {
        LOGGER.info("Return ferry bearing capacity ");
        return ferryBearingCapacity;
    }

    @Override
    public int getFerryArea() {
        LOGGER.info("Return ferry area ");
        return ferryArea;
    }
}
