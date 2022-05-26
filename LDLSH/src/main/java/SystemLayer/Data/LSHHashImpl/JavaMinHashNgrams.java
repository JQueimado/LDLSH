package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.util.Set;

public class JavaMinHashNgrams extends JavaMinHash{

    private static final String ngrams_config = "NGRAMS_LEVEL";

    public static void setupMinHash(
            double error,
            int vector_dimensions,
            long seed,
            int level
    ) { //l-n+1
        int ngramDimensions = vector_dimensions - level + 1;
        JavaMinHash.setupMinHash(error,ngramDimensions,seed);
    }

    public JavaMinHashNgrams( DataContainer appContext ){
        super(appContext);
    }

    @Override
    public void setObject( DataObject object, int n_blocks ){
        byte[] data = object.toByteArray();
        //Calculate ngram vector
        this.data = getSignature(data);
        this.blocks = createBlocks(this.data, n_blocks);
    }

}
