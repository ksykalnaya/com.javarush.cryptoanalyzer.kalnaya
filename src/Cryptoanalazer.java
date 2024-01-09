import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Cryptoanalazer {
    public static final List<Character> ALPHABET = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',', '«', '»', '"', '\'',
            ':', '!', '?', ' ');
    public static void main(String[] args) throws IOException {

        System.out.println("Выберите режим работы:");
        System.out.println("1. Шифровка");
        System.out.println("2. Расшифровка с помощью ключа");
        System.out.println("3. Расшифровка с помощью перебора");
        System.out.println("exit");

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()){
            if(scanner.hasNextInt()){
                int type = scanner.nextInt();
                switch (type){
                    case(1)-> {
                        try{
                            Encryption.doEncrypt();
                        } catch (CharNotFindInAlphabetException e){
                            e.printStackTrace();
                        }finally {
                            return;
                        }
                    }
                    case(2) -> {
                        try{
                            Decryption.doDecrypt();
                        } catch (CharNotFindInAlphabetException e){
                            e.printStackTrace();
                        }finally {
                            return;
                        }
                    }
                    case(3) -> {BruteForce.doBruteForce(); return;}
                    default -> System.out.println("Режим работы не найден.Попробуйте еще раз");
                }
            }else if(scanner.nextLine().toLowerCase().equals("exit")){
                break;
            }else{
                System.out.println("Введите число");
            }

        }

    }
}
