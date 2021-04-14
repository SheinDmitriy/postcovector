package main;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Задаем путь к файлам и запускаем метод осклейки данных
        Path path = Paths.get("rep");
        runMerge(path);
    }

    // Метод для склейки данных
    private static void runMerge(Path path){
        try {
            // Используем метод walkFileTree из java nio для обхода всех файлов в заданной дериктории
            Files.walkFileTree( path, Collections.singleton(FileVisitOption.FOLLOW_LINKS), 5,new MyFileVisit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
