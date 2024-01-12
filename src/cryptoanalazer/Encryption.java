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
            while(reader.ready()){
                int real = reader.read(buffer);
                changeSignPosition(buffer,key,real);
                writer.write(buffer,0,real);
            }
        }
        System.out.println("Операция прошла успешно");
    }

    public static void changeSignPosition(char[] array, int shift,int size) throws CharNotFindInAlphabetException{
        for (int i = 0; i < size; i++) {
            char sign = array[i];
            boolean upperCaseFlg = Character.isUpperCase(sign);
            int index = Cryptoanalazer.ALPHABET.indexOf(Character.toLowerCase(sign));
            if(index >= 0){
                index = (index + shift) % Cryptoanalazer.ALPHABET.size();
                sign = Cryptoanalazer.ALPHABET.get(index);
                array[i] = upperCaseFlg ? Character.toUpperCase(sign) : sign;
            }else{
                if(sign != '\n'){
                    throw new CharNotFindInAlphabetException("Символ " + sign + " не найден!");
                }
            }
        }
    }

    private static String fillFilePath(Scanner scanner,String message, boolean isFile,boolean isDirectory){
        System.out.println(message);
        String pathString = "";

        while(true){
            if(scanner.hasNextLine()){
                pathString = scanner.nextLine();
                Path path = Path.of(pathString);
                if (pathString.equals("")) {
                    System.out.println("Ничего не введено. " + message);
                } else if (!Files.exists(path) || (!Files.isRegularFile(path) && isFile) || (!Files.isDirectory(path) && isDirectory)) {
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
}
