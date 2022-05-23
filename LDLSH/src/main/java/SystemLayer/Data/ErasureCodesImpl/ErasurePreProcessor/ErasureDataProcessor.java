package SystemLayer.Data.ErasureCodesImpl.ErasurePreProcessor;

public abstract class ErasureDataProcessor {
    public byte[] preProcess( byte[] data ){
        return data;
    };
    public byte[] postProcess( byte[] data ){
        return data;
    };
}
