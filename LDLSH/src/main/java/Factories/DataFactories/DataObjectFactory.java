package Factories.DataFactories;

import Factories.Factory;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.ArrayLSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;

public class DataObjectFactory implements Factory {
    private enum configurations {NONE,STRING}

    public DataObjectFactory(){
        //
    }

    public DataObject getNewDataObject(String config_name){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case STRING -> {
                return new StringDataObject();
            }

            default ->{
                return null;
            }
        }
    }
}
