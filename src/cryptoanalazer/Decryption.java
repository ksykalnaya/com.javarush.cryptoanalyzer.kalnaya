package cryptoanalazer;

import java.io.*;
import java.util.Collections;

public class Decryption {
    public static void doDecrypt() throws IOException, CharNotFindInAlphabetException {
        Collections.reverse(Cryptoanalazer.ALPHABET);
        Encryption.doEncrypt();
    }
}