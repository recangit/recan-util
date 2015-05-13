package se.recan.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Anders Recksén (recan)
 */
public class EntityUtil {
    
    private static final Logger LOGGER = Logger.getLogger("Logger");
    
    private int counter = 0;

    /**
     * Singleton, only one instance allowed.
     */
    private static EntityUtil instance = null;

    /**
     * Populate this with key-value pairs when iterating the request parameters.
     * It's needed later on for validation.
     */
    private final HashMap<String, String> map;

    private EntityUtil() {
        map = new HashMap<>();
    }

    /**
     *
     * @return
     */
    public static EntityUtil getInstance() {
        if (instance == null) {
            instance = new EntityUtil();
        }

        return instance;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return map.size();
    }

    /**
     * Fult! Ibland vill jag inte att metoder skall föregås av set och get.
     * Hitta annan lösning.
     */
    public Object processCorrectName(List<String> list, String className)
            throws NoSuchMethodException, SecurityException, Exception {

        Class c = Class.forName(className);
        Object o = c.newInstance();

        map.clear();

        for (String s : list) {
            String[] split = s.split(":");

//            String key = split[0];
            
            String methodName = split[0];
//            methodName = "set" + methodName;
            
            String value = split[1];
            map.put(methodName, value);

            Method m = null;
            try {
                if (value.equals("on") || value.equals("true")) {
                    m = getBol(c, methodName);
                    m.invoke(o, true);
                } else if (value.equals("off") || value.equals("false")) {
                    m = getBol(c, methodName);
                    m.invoke(o, false);
                } 
                else if (Pattern.matches("[0-9]+", value)) {
                    m = getInt(c, methodName);
                    m.invoke(o, Integer.parseInt(value));
                } else if (Pattern.matches("[0-9]+-[0-9]+-[0-9]+", value)) {
                    m = getLong(c, methodName);
                    m.invoke(o, dateToLong(value));
                }
                else {
                    m = getStr(c, methodName);
                    m.invoke(o, value);
                }
            } catch (NoSuchMethodException nse) {
                // All request parameters end up in this class, even they that's not represented in the bean
                // we are populating. When that happens a NoSuchMethodException will be thrown. In this case
                // that is considered a normal behaviour and this method should not be terminated.
                System.out.println("Method " + methodName + " doesn't exist!\n" + nse.getMessage());
            }
        }

        return o;
    }
    
    public Object process(List<String> list, String className)
            throws NoSuchMethodException, SecurityException, Exception {

        Class c = Class.forName(className);
        Object o = c.newInstance();

        map.clear();

        for (String s : list) {
            String[] split = s.split(":");

            String key = split[0];

            String methodName = key.substring(0, 1).toUpperCase() + key.substring(1, key.length()); // First letter to upper case
            methodName = "set" + methodName;
            
            String value = split[1];
            map.put(methodName, value);

            Method m = null;
            try {
                if (value.equals("on") || value.equals("true")) {
                    m = getBol(c, methodName);
                    m.invoke(o, true);
                } else if (value.equals("off") || value.equals("false")) {
                    m = getBol(c, methodName);
                    m.invoke(o, false);
                } 
                else if (Pattern.matches("[0-9]+", value)) {
                    m = getInt(c, methodName);
                    m.invoke(o, Integer.parseInt(value));
                } else if (Pattern.matches("[0-9]+-[0-9]+-[0-9]+", value)) {
                    m = getLong(c, methodName);
                    m.invoke(o, dateToLong(value));
                }
                else {
                    m = getStr(c, methodName);
                    m.invoke(o, value);
                }
            } catch (NoSuchMethodException nse) {
                // All request parameters end up in this class, even they that's not represented in the bean
                // we are populating. When that happens a NoSuchMethodException will be thrown. In this case
                // that is considered a normal behaviour and this method should not be terminated.
                System.out.println("Method " + methodName + " doesn't exist!\n" + nse.getMessage());
                LOGGER.debug("Method " + methodName + " doesn't exist!\n" + nse.getMessage());
            }
        }

        return o;
    }

    /**
     *
     * @param request
     * @param className
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws Exception
     */
    public Object process(HttpServletRequest request, String className)
            throws NoSuchMethodException, SecurityException, Exception {

        Class c = Class.forName(className);
        Object o = c.newInstance();

        Enumeration e = request.getParameterNames();
        map.clear();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
            String methodName = key.substring(0, 1).toUpperCase() + key.substring(1, key.length()); // First letter to upper case
            methodName = "set" + methodName;

            Method m = null;
            try {
                if (value.equals("on") || value.equals("true")) {
                    m = getBol(c, methodName);
                    m.invoke(o, true);
                } else if (Pattern.matches("[0-9]+", value)) {
                    m = getInt(c, methodName);
                    m.invoke(o, Integer.parseInt(value));
                } else if (Pattern.matches("[0-9]+-[0-9]+-[0-9]+", value)) {
                    m = getLong(c, methodName);
                    m.invoke(o, dateToLong(value));
                } else {
                    m = getStr(c, methodName);
                    m.invoke(o, value);
                }
            } catch (NoSuchMethodException nse) {
                // All request parameters end up in this class, even them that's not represented in the bean
                // we are populating. When that happens a NoSuchMethodException will be thrown. In this case
                // that is considered a normal behaviour and this method should not be terminated.
                System.out.println("Method " + methodName + " doesn't exist!\n" + nse.getMessage());
            }
        }

        return o;
    }

    /**
     *
     * @param resultset
     * @param className
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws Exception
     */
    public Object process(ResultSet resultset, String className)
            throws NoSuchMethodException, SecurityException, Exception {

        Class c = Class.forName(className);
        Object o = c.newInstance();

        map.clear();

        ResultSetMetaData metadata = resultset.getMetaData();

        if (resultset.next()) {
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                String key = metadata.getColumnLabel(i);
                String value = resultset.getString(i);
                map.put(key, value);

                String columnType = metadata.getColumnTypeName(i);

                String methodName = key.substring(0, 1).toUpperCase() + key.substring(1, key.length()); // First letter to upper case
                methodName = "set" + methodName;

                Method m = null;
                try {
                    switch (columnType) {
                        case "LONG":
                            m = getLong(c, methodName);
                            m.invoke(o, Long.parseLong(value));
                            break;
                        case "LONGLONG":
                            m = getLong(c, methodName);
                            m.invoke(o, Long.parseLong(value));
                            break;
                        case "BIGINT":
                            m = getLong(c, methodName);
                            m.invoke(o, Long.parseLong(value));
                            break;
                        case "CHAR":
                            m = getStr(c, methodName);
                            m.invoke(o, value);
                            break;
                        case "VARCHAR":
                            m = getStr(c, methodName);
                            m.invoke(o, value);
                            break;
                        case "TEXT":
                            m = getStr(c, methodName);
                            m.invoke(o, value);
                            break;
                        case "TINY":
                            m = getInt(c, methodName);
                            m.invoke(o, Integer.parseInt(value));
                            break;
                        case "TINYINT":
                            m = getBol(c, methodName);
                            m.invoke(o, Integer.parseInt(value) == 1);
                            break;
                        case "INT":
                            m = getInt(c, methodName);
                            m.invoke(o, Integer.parseInt(value));
                            break;
                        case "BOOLEAN":
                            m = getBol(c, methodName);
                            m.invoke(o, true);
                            break;
                        default:
                            m = getStr(c, methodName);
                            m.invoke(o, value);
                            break;
                    }
                } catch (NoSuchMethodException nse) {
                    // All request parameters end up in this class, even them that's not represented in the bean
                    // we are populating. When that happens a NoSuchMethodException will be thrown. In this case
                    // that is considered a normal behaviour and this method should not be terminated.
                    System.out.println("Method " + methodName + " doesn't exist!\n" + nse.getMessage() + "columnType=" + columnType);
                }
            }
        }

        return o;
    }

    /**
     * The correct type of argument must be known by a dynamicly created method.
     * Here we create a method that take a boolean as argument.
     *
     * @param Class c The Class that are about to be populated.
     * @param String methodName The name of the method to call.
     * @return Method The actual method.
     * @throws NoSuchMethodException
     */
    private Method getBol(Class c, String methodName) throws NoSuchMethodException {
        return c.getMethod(methodName, Boolean.TYPE);
    }

    /**
     * Here we create a method that take a int as argument.
     *
     * @param Class c The Class that are about to be populated.
     * @param String methodName The name of the method to call.
     * @return Method The actual method.
     * @throws NoSuchMethodException
     */
    private Method getInt(Class c, String methodName) throws NoSuchMethodException {
        return c.getMethod(methodName, Integer.TYPE);
    }

    /**
     * Here we create a method that take a long as argument.
     *
     * @param Class c The Class that are about to be populated.
     * @param String methodName The name of the method to call.
     * @return Method The actual method.
     * @throws NoSuchMethodException
     */
    private Method getLong(Class c, String methodName) throws NoSuchMethodException {
        return c.getMethod(methodName, Long.TYPE);
    }

    /**
     * Here we create a method that take a String as argument.
     *
     * @param Class c The Class that are about to be populated.
     * @param String methodName The name of the method to call.
     * @return Method The actual method.
     * @throws NoSuchMethodException
     */
    private Method getStr(Class c, String methodName) throws NoSuchMethodException {
        return c.getMethod(methodName, String.class);
    }

    /**
     * Validate that the value of a field is no shorter than specified.
     *
     * @param key String key is the name of a field.
     * @param size The minimum allowed size.
     * @return boolean
     */
    public boolean validate(String key, int size) {
        return map.containsKey(key) && map.get(key).length() >= size;
    }

    /**
     * Validate that a field is not empty.
     *
     * @param key String key is the name of a field.
     * @return boolean
     */
    public boolean validate(String key) {
        return map.containsKey(key) && !map.get(key).equals("");
    }

    /**
     * Validate that the value of a field contains a specified String.
     *
     * @param key String key is the name of a field.
     * @param mandatory The recuired String.
     * @return boolean
     */
    public boolean validate(String key, String mandatory) {
        if (mandatory.equals("@")) {
            return validateMail(key);
        }
        return map.containsKey(key) && map.get(key).contains(mandatory);
    }

    /**
     * Validate that a String is a valid email address.
     *
     * @param mail
     * @return boolean
     */
    private boolean validateMail(String mail) {
        if (mail == null) {
            return false;
        }

        String[] at = mail.split("@");
        if (at.length < 2) {
            return false;
        }

        String[] dot = at[1].split("\\.");
        return dot.length >= 2;
    }

    /**
     *
     * @return
     */
    public String getUri() {
        StringBuilder buffer = new StringBuilder(500);
        counter = 0;
        
        for (String key : map.keySet()) {    
            buffer.append(counter == 0 ? "?" : "&");
            buffer.append(key);
            buffer.append("=");
            buffer.append(map.get(key).trim());
            
            counter++;
        }
        return buffer.toString();
    }

    /**
     *
     * @param object
     * @return
     * @throws Exception
     */
    public String getUri(Object object) throws Exception {
        Class c = object.getClass();

        Field[] fields = c.getDeclaredFields();

        StringBuilder buffer = new StringBuilder(500);
        for (Field f : fields) {
            // Because fields are private.
            f.setAccessible(true);

            buffer.append("&");
            buffer.append(f.getName());
            buffer.append("=");
            buffer.append(f.get(object));
        }

        return buffer.toString();
    }

    /**
     * @return 
     * Wright parameter names and pameter values to log.
     */
    public String feedback() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Parameters recived in EntityUtil\n");
    
        for (String key : map.keySet()) {
            buffer.append("\t");
            buffer.append(String.format("%-30s", key));
            buffer.append(" => ");
            buffer.append(map.get(key));
            buffer.append("\n");
        }

        return buffer.toString();
    }

    /**
     * Check content of Resultset. Must change this as it's pointing to a
     * specific log.
     *
     * @param resultset
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws Exception
     */
    public void preview(ResultSet resultset) throws Exception {
        ResultSetMetaData metadata = resultset.getMetaData();

        while (resultset.next()) {
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                String key = metadata.getColumnLabel(i);
                String value = resultset.getString(i);
                String columnType = metadata.getColumnTypeName(i);

                System.out.println(key + "(" + columnType + ") => " + value);
            }
        }
    }

    /**
     *
     * @param inputDate
     * @return
     * @throws Exception
     */
    public static long dateToLong(String inputDate) throws Exception {
        String delimeter = "-";

        int index = inputDate.indexOf(delimeter);
        if (index == -1 || index == 0) {
            throw new Exception("This [\"" + delimeter + "\"] is not a valid delimeter in String (" + inputDate + "!");
        }

        Calendar cal = Calendar.getInstance(new Locale("sv", "SE"));
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        StringTokenizer date = new StringTokenizer(inputDate, delimeter);

        if (date.hasMoreTokens()) {
            String token = date.nextToken();
            if (token.length() == 2) {
                token = "20" + token;
            }
            cal.set(Calendar.YEAR, Integer.parseInt(token));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.MONTH, Integer.parseInt(date.nextToken()) - 1);
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.MINUTE, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.SECOND, Integer.parseInt(date.nextToken()));
        }
        if (date.hasMoreTokens()) {
            cal.set(Calendar.MILLISECOND, Integer.parseInt(date.nextToken()));
        }

        return cal.getTimeInMillis();
    }
}
