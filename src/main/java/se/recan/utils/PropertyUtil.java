package se.recan.utils;

import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 2012-09-05
 *
 * @author Anders Recksén (recan)
 */
public class PropertyUtil extends Properties {

    private final Properties properties;
    private File configfile;

    public PropertyUtil(String address) {
        properties = new Properties();
        FileInputStream input = null;

        try {
            configfile = new File(address);
            if (!configfile.exists()) {
                configfile.createNewFile();
            }

            input = new FileInputStream(configfile);
            properties.load(input);
        } catch (IOException ioe) {
            System.out.println(address + " No such file or directory");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
    
    public void addProperty(String name, String value) throws IOException {
        properties.setProperty(name, value);

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(configfile);
            properties.store(output, "ConfigFile");
        } catch (IOException ioe) {
            System.out.println("Filen " + configfile + " existerar inte");
        } finally {
            if(output != null) {
                output.close();
            }
        }
    }

    // Go to super class directly.
    @Override
    public String getProperty(String prop) throws NullPointerException {
        return properties.getProperty(prop);
    }
    
     // Go to super class directly.
    @Override
    public String getProperty(String prop, String defaultValue) throws NullPointerException {
        return properties.getProperty(prop, defaultValue);
    }

    public String getStringProperty(String prop) throws NullPointerException {
        return properties.getProperty(prop);
    }

    public int getIntProperty(String prop) throws NullPointerException {
        return Integer.parseInt(properties.getProperty(prop));
    }

    public boolean getBolProperty(String prop) throws NullPointerException {
        return Boolean.parseBoolean(properties.getProperty(prop));
    }
    
    public boolean getBolProperty(String prop, String defaultValue) {
        String property = properties.getProperty(prop, defaultValue);
        return Boolean.parseBoolean(property);
    }

    public File getFileProperty(String prop) throws NullPointerException {
        return new File(properties.getProperty(prop));
    }

    public URL getUrlProperty(String prop) throws NullPointerException, MalformedURLException {
        return new URL(properties.getProperty(prop));
    }
    
    public List<String> getProperties() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            list.add(entry.getKey() + ": " + entry.getValue());
        }

        return list;
    }
}
