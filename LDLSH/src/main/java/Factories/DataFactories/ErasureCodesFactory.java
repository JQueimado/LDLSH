package Factories.DataFactories;

import Factories.Factory;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ReedSolomonErasureCodes;

public class ErasureCodesFactory implements Factory {

    public enum configurations {NONE,REED_SOLOMON}

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

            default -> {return null;}
        }
    }
}
