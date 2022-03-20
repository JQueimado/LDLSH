package SystemLayer.Data.LSHHashImpl;

public class ArrayLSHHash<T> implements LSHHash<T>{

    T[] data;

    public ArrayLSHHash(T[] data ){
        this.data = data;
    }

    public ArrayLSHHash(){
        this.data = null;
    }

    @Override
    public void setValues(T[] values) {
        this.data = values;
    }

    @Override
    public T[] getValues() {
        return data;
    }

    @Override
    public LSHBlock<T>[] getBlocks(int n_blocks) {
        /*TODO*/
        return null;
    }

    //Blocks
    public static class ArrayLSHBlock<T> implements LSHBlock<T>{

        T[] block_data;

        @Override
        public void setValues( T[] values) {
            block_data = values;
        }

        @Override
        public T[] getValues() {
            return block_data;
        }
    }

}
