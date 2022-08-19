package Factories.ComponentFactories;

import Factories.FactoryImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;
import SystemLayer.SystemMain.*;

public class MainFactory extends FactoryImpl {

    private final String main_config = "MAIN";

    private enum configurations {STANDARD, TEST, ACCURACY_TEST, THROUGHPUT_TEST, LATENCY_TEST}

    public MainFactory(DataContainer appContext) {
        super(appContext);
    }

    public SystemMain getNewMain(String args[]) throws Exception{
        try {
            String config = appContext.getConfigurator().getConfig(main_config);
            return getNewMain(config, args);
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }
    }

    public SystemMain getNewMain(String config_setting, String[] args) throws Exception {
        try{
            configurations config = configurations.valueOf(config_setting);

            switch (config){
                case STANDARD -> {
                    return new StandardMain(args, appContext);
                }

                case TEST -> {
                    return new TestMain(args, appContext);
                }

                case ACCURACY_TEST -> {
                    return new AccuracyTestMain(args, appContext);
                }

                case THROUGHPUT_TEST -> {
                    return new ThroughputTestMain(args, appContext);
                }

                case LATENCY_TEST -> {
                    return new LatencyTestMain(args, appContext);
                }

                default -> {
                    return null;
                }
            }

        }catch (IllegalArgumentException e){
            throw new UnknownConfigException(main_config, config_setting);
        }
    }

}
