package Factories;

public interface Factory {
    class ConfigException extends Exception{
        String config_name;
        String config_value;
        public ConfigException(String msg, String config_name, String config_value){
            super(msg);
        }
    }
}
