package cryptoanalazer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Scanner;

public class BruteForce {
    private static Scanner scanner = new Scanner(System.in);
    public static void doBruteForce() throws IOException,CharNotFindInAlphabetException {


        String source = fillFilePath(scanner,"Введите путь к исходному файлу",true,false);
        String target = fillFilePath(scanner,"Введите путь к директории, куда сохранить",false,true);
        Collections.reverse(Cryptoanalazer.ALPHABET);

        //Создание файла с номером расшифровки
        Path targetPath = Path.of(target).resolve(Path.of(source).getFileName() + "_result");
        if(Files.exists(targetPath)){
            Files.delete(targetPath);
        }
        Files.createFile(targetPath);

        for (int key = 0; key < Cryptoanalazer.ALPHABET.size(); key++) {
            System.out.println("Расшифровка №" + key);

            try(FileReader reader = new FileReader(source);
                FileWriter writer = new FileWriter(targetPath.toString())){

                char[] buffer = new char[65536];
                boolean rightDecryption = false;
                while(reader.ready()){
                    int real = reader.read(buffer);
                    Encryption.changeSignPosition(buffer,key,real);

                    //Проверка расшифровки, если не подходит - идем к след варианту
                    rightDecryption = checkDecryption(buffer,real,rightDecryption);
                    if(rightDecryption)
                        writer.write(buffer,0,real);
                    else break;
                }
                if(rightDecryption){ break; }
            }
        }
        System.out.println("Операция прошла успешно");
    }

    private static boolean checkDecryption(char[] array,int size,boolean checkedBefore) throws IOException{
        if(checkedBefore){ return true; }

        try(BufferedReader bufferedReader = new BufferedReader(new CharArrayReader(array,0,size))){
            System.out.println(bufferedReader.readLine());
            System.out.println("Расшифровка верна? yes/no");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("yes")) {
                return true;
            }
        }
        return false;
    }

    private static String fillFilePath(Scanner scanner, String message, boolean isFile, boolean isDirectory){
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
}
