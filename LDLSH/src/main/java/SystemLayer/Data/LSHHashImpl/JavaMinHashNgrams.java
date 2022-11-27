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
            this.data = getSignature(NgramProcessor.create_ngrams(object, appContext));
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
        }
    }

    //Auxiliary methods

}
