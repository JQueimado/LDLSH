package Factories.DataFactories;

import Factories.FactoryImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.BackblazeReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ShamirErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.SimplePartitionErasureCodes;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class ErasureCodesFactory extends FactoryImpl {

    public static final String config_name = "ERASURE_CODES";

    public enum configurations {
        NONE,
        REED_SOLOMON,
        SHAMIR,
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
