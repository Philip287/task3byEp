package by.suprun.task3.reader;

import by.suprun.task3.exception.MultithreadingException;

import java.util.List;

public interface DataReader {
    public List<String> readData(String path);
}
