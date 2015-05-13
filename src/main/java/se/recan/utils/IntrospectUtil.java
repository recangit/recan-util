package se.recan.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.log4j.Logger;

/**
 * This class uses Reflection to investigate a class.
 * 
 * September 2009
 * @author Anders Recksén (anders[at]recksen[dot]se)
 */
public class IntrospectUtil {

    private static final Logger LOGGER = Logger.getLogger("IntrospectTest");
    private static IntrospectUtil instance = null;

    public static IntrospectUtil getInstance() {
        if (instance == null) {
            instance = new IntrospectUtil();
        }
        return instance;
    }

    private String parseModifier(int numb) {
        String mod = "";

        switch (numb) {
            case Modifier.ABSTRACT:
                mod = "abstract";
                break;
            case Modifier.FINAL:
                mod = "final";
                break;
            case Modifier.INTERFACE:
                mod = "interface";
                break;
            case Modifier.NATIVE:
                mod = "protected";
                break;
            case Modifier.PRIVATE:
                mod = "private";
                break;
            case Modifier.PROTECTED:
                LOGGER.debug("PROTECTED");
                mod = "protected";
                break;
            case Modifier.PUBLIC:
                mod = "public";
                break;
            case Modifier.STATIC:
                LOGGER.debug("STATIC");
                mod = "static";
                break;
            case Modifier.STRICT:
                mod = "protected";
                break;
            case Modifier.SYNCHRONIZED:
                mod = "protected";
                break;
            case Modifier.TRANSIENT:
                mod = "protected";
                break;
            case Modifier.VOLATILE:
                mod = "protected";
                break;
            case 9:
                mod = "public static";
                break;
            case 10:
                mod = "private static";
                break;
            case 12:
                mod = "protected static";
                break;
            default:
                mod = "unknown";
        }

        return mod;
    }

    private String parseString(String value) {
        if (value.contains(".")) {
            return value.substring(value.lastIndexOf(".") + 1);
        }
        return value;
    }

    private String parseConstructor(Constructor constructor) {

        StringBuilder buffer = new StringBuilder();

        String[] value = constructor.toString().split("\\(");

        if (value[0].contains(".")) {
            buffer.append(value[0].substring(value[0].lastIndexOf(".") + 1));
        } else {
            buffer.append(value[0]);
        }
        buffer.append("(");

        if (value[1].contains(".")) {
            String params = value[1].replace(",", ", ");
            buffer.append(params.substring(params.lastIndexOf(".") + 1));
        } else {
            buffer.append(")");
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public String getConstructors(String className)
            throws NoSuchMethodException, SecurityException, Exception {

        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");

        Class c = Class.forName(className);
        Constructor[] constructors = c.getDeclaredConstructors();

        for (Constructor constructor : constructors) {
            buffer.append(parseConstructor(constructor));
        }
        return buffer.toString();
    }

    public String getMethods(String className)
            throws NoSuchMethodException, SecurityException, Exception {

        Class c = Class.forName(className);

        Method methods[] = c.getDeclaredMethods();

        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");

        for (Method method : methods) {

            int modifier = method.getModifiers();
            buffer.append(parseModifier(modifier));

            buffer.append(" ");

            String returnType = method.getGenericReturnType().toString();
            buffer.append(parseString(returnType));

            buffer.append(" ");

            buffer.append(method.getName());
            buffer.append("(");

            Class clazzes[] = method.getParameterTypes();

            int counter = 0;
            for (Class clazz : clazzes) {
                counter++;

                buffer.append(parseString(clazz.toString()));

                if (counter < clazzes.length) {
                    buffer.append(", ");
                }
            }

            buffer.append(")");
            buffer.append("\n");
        }

        return buffer.toString();
    }
}
