import Factories.ComponentFactories.MainFactory;
import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemMain.SystemMain;

import javax.xml.crypto.Data;

public class Main {

    public static void main(String[] args) throws Exception {
        //Create Context
        DataContainer appContext = new DataContainer(args[0]);
        //Create main
        MainFactory mainFactory = new MainFactory(appContext);
        SystemMain main = mainFactory.getNewMain(args);
        //Run
        main.run();
    }
}
