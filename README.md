# RSA-1024-Bit
RSA Encryption/Decryption with 1024 bit key.

Instructions to run program files:

1. Make sure you have a java compiler installed.

2. Open the command line

3. Change directory to folder that contains the code through the command line.

4. Once in the folder, compile all of the java files

	Windows Example:
		javac *java

5. Generate the RSA keys by running: "java RSAGenKey [filepath]"
	Example: java RSAGenKey ..\data\key.dat

6. Encrypt with RSA by running: "java RSAEncrypt [message (source) filepath] [encrypt (destination) filepath] [same key file generated in step 5 filepath]"

	Example: RSAEncrypt ..\data\message.txt ..\data\message.enc ..\data\key.dat

7. Finally Decrypt with RSA by running: "java RSADecrypt [same encrypted file generated in step 6 filepath] [decrypt (destination) filepath] [same key file generated in step 5 filepath]"

	Example: RSADecrypt ..\data\message.enc ..\data\decrypt_message.txt ..\data\key.dat
