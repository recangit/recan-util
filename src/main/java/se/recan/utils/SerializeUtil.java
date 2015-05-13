package se.recan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/** 
 * Created: 2011-11-08
 * Last Modified: 2014-08-11
 * @author Anders Recksén (recan)
 */
 
public class SerializeUtil {
 
    public static void saveToFile(List<Object> items) {
        if (items.isEmpty()) {
            return;
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Items.bin"));
            out.writeObject(items);
            out.close();
        } catch (NotSerializableException nse) {
            System.err.println("SerializableException!");
            System.err.println(nse);
        } catch (IOException ioe) {
            System.err.println("IOException!");
            System.err.println(ioe);
        }
    }

    public static List<Object> readFromFile() {
        List<Object> items = new ArrayList<Object>();
        
        File file = new File("Items.bin");
        if (!file.exists()) {
            return items;
        }
                
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            items = (List<Object>) in.readObject();
        } catch (NotSerializableException nse) {
            System.err.println("SerializableException!");
            System.err.println(nse);
        } catch (ClassNotFoundException cne) {
            System.err.println("Class not found!");
            System.err.println(cne);
        } catch (FileNotFoundException fne) {
            System.err.println("File not found!");
            System.err.println(fne);
        } catch (IOException ioe) {
            System.err.println("IOException!");
            System.err.println(ioe);
        }
        
        return items;
    }
}