package Factories.DataFactories;


import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;

public class LSHHashFactory {
    private enum configurations {NONE,JAVA_MINHASH}

    public LSHHashFactory(){
        //
    }

    public LSHHash getNewLSHHash(String config_name, DataContainer dataContainer){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case JAVA_MINHASH -> {
                return new JavaMinHash(dataContainer);
            }

            default ->{
                return null;
            }
        }
    }
}
