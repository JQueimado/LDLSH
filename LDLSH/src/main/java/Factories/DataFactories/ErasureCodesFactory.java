package Factories.DataFactories;

import Factories.FactoryImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.*;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class ErasureCodesFactory extends FactoryImpl {

    public static final String config_name = "ERASURE_CODES";

    public enum configurations {
        NONE,
        REED_SOLOMON,
        SHAMIR,
        REED_SOLOMON_SHAMIR,
        SIMPLE_PARTITION
    }

    //Constructors
    public ErasureCodesFactory( DataContainer appContext ){
        super(appContext);
    }

    public ErasureCodes getNewErasureCodes(){
        String config = appContext.getConfigurator().getConfig( config_name );
        return getNewErasureCodes(config);
    }

    //getters
    public ErasureCodes getNewErasureCodes(String config_name) {
        try {
            configurations config = configurations.valueOf(config_name);
            switch (config) {

                case REED_SOLOMON -> {
                    return new BackblazeReedSolomonErasureCodes(appContext);
                }

                case SHAMIR -> {
                    return new ShamirErasureCodes(appContext);
                }

                case REED_SOLOMON_SHAMIR -> {
                    return new SecretShareReadSolomonErasureCodes(appContext);
                }

                case SIMPLE_PARTITION -> {
                    return new SimplePartitionErasureCodes(appContext);
                }

                default -> {
                    return null;
                }
            }
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }
    }
}
