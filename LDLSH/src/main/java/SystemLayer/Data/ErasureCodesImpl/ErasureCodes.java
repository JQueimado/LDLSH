package SystemLayer.Data.ErasureCodesImpl;

public interface ErasureCodes {
    void createErasureCodes();
    byte[] getErasureCodes();
    byte[][] getErasureBlocks();
}
