package Factories.DataFactories;

import Factories.Factory;
import SystemLayer.Data.PackagesImpl.StandardPackages;

public class PackagesFactory implements Factory {
    private enum configurations {NONE,STANDARD}

    public PackagesFactory(){
        //
    }

    public StandardPackages getNewLSHHash(String config_name){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case STANDARD -> {
                return new StandardPackages();
            }

            default ->{
                return null;
            }
        }
    }
}
