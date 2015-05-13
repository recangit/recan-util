package se.recan.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.net.URL;
import java.net.URLConnection;
import java.io.FilenameFilter;

/**
 * Encoding iso-8859-1 används men borde väl vara UTF-8?
 * 
 * Created: 2009-09-15 Last Modified: 2012-10-14
 *
 * @author Anders Recksén (anders[at]recksen[dot]se)
 */
public class IOutil {

    private static IOutil instance = null;
    private SuffixFilter filter = null;
    private String delimeter;
    private String unvisible;
    private static ArrayList<Row> rowList = null;

    /**
     * @return instance of this class.
     */
    public static IOutil getInstance() {
        if (instance == null) {
            instance = new IOutil();
        }
        return instance;
    }

    // Vilken funktion fyller argumenten?
    public static IOutil getInstance(String delimeter, String unvisible) {
        if (instance == null) {
            instance = new IOutil(delimeter, unvisible);
        }
        return instance;
    }

    /**
     * Private constructor as this is a singleton.
     */
    private IOutil() {
        filter = new SuffixFilter();
    }

    /**
     * Private constructor as this is a singleton.
     */
    private IOutil(String delimeter, String unvisible) {
        this.delimeter = delimeter;
        this.unvisible = unvisible;
        filter = new SuffixFilter();
    }

    public void setDelimeter(String delimeter) {
        this.delimeter = delimeter;
    }

    public static String fromByteFile(File file) throws IOException {
        FileInputStream in = null;
        StringBuilder buffer = new StringBuilder();
        try {
            in = new FileInputStream(file);

            // read kan läsa 0 t.o.m 255
            // När strömmen kommit till sitt slut returneras -1
            for (int i = in.read(); i != -1; i = in.read()) {
                buffer.append((char) i);
            }

            in.close();

            // Funktionaliteten, ex buffring, kan utökas genom att koppla till ytterligare en strömm.
            // BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException fnfe) {
            System.err.println(file + " not found");
        } catch (IOException ioe) {
            System.err.println("Could not read from " + file);
        } finally {
            if(in != null) {
                in.close();
            }
        }
        return buffer.toString();
    }

    /**
     * Read a file into a ArrayList. Use the "magic" InputStreamReader to
     * translate 8 bitars byte to 16 bitars Unicode
     *
     * @param file File
     * @return 
     * @throws Exception
     */
    public static ArrayList<String> readFile(File file) throws Exception {
        ArrayList<String> array = new ArrayList<String>();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("iso-8859-1")));

            while (true) {
                String line = reader.readLine();

                if (line == null) {
                    break;
                }

                array.add(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.getMessage();
                }
            }
        }

        return array;
    }

    /**
     * @param directory
     * @param added
     * @param ind
     * @return 
     * @throws Exception
     */
    public ArrayList<String> fileWalk(File directory, ArrayList<String> added, int ind) throws Exception {
        if (directory.exists() == false) {
            System.out.println("Directory: " + directory + " doesn't exist");
        }

        if (added.isEmpty()) {
            String rootDir = directory.getCanonicalPath();

            // Get rid of first slash (/)
            rootDir = rootDir.substring(1);

            added.add("0," + rootDir + ",D");
        }

        ArrayList<String> result = added;

        File[] subs = directory.listFiles(filter);

        int indent = ind;
        indent++;

        for (File sub : subs) {
            if (sub.isDirectory()) {
                result.add(indent + "," + sub.getName() + ",D");
                fileWalk(new File(sub.toString()), result, indent);
            }
            if (sub.isFile() && !sub.isHidden()) {
                result.add(indent + "," + sub.getName() + ",F");
            }
        }

        indent--;

        return result;
    }

    // Without recursion
    public ArrayList<String> fileWalk(File directory) throws Exception {
        if (directory.exists() == false) {
            System.out.println("Directory: " + directory + " doesn't exist");
        }

        ArrayList<String> result = new ArrayList<String>();

        File[] subs = directory.listFiles(filter);

        for (File sub : subs) {
            if (sub.isFile() && !sub.isHidden()) {
                if (sub.getName().endsWith(".jpg") || sub.getName().endsWith(".JPG")) {
                    result.add(sub.getName());
                }
            }
        }

        return result;
    }

    /**
     *
     * @param host
     * @return
     * @throws Exception
     */
    public static ArrayList<String> readHtmlSource(String host) throws Exception {
        ArrayList<String> array = new ArrayList<String>();

        BufferedReader reader = null;

        try {
            URL u = new URL(host);
            URLConnection c = u.openConnection();

            reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "iso-8859-1"));
            String line = null;

            while ((line = reader.readLine()) != null) {
                array.add(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.getMessage();
                }
            }
        }

        return array;
    }

    /**
     * Write a ArrayList to file.
     *
     * @param file File
     * @param array
     */
    public static void writeFile(File file, ArrayList<String> array) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("iso-8859-1"))));

            for (String s : array) {
                out.println(s);
            }
        } catch (FileNotFoundException ioe) {
            ioe.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Write a Enumeration to file.
     *
     * @param file File
     * @param e Enumeration
     */
    public static void writeFile(File file, Enumeration e) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("iso-8859-1"))));

            while (e.hasMoreElements()) {
                String tmp = (String) e.nextElement();
                out.println(tmp + " = " + System.getProperty(tmp));
            }
        } catch (FileNotFoundException ioe) {
            ioe.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public static void writeFile(File file, String[][] string) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("iso-8859-1"))));

            for (String[] string1 : string) {
                for (String item : string1) {
                    out.print(item);
                }
                out.println("");
            }

        } catch (FileNotFoundException ioe) {
            ioe.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Write String to file. If argument append is true the value is appended to
     * previous text in file.
     *
     * @param file File
     * @param value String
     * @param append boolean
     */
    public static void writeFile(File file, String value, boolean append) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), Charset.forName("iso-8859-1"))));
            out.println(value);
        } catch (FileNotFoundException ioe) {
            ioe.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Write text to console as Latin-1.
     *
     * @param string
     */
    public static void print(String string) {
        try {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
//            PrintStream out = new PrintStream(System.out, true, "iso-8859-1");
            out.println(string);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Exempel: Anders:Kajsa:Annie, ":", 10, 2 Skriver ut:
     * Anders(4 mellanslag)Kajsa(radbrytning)
     * Annie
     *
     * @param string String to print.
     * @param delimeter String describing ...
     * @param length int describing column length.
     * @param lineBreak int describing row length.
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String print(String string, String delimeter, int length, int lineBreak) throws UnsupportedEncodingException {
        StringBuilder buffer = new StringBuilder(1000);
        buffer.append("\n");

        String[] split = string.split(delimeter);

        int counter = 1;
        for (String s : split) {
            int difference = length - s.length();
            buffer.append(s);
            for (int i = 1; i <= difference; i++) {
                buffer.append(" ");
            }
            if (counter % lineBreak == 0) {
                counter = 0;
                buffer.append("\n");
            }
            counter++;
        }

        return buffer.toString();
    }

    public static String printAsterix(String string) {

        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        string = string.toUpperCase();
        int length = (string.length() * 2) + 3;

        for (int i = 0; i < length; i++) {
            builder.append("*");
        }
        builder.append("\n");

        char[] stringArray = string.toCharArray();
        builder.append("* ");
        for (char c : stringArray) {
            builder.append(c);
            builder.append(" ");
        }
        builder.append("*");
        builder.append("\n");

        for (int i = 0; i < length; i++) {
            builder.append("*");
        }

        return builder.toString();
    }

    /**
     * Print a Array describing a Directory tree to screen.
     *
     * @param list
     * @return 
     */
    public String printFileWalk(ArrayList<String> list) {
        StringBuilder buffer = new StringBuilder();

        for (String s : list) {
            String[] split = s.split(",");
            int indent = Integer.parseInt(split[0]);

            String result = "";
            for (int i = 0; i < indent; i++) {
                result = result + "| ";
                buffer.append("| ");
            }
            if (split[2].equals("D")) {
                result = result + "+-";
                buffer.append("+-");
            } else {
                result = result + "|-";
                buffer.append("|-");
            }

            result = result + split[1];
            buffer.append(split[1]);
            buffer.append("\n");

            System.out.println(result);
        }

        return buffer.toString();
    }

    public void addRow(String string) {
        if (rowList == null) {
            rowList = new ArrayList<Row>();
        }

        Row row = new Row(string);
        rowList.add(row);
    }

    public void resetRowList() {
        rowList = new ArrayList<Row>();
    }
    
    public String printRow() throws UnsupportedEncodingException {
        int[] columnLength = parseRow();

        StringBuilder buffer = new StringBuilder(1000);
        buffer.append("\n");

        buffer.append(getHorizontal(columnLength));

        for (Row row : rowList) {
            String[] split = row.getSplit();

            for (int j = 0; j < split.length; j++) {
                int wordLength = columnLength[j] - split[j].length();
                buffer.append("| ");

                if (split[j].equals(unvisible)) {
                    buffer.append(" ");
                } else {
                    buffer.append(split[j]);
                }
                for (int k = 0; k < wordLength; k++) {
                    buffer.append(" ");
                }
                buffer.append(" ");
            }
            buffer.append("|");
            buffer.append("\n");
            buffer.append(getHorizontal(columnLength));
        }

        return buffer.toString();
    }

    private String getHorizontal(int[] columnLength) {
        StringBuilder buffer = new StringBuilder(1000);

        buffer.append("+");
        for (int total = 0; total < columnLength.length; total++) {
            for (int t = 0; t < columnLength[total]; t++) {
                buffer.append("-");
            }
            buffer.append("--+");
        }
        buffer.append("\n");

        return buffer.toString();
    }

    private int[] parseRow() {
        Row firstRow = rowList.get(0);
        int rowLength = firstRow.getRowLength();
        int[] columnLength = new int[rowLength];

        for (Row row : rowList) {
            int[] cLength = row.getColumnLength();
            for (int j = 0; j < cLength.length; j++) {
                if (cLength[j] > columnLength[j]) {
                    columnLength[j] = cLength[j];
                }
            }
        }

        return columnLength;
    }

    @Override
    public String toString() {
        return "Read and write to file with Charset iso-8859-1.";
    }

    private class Row {

        String[] split;
        int rowLength;
        int[] columnLength;

        Row(String message) {
            split = message.split(delimeter);
            rowLength = split.length;
            columnLength = new int[rowLength];

            for (int i = 0; i < rowLength; i++) {
                int l = split[i].length();
                columnLength[i] = l;
            }
        }

        String[] getSplit() {
            return split;
        }

        int getRowLength() {
            return rowLength;
        }

        int[] getColumnLength() {
            return columnLength;
        }
    }

    public static String info(Object obj) {
        return info(obj.getClass().getSimpleName());
    }

    public static String info(String arg) {
        StringBuilder buffer = new StringBuilder();
        
        int l = arg.length();
        buffer.append("\n+--");

        for (int i = 0; i < l; i++) {
            buffer.append("-");
        }
        buffer.append("--+\n");
        buffer.append("|");
        for (int i = 0; i < l + 4; i++) {
            buffer.append(" ");
        }
        buffer.append("|\n");
        buffer.append("|  ");
        buffer.append(arg);
        buffer.append("  |\n");
        buffer.append("|");
        for (int i = 0; i < l + 4; i++) {
            buffer.append(" ");
        }
        buffer.append("|\n");
        buffer.append("+--");

        for (int i = 0; i < l; i++) {
            buffer.append("-");
        }
        buffer.append("--+");
        return buffer.toString();
    }

    static class SuffixFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {

            File file = new File(dir, name);

            return !file.isHidden();
        }
    }
}
