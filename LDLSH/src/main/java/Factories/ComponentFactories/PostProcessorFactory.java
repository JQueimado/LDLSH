package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Components.PostProcessorImpl.PostProcessor;
import SystemLayer.Components.PostProcessorImpl.StandardPostProcessor;

public class PostProcessorFactory implements Factory {

    public enum configurations {NONE,STANDARD}

    public PostProcessor getPostProcessor( configurations config ){
        switch ( config ){
            case STANDARD -> {
                return new StandardPostProcessor();
            }

            default -> {
                return null;
            }
        }
    }
}
