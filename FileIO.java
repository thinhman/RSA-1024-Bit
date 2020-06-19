import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileIO {
    public FileIO() {

    }

    public static void testFilePath(File filepath, String fileType){
        if(fileType.equals("Source") || fileType.equals("Key")){
            try{
                if(filepath.exists()){
                    if(!filepath.isDirectory()){
                        if(!filepath.isFile())
                            throw new Exception(fileType + " file path does not lead to a file");
                    }else
                        throw new Exception(fileType + " file path does not lead to a file");
                }else
                    throw new Exception(fileType + " file does not exist");
            }catch (Exception ex){
                System.err.println(ex + ", Exiting.");
                System.exit(1);
            }
        }else{
            try{
                if(filepath.isDirectory())
                    throw new Exception(fileType + " file path is a directory");
            }catch (Exception ex){
                System.err.println(ex + ", Exiting.");
                System.exit(1);
            }
        }




    }

    public static void storeKeys(String filepath, BigInteger n, BigInteger e, BigInteger d){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
            writer.write(n.toString() + '\n');
            writer.write(e.toString() + '\n');
            writer.write(d.toString() + '\n');
            writer.close();

        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }

    }

    public static ArrayList<String> readKeys(String filepath){
        ArrayList<String> keys = new ArrayList<>(3);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String n = reader.readLine();
            keys.add(n);
            String e = reader.readLine();
            if(!e.equals("65537")){
                throw new Exception();
            }
            keys.add(e);
            String d = reader.readLine();
            keys.add(d);

            reader.close();
        }catch (Exception ex){
            System.err.println("There is an issue with the given key file, Exiting");
            System.exit(1);
        }
        return keys;
    }

    public static void saveDecryptedMessage(String filePath, BigInteger message){
        byte[] chars = message.toByteArray();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for(byte c: chars){
                writer.write(c);
            }

            writer.close();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void saveEncryptedMessage(String encryptFilePath, BigInteger ciphertext){

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(encryptFilePath));
            writer.write(ciphertext.toString());
            writer.close();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static String readMessage(String sourceFilePath) {
        String message = "";
        try {
            message = new String(Files.readString(Paths.get(sourceFilePath)));
            //System.out.println(message);
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
        return message;
    }

}
