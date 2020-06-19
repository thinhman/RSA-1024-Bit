import java.io.File;
import java.util.ArrayList;
import java.math.BigInteger;

public class RSAEncrypt {

    public static void main(String[] args) {
        File source_file_path = null;
        File destination_file_path = null;
        File key_file_path = null;

        try{
            source_file_path = new File(args[0]);
            destination_file_path = new File(args[1]);
            key_file_path = new File(args[2]);
        }catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("Error with the given file paths! Exiting.");
            System.err.println(ex);
            System.exit(1);
        }

        FileIO.testFilePath(source_file_path, "Source");
        FileIO.testFilePath(destination_file_path, "Destination");
        FileIO.testFilePath(source_file_path, "Key");

        ArrayList<String> RSAKeys;
        RSAKeys = FileIO.readKeys(key_file_path.toString());

        BigInteger mod = new BigInteger(RSAKeys.get(0));
        BigInteger publicKeyExponent = new BigInteger(RSAKeys.get(1));

        String message = FileIO.readMessage(source_file_path.toString());
        BigInteger ciphertext = EncryptMsg(message, publicKeyExponent, mod);

        FileIO.saveEncryptedMessage(destination_file_path.toString(), ciphertext);
        System.out.println("Encryption Done!");
    }


    public static BigInteger EncryptMsg(String message, BigInteger publicKeyExponent, BigInteger mod){
        BigInteger CipherText = new BigInteger(message.getBytes()).modPow(publicKeyExponent, mod);
        return CipherText;
    }

}