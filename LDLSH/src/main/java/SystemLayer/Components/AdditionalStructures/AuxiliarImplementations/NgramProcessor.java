package SystemLayer.Components.AdditionalStructures.AuxiliarImplementations;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.JavaMinHashNgrams;
import SystemLayer.SystemExceptions.UnknownConfigException;

import java.util.HashSet;
import java.util.Set;

public class NgramProcessor {

    private static final String ngrams_config = "NGRAMS_LEVEL";
    private static final int ngrams_level_limit = Integer.BYTES;

    private static int Level = -1;

    /**
     * Calculates a set of Integers that represent a set of ngrams extracted from an input vector
     * @param data data input
     * @return set of Integers
     * @throws UnknownConfigException
     */
    public static Set<Integer> create_ngrams(byte[] data, DataContainer appContext ) throws UnknownConfigException {

        //Setup
        if( Level == -1 ) {
            String value = "";
            try {
                value = appContext.getConfigurator().getConfig(ngrams_config);
                if (value.isBlank())
                    throw new Exception();

                Level = Integer.parseInt(value);
                if (Level < 0 || Level > ngrams_level_limit)
                    throw new UnknownConfigException(ngrams_config, value);

            } catch (Exception e) {
                throw new UnknownConfigException(ngrams_config, value);
            }
        }

        //Create ngrams
        Set<Integer> result = new HashSet<>();
        int c = data.length - Level + 1;
        for( int i = 0; i<c; i++){
            //Convert into Integer
            byte[] bytes = new byte[Level];
            System.arraycopy(data, i, bytes,0, Level);

            //Copy bytes to int
            int int_value = 0;
            for(int j = 0; j<Level; j++){
                int_value = (int_value << 8) + (bytes[j] & 0xFF);
            }

            result.add(int_value);
        }

        return result;
    }
}
