package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Components.AdditionalStructures.AuxiliarImplementations.NgramProcessor;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class JavaMinHashNgrams extends JavaMinHash{

    public JavaMinHashNgrams( DataContainer appContext ) throws UnknownConfigException {
        super(appContext);
    }

    @Override
    public void setObject( byte[] object, int n_blocks ){
        try {
            //Calculate ngram vector
            byte[] data = getSignature(NgramProcessor.create_ngrams(object, appContext));
            this.blocks = createBlocks(data, n_blocks);
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
        }
    }

    //Auxiliary methods

}
