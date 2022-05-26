package Factories;

import SystemLayer.Containers.DataContainer;

public class FactoryImpl implements Factory{

    protected DataContainer appContext;

    public FactoryImpl(DataContainer appContext){
        this.appContext = appContext;
    }

}
