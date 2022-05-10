package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.util.Set;

public class JavaMinHashNgrams extends JavaMinHash{

    private static final String vector_dimensions = "VECTOR_DIMENSIONS";

    public JavaMinHashNgrams(DataObject dataObject, int n_blocks, DataContainer dataContainer) throws Exception {
        super(dataObject, n_blocks, dataContainer);
    }

    public JavaMinHashNgrams(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    public void setObject( DataObject object, int n_blocks ){

    }

}
