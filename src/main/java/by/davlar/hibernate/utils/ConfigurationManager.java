package by.davlar.hibernate.utils;

import lombok.experimental.UtilityClass;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class ConfigurationManager {

    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.configure();

        return configuration;
    }
}
