package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Components.PackerImpl.Packer;
import SystemLayer.Components.PackerImpl.StandardPacker;

public class PackerFactory implements Factory {

    public enum configurations {NONE,STANDARD}

    public PackerFactory(){
        /**/
    }

    public Packer getPackerFactory(configurations config){
        switch ( config ){

            case STANDARD -> {
                return new StandardPacker();
            }

            default ->{
                return null;
            }
        }
    }
}
