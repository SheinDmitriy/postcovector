package main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyFileVisit extends SimpleFileVisitor<Path> {

    private WriteFile writeFile = new WriteFile();
//    private ParseCSVFile parseCSVFile = new ParseCSVFile();
    private InputStream inputStream = null;


    private static XSSFWorkbook workBook = null;
    private static List<String> listSHPI = new ArrayList<>();



    // Заполняем HashMap клюяами и пустыми массивами


    // Метод который обходит и выболняет действия по очереди с каждым файлом в задвнной директории
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        String fileName = path.getFileName().toString();
//        System.out.println("Обрабатываем файл: " + fileName);

//        if (attrs.isDirectory()){
//            FileSystem fs = FileSystems.newFileSystem(path, null);
//            Path dirPath = fs.getPath("/");
//
//            // Запускаем обход по "zip-папке"
//            try {
//                Files.walkFileTree(dirPath, new MyFileVisit());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        // Выявляем расширение файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
            // В зависимости от расширения запускаем соответсвующий парсер данных
            switch (fileName.substring(fileName.lastIndexOf(".")+1)){
                case "xlsx" :
                    roundLineInCVSFile(path);
                    break;
//                case "zip" :
//                    roundFileInZIPFolder(path);
//                    break;
            }
        }
//        else {
//            System.out.println("У файла " + fileName + " нет расширения");
//        }
        return CONTINUE;
    }
    // Метод для парсера данных из ZIP файлв
//    public void roundFileInZIPFolder(Path path) throws IOException{
//        // Определяем zip файл как папку
//        FileSystem fs = FileSystems.newFileSystem(path, null);
//        Path zipPath = fs.getPath("/");
//
//        // Запускаем обход по "zip-папке"
//        try {
//            Files.walkFileTree(zipPath, new MyFileVisit());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // Метод для прасинга данных из cvs файлов
    public void roundLineInCVSFile(Path path) throws IOException{
        try {
            File file = new File("D:/proj/postcovector/" + path.getParent() + "/" + path.getFileName());

//            file.renameTo(new File("D:/proj/postcovector/" + path.getParent() + "/" + path.getFileName() + "x")); ------------ переименовать файл
//            file.createNewFile();
//            inputStream = new FileInputStream(file);


            workBook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()){
            Row row = it.next();
            Cell cell = row.getCell(0);
            if (!cell.getStringCellValue().equals("NUM")){
                listSHPI.add(cell.getStringCellValue());
            }
        }


//        // Считываем данные из файла в массив строк
//        List<String> lines = Files.readAllLines(path);
//        // Обходим каждую строку отдельно
//        for (String s: lines) {
//            parseCSVFile.parseLineFromCSV(s, dataForMerge);
//        }
//        System.out.println(dataForMerge);
    }

    // Метод для выполнения действий после завершения обхода директории
    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
        // Если закончили обход "zip-папки" то ничего не делаем, просто завершаем обход
        if(path.toString().equals("/")){
            return CONTINUE;
        }
        // Обробатываем данные в массивах HashMap для формирования трех типов отчетов
//        System.out.println("Формируем данные для отчета");
//        // Обходим каждый элемент HashMap
//        dataForMerge.forEach((k, v) ->{
//            // Проверяем пустой ли массив
//            if(!v.isEmpty()){
//                // Сортируем данные в массиве для 3 отчета
//                Collections.sort(v, Collections.reverseOrder());
//                dataForReport3.put(k,v);
//                // Обходим массивы и складываем значения внутри для отчета 1 и 2
//                int count = 0;
//                for (Integer s: v) {
//                    count = count + s;
//                }
//                dataForReport1.put(k, count);
//                dataForReport2.put(k, count);
//            }else {
//                // Добавляем пустые массивы для отчета 2
//                dataForReport2.put(k, null);
//            }
//        });
        // Записываем данные в три разных файла
//        System.out.println(dataForMerge);
        writeFile.jsonWriteFile(listSHPI, "shpi.txt");
//        System.out.println(dataForReport3.toString());
//        System.out.println("Формируем файл для отчета №1");
//        writeFile.jsonWriteFile(dataForReport1, "report №1.json");
//        System.out.println(dataForReport2.toString());
//        System.out.println("Формируем файл для отчета №2");
//        writeFile.jsonWriteFile(dataForReport2, "report №2.json");

        return CONTINUE;
    }


}
