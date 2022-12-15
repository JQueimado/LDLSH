package SystemLayer.Components.DataProcessor;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.CorruptDataException;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import SystemLayer.SystemExceptions.UnknownConfigException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SecretShareDataProcessor extends DataProcessorImpl{

    //Adicionar chave aos codigos
    private static final String IV ="IV_SEED";
    private static final String ALGORITHM = "CIPHER_ALGORITHM";         //test: "AES";
    private static final String CIPHER_ALGORITHM = "CIPHER_CONFIG";     //test: "AES/CBC/PKCS5PADDING";
    private static final int iv_size = 16;
    private static final int key_size = 32; // in Bytes

    //Objects
    private final byte[] iv;
    private final Cipher cipher;
    private final KeyGenerator keyGenerator;
    private String algorithm = "";

    /**
     * Standard Constructor
     * @param appContext application state
     */
    public SecretShareDataProcessor(DataContainer appContext) throws UnknownConfigException {
        super(appContext);

        //IV config set check
        String iv_seed = "";
        try{
            iv_seed = appContext.getConfigurator().getConfig( IV ); //Get seed from config
            if( iv_seed.length() != iv_size )
                throw new IllegalArgumentException();

            /*
            SecureRandom secureRandom = new SecureRandom( iv_seed.getBytes(StandardCharsets.UTF_8) ); //Create an PRNG
            //iv = new byte[iv_size]; //init array
            secureRandom.nextBytes(iv); //set array with de PRNG values
            */

            iv = iv_seed.getBytes(StandardCharsets.UTF_8);

        }catch (IllegalArgumentException e){
            throw new UnknownConfigException( IV, iv_seed );
        }

        //KeyGenerator config set check
        try {
            algorithm = appContext.getConfigurator().getConfig(ALGORITHM); //get config value
            keyGenerator = KeyGenerator.getInstance(algorithm); //create a Random key generator
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(key_size*8, secureRandom); //Key size must be in bits
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

        //int data_size = appContext.getObjectByteSize(); //TODO:Remove function adapt cipher to each object
        //int cypher_size = ( data_size/iv_size ) * ( iv_size + 1 );
        //appContext.setErasureCodesDataSize( cypher_size + key_size );
    }

    @Override
    public ProcessedData preProcessData(DataObject object) throws Exception {
        int n_bands = appContext.getNumberOfBands();

        //LSH
        LSHHash lshHash = preprocessLSH(object);

        //Secrete Share build
        SecretKey key = createAESKey(); //Creates a random key object
        byte[] cyphered_data = cipher(object.toByteArray(), key); //Ciphers object using the generated key
        byte[] newData = combineKeyAndData(cyphered_data, key.getEncoded()); //Joins data and key into a single object

        //Create erasure codes using ciphered data and key
        ErasureCodes erasureCodes = appContext.getErasureCodesFactory().getNewErasureCodes();
        erasureCodes.encodeDataObject( newData, n_bands );

        //Create UID using ciphered data and key
        UniqueIdentifier uniqueIdentifier = appContext.getUniqueIdentifierFactory().getNewUniqueIdentifier();
        uniqueIdentifier.setObject( newData );

        return new ProcessedData(object, lshHash,uniqueIdentifier, erasureCodes);
    }

    @Override
    public DataObject<?> postProcess( ErasureCodes erasureCodes, UniqueIdentifier uniqueIdentifier )
            throws CorruptDataException,
            IncompleteBlockException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException
    {
        byte[] rcv_data = erasureCodes.decodeDataObject();

        //validate
        if( !validate(rcv_data, uniqueIdentifier) )
            throw new CorruptDataException("Corrupt data block found", erasureCodes, uniqueIdentifier);

        //Create key
        byte[] encoded_key = new byte[key_size];
        System.arraycopy(rcv_data, 0,encoded_key,0, key_size);
        SecretKey key = new SecretKeySpec(encoded_key, algorithm);

        byte[] ciphered_data = new byte[rcv_data.length - key_size];
        System.arraycopy(rcv_data, key_size, ciphered_data, 0, ciphered_data.length);

        //Decipher
        byte[] data = null;
        try {
            data = decipher(ciphered_data, key);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Create Object
        DataObject object = appContext.getDataObjectFactory().getNewDataObject();
        object.setByteArray(data);

        return object;
    }

    @Override
    public LSHHash preprocessLSH(DataObject object) {
        LSHHash object_hash = appContext.getLshHashFactory().getNewLSHHash(); //Gets based on config file
        object_hash.setObject(object.toByteArray(),appContext.getNumberOfBands());

        return object_hash;
    }

    //Auxiliary methods
    /**
     * Creates a random AES key using a KeyGenerator
     */
    private SecretKey createAESKey() {
        return keyGenerator.generateKey();
    }

    /**
     * Cyphers a data array using a cypher and a key
     * @param data raw data vector
     * @param key cipher key
     * @return  cyphered data vector
     */
    private synchronized byte[] cipher( byte[] data, SecretKey key) throws
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    /**
     * Deciphers a ciphered data array using a cipher and a key
     * @param data ciphered data vector
     * @param key cipher key
     * @return raw data vector
     */
    private synchronized byte[] decipher( byte[] data, SecretKey key ) throws
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    /**
     * Combines the key and data into a single data array
     * @param data data vector
     * @param key key vector
     * @return key and data in that order
     */
    private byte[] combineKeyAndData( byte[] data, byte[] key ){
        byte[] result = Arrays.copyOf(key, data.length + key.length);
        System.arraycopy(data, 0, result, key.length, data.length);
        return result;
    }
}
