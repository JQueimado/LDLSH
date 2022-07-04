package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

import java.util.HashSet;
import java.util.Set;

public class JavaMinHashNgrams extends JavaMinHash{

    private static final String ngrams_config = "NGRAMS_LEVEL";
    private static final int ngrams_level_limit = Integer.BYTES;

    public JavaMinHashNgrams( DataContainer appContext ) throws UnknownConfigException {
        super(appContext);
    }

    @Override
    public void setObject( byte[] object, int n_blocks ){
        try {
            //Calculate ngram vector
            this.data = getSignature(create_ngrams(object, appContext));
            this.blocks = createBlocks(this.data, n_blocks);
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
        }
    }

    //Auxiliary methods

    /**
     * Calculates a set of Integers that represent a set of ngrams extracted from an input vector
     * @param data data input
     * @return set of Integers
     * @throws UnknownConfigException
     */
    public static Set<Integer> create_ngrams(byte[] data, DataContainer appContext ) throws UnknownConfigException {

        //Setup
        String value = "";
        int ngram_level;
        try{
            value = appContext.getConfigurator().getConfig(ngrams_config);
            if( value.isBlank() )
                throw new Exception();

            ngram_level = Integer.parseInt(value);
            if( ngram_level < 0 || ngram_level > ngrams_level_limit)
                throw new UnknownConfigException( ngrams_config, value );

        }catch (Exception e){
            throw new UnknownConfigException( ngrams_config, value );
        }

        //Create ngrams
        Set<Integer> result = new HashSet<>();
        int c = data.length - ngram_level + 1;
        for( int i = 0; i<c; i++){
            //Convert into Integer
            byte[] bytes = new byte[ngram_level];
            System.arraycopy(data, i, bytes,0, ngram_level);

            //Copy bytes to int
            int int_value = 0;
            for(int j = 0; j<ngram_level; j++){
                int_value = (int_value << 8) + (bytes[j] & 0xFF);
            }

            result.add(int_value);
        }

        return result;
    }

}
