package Factories.DataFactories;

import SystemLayer.Data.LSHHashImpl.ArrayLSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;

public class LSHHashFactory {
    private enum configurations {NONE,INT_ARRAY_HASH}

    public LSHHashFactory(){
        //
    }

    public LSHHash getNewLSHHash( String config_name){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case INT_ARRAY_HASH -> {
                return new ArrayLSHHash<Integer>();
            }

            default ->{
                return null;
            }
        }
    }

    //Hash
    public interface LSHHash<T> {
        void setValues( T[] values );
        T[] getValues();
        SystemLayer.Data.LSHHashImpl.LSHHash.LSHBlock<T>[] getBlocks(int n_blocks);

        interface LSHBlock<T>{
            void setValues( T[] values );
            T[] getValues();
        }
    }
}
