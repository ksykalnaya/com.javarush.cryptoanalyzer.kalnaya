import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Encryption {

    public static void doEncrypt() throws IOException,CharNotFindInAlphabetException {
        Scanner scanner = new Scanner(System.in);
        String source = fillPath(scanner,"Введите путь к файлу, который нужно зашифровать");
        String target = fillPath(scanner,"Введите путь к файлу, куда сохранить");
        int key = fillKey(scanner,"Введите ключ");

        try(FileReader reader = new FileReader(source);
            FileWriter writer = new FileWriter(target)){

            char[] buffer = new char[65536];
            while(reader.ready()){
                int real = reader.read(buffer);
                for (int i = 0; i < real; i++) {
                    char sign = buffer[i];
                    int index = Cryptoanalazer.ALPHABET.indexOf(Character.toLowerCase(sign));
                    if(index >= 0){
                        index = (index + key) % Cryptoanalazer.ALPHABET.size();
                        buffer[i] = Cryptoanalazer.ALPHABET.get(index);
                    }else{
                        if(sign != '\n'){
                            throw new CharNotFindInAlphabetException("Символ " + sign + " не найден! В файле " + source);
                        }
                    }
                }
                writer.write(buffer,0,real);
            }
        }
        System.out.println("Шифрование прошло успешно");
    }

    private static String fillPath(Scanner scanner,String message){
        System.out.println(message);
        String pathString = "";
        Path path = null;

        while(true){
            if(scanner.hasNextLine()){
                pathString = scanner.nextLine();
                path = Path.of(pathString);
                if (pathString.equals("")) {
                    System.out.println("Ничего не введено. Введите путь к файлу");
                } else if (!Files.exists(path) || !Files.isRegularFile(path)) {
                    System.out.println("Файл не найден. Введите путь к файлу");
                } else {
                    break;
                }
            }else{
                scanner.next();
                System.out.println("Введите путь к файлу");
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
