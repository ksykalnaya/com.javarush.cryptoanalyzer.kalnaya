package cryptoanalazer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Encryption {

    public static void doEncrypt() throws IOException, CharNotFindInAlphabetException {
        Scanner scanner = new Scanner(System.in);

        String source = fillFilePath(scanner,"Введите путь к исходному файлу",true,false);
        String target = fillFilePath(scanner,"Введите путь к директории, куда сохранить",false,true);
        int key = fillKey(scanner,"Введите ключ");

        //Создание файла с расшифровкой
        Path targetPath = Path.of(target).resolve(Path.of(source).getFileName() + "_result");
        if(Files.exists(targetPath)){
            Files.delete(targetPath);
        }
        Files.createFile(targetPath);

        try(FileReader reader = new FileReader(source);
            FileWriter writer = new FileWriter(targetPath.toString())){

            char[] buffer = new char[65536];
            boolean upperCaseFlg = false;
            while(reader.ready()){
                int real = reader.read(buffer);
                for (int i = 0; i < real; i++) {
                    char sign = buffer[i];
                    upperCaseFlg = Character.isUpperCase(sign) ? true : false;
                    int index = Cryptoanalazer.ALPHABET.indexOf(Character.toLowerCase(sign));
                    if(index >= 0){
                        index = (index + key) % Cryptoanalazer.ALPHABET.size();
                        sign = Cryptoanalazer.ALPHABET.get(index);
                        buffer[i] = upperCaseFlg == true ? Character.toUpperCase(sign) : sign;
                    }else{
                        if(sign != '\n'){
                            throw new CharNotFindInAlphabetException("Символ " + sign + " не найден! В файле " + source);
                        }
                    }
                }
                writer.write(buffer,0,real);
            }
        }
        System.out.println("Операция прошла успешно");
    }

    private static String fillFilePath(Scanner scanner,String message, boolean sourcePath,boolean targetPath){
        System.out.println(message);
        String pathString = "";
        Path path = null;

        while(true){
            if(scanner.hasNextLine()){
                pathString = scanner.nextLine();
                path = Path.of(pathString);
                if (pathString.equals("")) {
                    System.out.println("Ничего не введено. " + message);
                } else if (!Files.exists(path) || (!Files.isRegularFile(path) && sourcePath) || (!Files.isDirectory(path) && targetPath)) {
                    System.out.println("Путь не найден. " + message);
                } else {
                    break;
                }
            }else{
                scanner.next();
                System.out.println(message);
            }

        }
        return pathString;
    }

    private static int fillKey(Scanner scanner,String message){
        System.out.println(message);
        int key = 0;
        while(true){
            if(scanner.hasNextInt()){
                key = scanner.nextInt();
                if (key < 0) {
                    System.out.println("Ключ должен быть положительным.");
                } else if (key > Cryptoanalazer.ALPHABET.size()) {
                    int x = key / Cryptoanalazer.ALPHABET.size();
                    key = key - Cryptoanalazer.ALPHABET.size() * x;
                    break;
                } else {
                    break;
                }
            }else {
                scanner.next();
                System.out.println("Ключ должен быть натуральным числом");
            }
        }
        return key;
    }

    /*
    private static String fillTargetFileDirectory(Scanner scanner,String message){
        System.out.println(message);
        String dirString = "";
        Path path = null;

        while(true){
            if(scanner.hasNextLine()){
                dirString = scanner.nextLine();
                path = Path.of(dirString);
                if (dirString.equals("")) {
                    System.out.println("Ничего не введено. Введите путь к директории");
                } else if (!Files.exists(path) || !Files.isDirectory(path)) {
                    System.out.println("Директория не найдена. Введите путь к директории");
                } else {
                    break;
                }
            }else{
                scanner.next();
                System.out.println("Введите путь к директории");
            }

        }
        return dirString;
    } */
}
