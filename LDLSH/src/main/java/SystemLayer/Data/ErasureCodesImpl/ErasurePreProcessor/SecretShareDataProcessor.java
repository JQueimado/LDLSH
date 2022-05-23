package SystemLayer.Data.ErasureCodesImpl.ErasurePreProcessor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecretShareDataProcessor extends ErasureDataProcessor {

    private static String ALGORITHM = "AES";
    private static String CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    private SecretKey key;
    private byte[] init_vector;

    private void createAESKey( SecureRandom secureRandom ) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256, secureRandom);
        key = keyGenerator.generateKey();
    }

    private void setInitializationVector( SecureRandom secureRandom ){
        init_vector = new byte[16];
        secureRandom.nextBytes(init_vector);
    }

    private void setup( String seed ){
        SecureRandom secureRandom = new SecureRandom(seed.getBytes(StandardCharsets.UTF_8)); //Create pseudo random

        //Create key
        try {
            createAESKey(secureRandom);
        }catch (NoSuchAlgorithmException nsae){
            System.out.println("Invalid Cypher algorithm");
        }

        //Create init Vector
        setInitializationVector(secureRandom);
    }

    public SecretShareDataProcessor( String seed ) {
        setup(seed);
    }

    @Override
    public byte[] preProcess(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(init_vector);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            return cipher.doFinal(data);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] postProcess(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(init_vector);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            return cipher.doFinal(data);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
