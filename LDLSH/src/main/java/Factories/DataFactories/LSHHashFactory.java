package Factories.DataFactories;


import Factories.Factory;
import Factories.FactoryImpl;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.JavaMinHashNgrams;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.SystemExceptions.UnknownConfigException;

import static SystemLayer.Data.LSHHashImpl.JavaMinHash.LSH_SEED;

public class LSHHashFactory extends FactoryImpl {
    //Class context
    public static final String config_name = "LSH_HASH";

    //Object context
    private enum configurations {NONE,JAVA_MINHASH, JAVA_MINHASH_NGRAMS}

    public LSHHashFactory( DataContainer appContext ){
        super(appContext);
    }

    public LSHHash getNewLSHHash() {
        try {
            String config = appContext.getConfigurator().getConfig(config_name);
            return getNewLSHHash(config);
        } catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }
    }

    public LSHHash getNewLSHHash(String config) throws UnknownConfigException {

        try {
            configurations config_value = configurations.valueOf(config);

            switch (config_value) {

                case JAVA_MINHASH -> {
                    return new JavaMinHash(appContext);
                }

                case JAVA_MINHASH_NGRAMS -> {
                    return new JavaMinHashNgrams(appContext);
                }

                default -> {
                    return null;
                }
            }
        }catch (IllegalArgumentException e){
            throw new UnknownConfigException(config_name, config);
        }
    }
}
