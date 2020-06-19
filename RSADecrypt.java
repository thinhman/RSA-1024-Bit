import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

public class RSADecrypt {

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
        BigInteger privateKeyExponent = new BigInteger(RSAKeys.get(2));

        BigInteger ciphertext = new BigInteger(FileIO.readMessage(source_file_path.toString()));
        BigInteger message = DecryptMsg(ciphertext, privateKeyExponent, mod);

        FileIO.saveDecryptedMessage(destination_file_path.toString(), message);
        System.out.println("Finished Decryption");
    }

    private static BigInteger DecryptMsg(BigInteger ciphertext, BigInteger privateKeyExponent, BigInteger mod){
        BigInteger message = ciphertext.modPow(privateKeyExponent, mod);
        return message;
    }
}