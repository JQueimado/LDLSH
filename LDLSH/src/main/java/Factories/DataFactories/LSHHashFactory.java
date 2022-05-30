package Factories.DataFactories;


import Factories.Factory;
import Factories.FactoryImpl;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;

import static SystemLayer.Data.LSHHashImpl.JavaMinHash.LSH_SEED;

public class LSHHashFactory extends FactoryImpl {
    //Class context
    public static final String config_name = "LSH_HASH";

    //Object context
    private enum configurations {NONE,JAVA_MINHASH}

    public LSHHashFactory( DataContainer appContext ){
        super(appContext);
    }

    public LSHHash getNewLSHHash() {
        String config = appContext.getConfigurator().getConfig(config_name);
        return getNewLSHHash( config );
    }

    public LSHHash getNewLSHHash(String config_name) {

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case JAVA_MINHASH -> {
                return new JavaMinHash(appContext);
            }

            default ->{
                return null;
            }
        }
    }
}
