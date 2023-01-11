package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.ErasureBlock;
import SystemLayer.Data.DataUnits.ErasureBlockImpl;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import SystemLayer.SystemExceptions.UnknownConfigException;
import com.codahale.shamir.Scheme;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecreteShareReadSolomonErasureCodes extends BackblazeReedSolomonErasureCodes {

    /**Class**/
    protected static final String random_seed_config_name ="SEED";
    protected static final String IV ="IV_SEED";
    protected static final String ALGORITHM = "CIPHER_ALGORITHM";         //test: "AES";
    protected static final String CIPHER_ALGORITHM = "CIPHER_CONFIG";     //test: "AES/CBC/PKCS5PADDING";

    protected static final int iv_size = 16;
    protected static final int key_size = 32; // in Bytes

    protected static Scheme secreteShareEncoder = null;

    protected static byte[] iv;
    protected static Cipher cipher;
    protected static KeyGenerator keyGenerator;
    protected static String algorithm = "";

    private static void setCipher( DataContainer appContext ) throws UnknownConfigException {
        //IV config set check
        String iv_seed = "";
        try{
            iv_seed = appContext.getConfigurator().getConfig( IV ); //Get seed from config
            if( iv_seed.length() != iv_size )
                throw new IllegalArgumentException();

            iv = iv_seed.getBytes(StandardCharsets.UTF_8);

        }catch (IllegalArgumentException e){
            throw new UnknownConfigException( IV, iv_seed );
        }

        //KeyGenerator config set check
        try {
            algorithm = appContext.getConfigurator().getConfig(ALGORITHM); //get config value
            keyGenerator = KeyGenerator.getInstance(algorithm); //create a Random key generator
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(key_size*8, secureRandom);
        } catch ( NoSuchAlgorithmException e ){
            throw new UnknownConfigException( ALGORITHM, algorithm );
        }

        //Cipher config set check
        String algorithm = "";
        try {
            algorithm = appContext.getConfigurator().getConfig( CIPHER_ALGORITHM ); //get cipher config
            cipher = Cipher.getInstance(algorithm); //create cipher object
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalArgumentException e){
            throw new UnknownConfigException( CIPHER_ALGORITHM, algorithm );
        }
    }

    private static void setSecreteShare( DataContainer appContext ) throws UnknownConfigException {
        if (secreteShareEncoder != null)
            return;

        n = appContext.getNumberOfBands();

        String t_value = "";
        //Parity blocks
        int t;
        try {
            t_value = appContext.getConfigurator().getConfig( fault_config_name );
            t = Integer.parseInt(t_value);
        }catch (Exception e){
            throw new UnknownConfigException( fault_config_name, t_value );
        }

        //Seed
        String seed = "";
        try {
            seed = appContext.getConfigurator().getConfig( random_seed_config_name );
        }catch (Exception e){
            throw new UnknownConfigException( random_seed_config_name, seed );
        }

        k = n- t;
        secreteShareEncoder = new Scheme( new SecureRandom(seed.getBytes(StandardCharsets.UTF_8)), n, k);
    }

    protected static void setupEncoders( DataContainer appContext ) throws UnknownConfigException {
       setCipher(appContext);
       setSecreteShare(appContext);
    }

    /**
     * Creates a random key using the key generator
     * @return random key
     */
    protected static SecretKey createKey(){
        return keyGenerator.generateKey();
    }

    /**
     * Creates a cipher text for a given plain text.
     * @param data plain text
     * @param key cipher key
     * @return cipher text
     */
    private static synchronized byte[] cipher( byte[] data, SecretKey key) throws
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    /**
     * Deciphers a cipher text for a given plain text
     * @param data cipher text
     * @param key cipher key
     * @return plain text
     */
    private static synchronized byte[] decipher( byte[] data, SecretKey key ) throws
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    /**Object**/
    public ShamirSecreteShareReadSolomonErasureCodes(DataContainer appContext) throws UnknownConfigException {
        super(appContext);
        setupEncoders(appContext);
    }

    @Override
    public void encodeDataObject(byte[] object, int n_blocks) throws Exception {

        SecretKey secretKey = createKey(); //Create a Cipher key
        byte[] cipheredObject = cipher( object, secretKey ); //Create cipher text for shard
        Map<Integer, byte[]> shares = secreteShareEncoder.split(secretKey.getEncoded()); //Create shares for the key
        byte[][] shards = createCodes(cipheredObject, k); //Create erasure codes for the cipher text

        erasureBlocks = new ErasureBlock[n];
        for(Integer i : shares.keySet()){
            erasureBlocks[i-1] = new SecreteShareErasureBlock(shards[i-1], i-1, shares.get(i));
        }
        number_of_blocks = n_blocks;
        total_blocks = n;
    }

    @Override
    public byte[] decodeDataObject()
            throws IncompleteBlockException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException {

        if( number_of_blocks < n-t ){
            throw new IncompleteBlockException();
        }

        //Calculate missing and
        //Assemble shards
        byte[][] dataMatrix = new byte[n][];
        Map<Integer, byte[]> keyMatrix = new HashMap<>();
        for( int c = 0; c<n; c++ ){
            SecreteShareErasureBlock current = (SecreteShareErasureBlock) erasureBlocks[c];
            if( current == null ){
                dataMatrix[c] = new byte[block_size];
            }else {
                dataMatrix[current.getPosition()] = current.getBlock();
                keyMatrix.put(current.getPosition()+1, current.share);
            }
        }

        //Data
        encoder.decodeMissing(dataMatrix,isPresent,0,block_size);
        int data_size = k*block_size;
        byte[] data = shardsToByteArray(dataMatrix, data_size);
        data = removePadding(data);

        //Key
        byte[] rawKey = secreteShareEncoder.join(keyMatrix);
        SecretKey secretKey = new SecretKeySpec(rawKey,algorithm);

        return decipher(data,secretKey);
    }

    /**ErasureBlock**/
    public static class SecreteShareErasureBlock extends ErasureBlockImpl {

        public byte[] share;

        public SecreteShareErasureBlock(byte[] block_data, int position, byte[] share) {
            super(block_data, position);
            this.share = share;
        }
    }
}
