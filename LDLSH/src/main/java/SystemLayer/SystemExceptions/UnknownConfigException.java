package SystemLayer.SystemExceptions;

//Config Exception
public class UnknownConfigException extends Exception {

    //Static
    public static void handler( UnknownConfigException unknownConfigException ){
        if (unknownConfigException.config_value.isBlank()){
            unknownConfigException.config_value = "\"\"";
        }

        System.err.println(
                "Config named " +
                unknownConfigException.config_name +
                " does not support option " +
                unknownConfigException.config_value
        );

        unknownConfigException.printStackTrace();

        System.exit(1);
    }

    //Static
    String config_name;
    String config_value;

    public UnknownConfigException( String config_name, String config_value) {
        super();
        this.config_name = config_name;
        this.config_value = config_value;
    }
}
