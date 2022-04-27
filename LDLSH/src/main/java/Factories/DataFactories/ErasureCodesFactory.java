package Factories.DataFactories;

import Factories.Factory;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.SimplePartitionErasureCodes;

public class ErasureCodesFactory implements Factory {

    public enum configurations {NONE,REED_SOLOMON, SIMPLE_PARTITION}

    //Constructors
    public ErasureCodesFactory(){
        //
    }

    //getters
    public ErasureCodes getNewErasureCodes( String config_name ){
        configurations config = configurations.valueOf(config_name);
        switch (config){

            case REED_SOLOMON ->{
                return new ReedSolomonErasureCodes();
            }

            case SIMPLE_PARTITION ->{
                return new SimplePartitionErasureCodes();
            }

            default -> {return null;}
        }
    }
}
