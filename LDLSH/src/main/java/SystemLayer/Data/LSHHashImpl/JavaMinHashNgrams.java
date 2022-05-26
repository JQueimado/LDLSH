package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

public class JavaMinHashNgrams extends JavaMinHash{

    private static final String ngrams_config = "NGRAMS_LEVEL";

    public JavaMinHashNgrams( DataContainer appContext ){
        super(appContext);
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

}
