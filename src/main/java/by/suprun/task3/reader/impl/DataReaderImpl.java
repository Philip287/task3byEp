package by.suprun.task3.reader.impl;

import by.suprun.task3.reader.DataReader;
import by.suprun.task3.validator.impl.FileValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataReaderImpl implements DataReader {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public List<String> readData(String path) {
        logger.info("Read file with name: " + path);
        if (!new FileValidatorImpl().validateFile(path)) {
            logger.error("Incorrect file");
        }
        ArrayList<String> lines = new ArrayList<>();
        Path pathFile = Paths.get(path);
        try (Stream<String> lineStream = Files.lines(pathFile)) {
            lines = lineStream.collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            logger.error("Reading fail is fail", e);
        }
        return lines;
    }
}
