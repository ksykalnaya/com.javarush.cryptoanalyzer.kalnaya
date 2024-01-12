package cryptoanalazer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Scanner;

public class BruteForce {
    public static void doBruteForce() throws IOException,CharNotFindInAlphabetException {
        Scanner scanner = new Scanner(System.in);

        String source = fillFilePath(scanner,"Введите путь к исходному файлу",true,false);
        String target = fillFilePath(scanner,"Введите путь к директории, куда сохранить",false,true);
        Collections.reverse(Cryptoanalazer.ALPHABET);

        for (int key = 0; key < Cryptoanalazer.ALPHABET.size(); key++) {

            //Создание файла с номером расшифровки
            Path targetPath = Path.of(target).resolve(Path.of(source).getFileName() + "_result_" + key);
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
                        if (index >= 0) {
                            index = (index + key) % Cryptoanalazer.ALPHABET.size();
                            sign = Cryptoanalazer.ALPHABET.get(index);
                            buffer[i] = upperCaseFlg == true ? Character.toUpperCase(sign) : sign;
                        } else {
                            if (sign != '\n') {
                                throw new CharNotFindInAlphabetException("Символ " + sign + " не найден! В файле " + source);
                            }
                        }
                    }
                    writer.write(buffer,0,real);
                }
            }
        }
        System.out.println("Операция прошла успешно");
    }

    private static String fillFilePath(Scanner scanner, String message, boolean sourcePath, boolean targetPath){
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
}
