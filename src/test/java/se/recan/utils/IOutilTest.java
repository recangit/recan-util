package se.recan.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @date 2014-jul-26
 * @author Anders Recksén (recan)
 */
public class IOutilTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");

    @Test
    public void testIOutil() throws Exception {
        IOutil io = IOutil.getInstance();

//        ArrayList<String> list = io.fileWalk(new File("/home/recan/Dokument/NetBeans/arbetssokande"), new ArrayList<String>(), 0);
        ArrayList<String> list = io.fileWalk(new File("."), new ArrayList<String>(), 0);
        LOGGER.debug(io.printFileWalk(list));

        LOGGER.debug(IOutil.printAsterix("KAJSA o ANNIE"));

        LOGGER.debug(IOutil.print("Denna text:Denna text:Denna text.", ":", 11, 2));

        io.resetRowList();
        io.setDelimeter("\\|");
        io.addRow("Element|Variabelnamn|Metodnamn");
        io.addRow("Select|<namn>Select|select<Namn>");
        io.addRow("Button|<namn>Button|click<Namn>Button");
        io.addRow("Länkar|<namn>Link|click<Namn>Link");
        io.addRow("Inputfält|<namn>Input|set<Namn>Input");
        io.addRow("Resultatlistor|<namn>List|get<Namn>List");
        io.addRow("||get<Namn>ListSize");
        io.addRow("Sök resultat meddelande|searchMessage|getSearchMessage");
        LOGGER.debug(io.printRow());
        
        
        LOGGER.debug(IOutil.info("KALLE"));
        LOGGER.debug(IOutil.info(this));
        
        ArrayList<String> urls = IOutil.readHtmlSource("http://foretag.arbetsformedlingen.se/");
//        ArrayList<String> urls = IOutil.readHtmlSource("http://www.recksen.se");
        for(String url: urls) {
//            LOGGER.debug(url);
        }
    }

//    @Test
    public void testRow() throws Exception {
        IOutil io = IOutil.getInstance();

        io.setDelimeter("\\|");
        
        io.addRow("|Må|Ti|Ons|To|Fr");
        io.addRow("09.40unvisible");
        io.addRow("08.20|IDH AKO Idrott:KO2|SV JeS 4C|IDH Uv AKO Idrott:KO2/TK Jv SaN 4c|Lektion|Lektion");
        io.addRow("09.40||Musik|Rast|Rast|Rast");
        LOGGER.debug(io.printRow());
    }
    
    @Test
    public void testLocalRow() throws Exception {
        int tdNumb = 4;
        
        List<Row> list = new ArrayList<>();
        
//        list.add(new Row("Annies Schema", 6, 1));
//        
//        list.add(new Row("8"));
//        list.add(new Row("Måndag"));
//        list.add(new Row("Tisdag"));
//        list.add(new Row("Onsdag"));
//        list.add(new Row("Torsdag"));
//        list.add(new Row("Fredag"));
//        
//        list.add(new Row("8", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB, idrott udda veckor", 1, 1));
//        
//        list.add(new Row("8", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        
//        list.add(new Row("8", 1, 1));
//        list.add(new Row("Rast", 2, 1));
//        list.add(new Row("Idrott", 1, 1));
//        list.add(new Row("Rast", 2, 1));
//        
//        list.add(new Row("8", 1, 1));
//        list.add(new Row("A", 1, 1));
//        list.add(new Row("B", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        list.add(new Row("B", 1, 1));
//        list.add(new Row("AB", 1, 1));
//        
//        list.add(new Row("8", 1, 1));
//        list.add(new Row("Lunch", 5, 1));
        
        list.add(new Row("A"));
        list.add(new Row("B"));
        list.add(new Row("Cccccccc"));
        list.add(new Row("D"));
        
        list.add(new Row("Eeeee", 2, 1));
        list.add(new Row("Fff", 1, 1));
        list.add(new Row("Gg", 1, 1));
        
        list.add(new Row("H", 1, 2));
        list.add(new Row("I", 2, 1));
        list.add(new Row("J", 1, 1));
        
        list.add(new Row("K", 1, 1));
        list.add(new Row("L", 1, 1));
        list.add(new Row("M", 1, 1));
        
        int length = getWordLength(list);
        
        StringBuilder buffer = new StringBuilder(1000);
        
        buffer.append("\n");
        
        populate(buffer, tdNumb, length);

        int counter = 1;
        for(Row row: list) {

            buffer.append("|");
            
//            int above = counter - 1 - tdNumb;
//            int above = 0;
//            if(above > 0 && list.get(above).getRows() > 1) {
//                for(int l = 1; l < length; l++) {
//                    buffer.append("*");
//                }
//                counter++;
//                continue;
//            }
            
            String message = row.getMessage();
            buffer.append(message);
            
            int whiteSpace = length - message.length();
            for(int j = 0; j < whiteSpace; j++) {
                buffer.append(" ");
            }
            
            int columns = row.getColumns();
            
            if(columns > 0) {
                for(int k = 0; k <= columns*length; k++) {
                    buffer.append(" ");
                }
                for(int l = 1; l < columns; l++) {
                    buffer.append(" ");
                }
                counter = counter + columns;
            }
            
            if(counter % tdNumb == 0) {
                buffer.append("|");
                buffer.append("\n");
                populate(buffer, tdNumb, length);
            }
            
            counter++;
        }
        
        LOGGER.debug(buffer.toString());
    }
    
    void populate(StringBuilder buffer, int tdNumb, int length) {
        
        for(int i = 0; i < tdNumb; i++) {
            buffer.append("+");
            for(int j = 0; j < length; j++) {
                buffer.append("-");
            }
        }
        buffer.append("+");
        buffer.append("\n");
    }
    
    int getWordLength(List<Row> list) {
        int length = 0;
        for(Row row: list) {
            if(row.getMessageLength() > length) {
                length = row.getMessageLength();
            }
        }
        
        return length;
    }
    
    private class Row {

        String message;
        int messageLength;
        int columns = 1;
        int rows = 1;

        Row(String message) {
           this.message = message;
        }
        
        Row(String message, int columns, int rows) {
           this.message = message;
           this.columns = columns;
           this.rows = rows;
        }

        String getMessage() {
            return message;
        }
        
        int getMessageLength() {
            return message.length();
        }
        
        int getColumns() {
            return columns - 1;
        }

        int getRows() {
            return rows;
        }
    }
}
