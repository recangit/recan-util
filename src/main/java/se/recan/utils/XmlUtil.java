package se.recan.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @date 2014-aug-06
 * @author Anders Recksén (recan)
 */
public class XmlUtil {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    
    private Document document;
    private final SAXBuilder builder;
    private Element rootNode;
    private final File xmlFile;

    public XmlUtil(File xmlFile) throws JDOMException, IOException {
        this.xmlFile = xmlFile;
        this.builder = new SAXBuilder();
        if (xmlFile.exists()) {
            this.document = (Document) builder.build(xmlFile);
            this.rootNode = document.getRootElement();
        }
    }

    public boolean deleteXmlFile() throws Exception {
        if (xmlFile.exists()) {
            return xmlFile.delete();
        }

        return false;
    }
    
    public void toFile(Element elements, String root) throws Exception {
        if (!xmlFile.exists()) {
            createXmlFile(elements, root);
        } else {
            addXmlPost(elements);
        }
    }

    public void createXmlFile(Element elements, String root) throws Exception {
        Element element = new Element(root);
        element.addContent(elements);
        this.document = new Document(element);

        print();
    }
    
    public void addXmlPost(Element elements) throws Exception {
        rootNode.addContent(elements);

        print();
    }
    
    public void deleteXmlPost(String searchAttribute, String searchValue) throws Exception {
        Element element = searchXmlPost(searchAttribute, searchValue);
        rootNode.removeContent(element);
        
        print();
    }
    
    public Element searchXmlPost(String searchAttribute, String searchValue) throws Exception {
        Element result = null;

        for (Element e : rootNode.getChildren()) {
            if (e.getAttributeValue(searchAttribute).equals(searchValue)) {
                result = e;
            }
        }

        return result;
    }
    
    public void updateXmlPost(String searchAttribute, String searchValue, String searchChild, String value) throws Exception {
        Element e = searchXmlPost(searchAttribute, searchValue);
        e.getChild(searchChild).getAttribute("value").setValue(value);

        print();
    }

    /**
     * Populera en ArrayList med key-value värden.
     * Värdena är separerade med en delimeter (=).
     * Key är namnet på taggen.
     * Value antingen attributvärdet eller text mellan öppna/stäng taggen
     * @return List
     * @throws IOException 
     */
    public List<String> getXmlAsList() throws IOException {
        List<String> result = null;
        
        result = recursion(rootNode, new ArrayList<String>());
        
        return result;
    }
    
    /**
     * Returnera antal objekt som ligger under root-elementet
     * @return int
     * @throws IOException 
     */
    public int getNumberOfPosts() throws IOException {
            return document.getRootElement().getChildren().size();
    }

    /**
     * Returnera xml-filen med taggar och allt.
     * @return String
     * @throws Exception 
     */
    public String getXmlAsString() throws Exception {
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        
        return xout.outputString(document);
    }
    
    /**
     * Hur fungerar detta?
     * Returnera xml-filen med taggar och allt.
     * @param element
     * @return String
     * @throws Exception 
     */
    public String toString(Element element) throws Exception {
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        
        return xout.outputString(document);
    }

    private List<String> recursion(Element node, List<String> added) {
        List<String> result = added;
        
        List<Element> elements = node.getChildren();
        
        for (Element e : elements) {
            String key = e.getName();
            String value = e.getTextTrim();
                    
            List<Attribute> attributes = e.getAttributes();
            if(!attributes.isEmpty()) {
                value = attributes.get(0).getValue();
            }
            
            if(!value.isEmpty()) {
                result.add(key + "=" + value);
            }
            if (e.getChildren() != null) {
                recursion(e, result);
            }
        }
        
        return result;
    }
    
    private void print() throws IOException {
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setIndent("    ");
        format.setExpandEmptyElements(false);
        format.setOmitDeclaration(false);
        XMLOutputter xout = new XMLOutputter(format);

        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(xmlFile), "UTF-8"));
        try {
            xout.output(document, writer);
        } finally {
            writer.close();
        }
    }
}
