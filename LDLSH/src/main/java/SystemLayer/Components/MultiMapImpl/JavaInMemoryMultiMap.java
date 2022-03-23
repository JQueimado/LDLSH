package SystemLayer.Components.MultiMapImpl;

public class JavaInMemoryMultiMap implements MultiMap{

    //Constructors
    public JavaInMemoryMultiMap(){

    }

    @Override
    public void insert(byte[] lshHash, byte[] uniqueIdentifier, byte[] erasureCodes) {

    }

    @Override
    public byte[] complete(byte[] lshHash, byte[] uniqueIdentifier) {
        return new byte[0];
    }

    @Override
    public MultiMapValue[] query(byte[] lshHash) {
        return new MultiMapValue[0];
    }

    @Override
    public void setHashBlockPosition(int position) {

    }

    @Override
    public int getHashBlockPosition() {
        return 0;
    }

    @Override
    public void setTotalBlocks(int totalBlocks) {

    }

    @Override
    public int getTotalBlocks() {
        return 0;
    }
}
