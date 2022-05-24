package Factories.DataFactories;


import Factories.Factory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;

import static SystemLayer.Data.LSHHashImpl.JavaMinHash.LSH_SEED;

public class LSHHashFactory {
    private enum configurations {NONE,JAVA_MINHASH}

    public LSHHashFactory(){
        //
    }

    public LSHHash getNewLSHHash(String config_name, DataContainer dataContainer) throws Exception {

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case JAVA_MINHASH -> {
                if ( !JavaMinHash.isSet() ){
                    if (dataContainer == null){
                        throw new Exception("No DataContainer was provided");
                    }

                    Configurator configurator = dataContainer.getConfigurator();

                    //Get Accuracy
                    String accuracy_string = configurator.getConfig(JavaMinHash.ERROR);
                    double accuracy_error;
                    if (accuracy_string.isBlank())
                        throw new Exception("JavaMinHash requires ERROR configuration");
                    else
                        accuracy_error = Double.parseDouble(accuracy_string);

                    //Get Vector dimensions
                    String vector_dimensions_string = configurator.getConfig(JavaMinHash.VECTOR_DIMENSIONS);
                    int vector_dimensions;
                    if (vector_dimensions_string.isBlank())
                        throw new Exception("JavaMinHash requires VECTOR_DIMENSIONS configuration");
                    else
                        vector_dimensions = Integer.parseInt(vector_dimensions_string);

                    //Get Seed
                    String seed_string = configurator.getConfig(LSH_SEED);
                    long seed;
                    if (seed_string.isBlank())
                        throw new Exception("JavaMinHash requires "+ LSH_SEED +" configuration");
                    seed = Long.parseLong(vector_dimensions_string);

                    //setUp
                    JavaMinHash.setupMinHash(accuracy_error, vector_dimensions, seed);
                }
                return new JavaMinHash(dataContainer);
            }

            default ->{
                return null;
            }
        }
    }
}
