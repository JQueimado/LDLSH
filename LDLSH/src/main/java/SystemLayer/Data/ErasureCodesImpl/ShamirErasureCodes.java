package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.ErasureBlock;
import SystemLayer.Data.DataUnits.ErasureBlockImpl;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import SystemLayer.SystemExceptions.UnknownConfigException;
import com.codahale.shamir.Scheme;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class ShamirErasureCodes extends ErasureCodesImpl{

    private static final String fault_config_name = "ERASURE_FAULTS";
    private static final String random_seed_config_name ="SEED";

    private static int n; //Total blocks
    private static int k; //Data blocks

    private static Scheme encoder = null;

    public static void setupEncoder( DataContainer appContext ) throws UnknownConfigException {
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
        encoder = new Scheme( new SecureRandom(seed.getBytes(StandardCharsets.UTF_8)), n, k);
    }

    /**
     * Erasure codes super constructor
     *
     * @param appContext application context
     */
    public ShamirErasureCodes(DataContainer appContext) throws UnknownConfigException {
        super(appContext);
        if ( encoder == null)
            setupEncoder(appContext);
    }

    @Override
    public void encodeDataObject(byte[] object, int n_blocks) throws Exception {
        Map<Integer, byte[]> codes = encoder.split(object);

        erasureBlocks = new ErasureBlock[n];
        for(Integer i : codes.keySet()){
            erasureBlocks[i-1] = new ErasureBlockImpl(codes.get(i), i-1);
        }
        number_of_blocks = n_blocks;
        total_blocks = n;
    }

    @Override
    public byte[] decodeDataObject() throws IncompleteBlockException {
        if( erasureBlocks.length < k)
            throw new IncompleteBlockException();

        Map<Integer, byte[]> codes = new HashMap<>();
        for( ErasureBlock block : erasureBlocks ){
            if( block != null )
                codes.put( block.getPosition()+1, block.getBlock() );
        }
        return encoder.join(codes);
    }
}
