package se.recan.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * 
 * @author Anders Recksén (recan)
 */
public class CacheUtil  {

    private static CacheUtil instance = null;
    private static final HashMap<String, String> CACHE = new HashMap<String, String>();

    public static CacheUtil initCache(String bundle) throws Exception {
        if (instance == null) {
            instance = new CacheUtil(bundle);
        }
        return instance;
    }

    private CacheUtil(String bundle) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle(bundle);
        
        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = rb.getString(key);
            CACHE.put(key, value);
        }
    }

    public static String getProperty(String key) {
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        } else {
            return key;
        }
    }
}
