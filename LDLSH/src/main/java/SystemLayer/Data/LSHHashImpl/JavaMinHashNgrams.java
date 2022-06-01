package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class JavaMinHashNgrams extends JavaMinHash{

    private static final String ngrams_config = "NGRAMS_LEVEL";

    private static int ngram_level;

    public JavaMinHashNgrams( DataContainer appContext ) throws UnknownConfigException {
        super(appContext);

        String value = "";
        try{
            value = appContext.getConfigurator().getConfig(ngrams_config);
            if( value.isBlank() )
                throw new Exception();
            ngram_level = Integer.parseInt(value);
        }catch (Exception e){
            throw new UnknownConfigException( ngrams_config, value );
        }
    }

    @Override
    public void setObject( byte[] object, int n_blocks ){
        //Calculate ngram vector
        this.data = getSignature(data);
        this.blocks = createBlocks(this.data, n_blocks);
    }

    @Override
    protected void setupMinHash( int vector_dimensions, DataContainer appContext ){ //l-n+1
        String level_string = appContext.getConfigurator().getConfig( ngrams_config );
        int level = Integer.parseInt(level_string);

        int ngramDimensions = vector_dimensions - level + 1;
        super.setupMinHash(ngramDimensions, appContext);
    }

    //Auxiliary methods
    public static byte[][] create_ngrams( byte[] data ){
        byte[][] ngram_vector = new byte[data.length-ngram_level+1][ngram_level];

        for( int i=0; i< ngram_vector.length; i++ ){
            System.arraycopy(data, i, ngram_vector[i], 0, ngram_level);
        }

        return ngram_vector;
    }

}
