package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil {

    public static DocumentBuilder getDocumentBuilder() {
        DocumentBuilder parser = null;
        try {
            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
            df.setNamespaceAware(true);
            parser = df.newDocumentBuilder();
            return parser;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Document load(String filePath) throws SAXException, IOException {
        DocumentBuilder parser = getDocumentBuilder();
        InputStream in = new FileInputStream(filePath);
        return parser.parse(in);
    }

    public static List<Node> loadAllNodes(Document document) throws Exception{
        NodeList nodeList = document.getChildNodes();
        ArrayList<Node> nodes = new ArrayList<>();
        return loadNode(nodeList, nodes, true);
    }

    public static List<Node> loadNodeList(Document document, String tagName) throws Exception{
        NodeList nodeList = document.getElementsByTagName(tagName);
        ArrayList<Node> nodes = new ArrayList<>();
        return loadNode(nodeList, nodes, false);
    }

    public static List<Node> loadNode(NodeList nodeList, List<Node> nodes, boolean needDeep) throws Exception{
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if(needDeep) {
                if (item.hasChildNodes()) {
                    loadNode(item.getChildNodes(), nodes, needDeep);
                } else {
                    nodes.add(item);
                }
            }else {
                nodes.add(item);
            }
        }
        return nodes;
    }

    public static List<Node> loadAllAttr(Document document, String attrTagName) throws Exception{
        NodeList nodeList = document.getChildNodes();
        ArrayList<Node> nodes = new ArrayList<>();
        return loadAttr(nodeList, nodes, true, attrTagName);
    }

    public static List<Node> loadAttrList(Document document, String tagName, String attrTagName) throws Exception{
        NodeList nodeList = document.getElementsByTagName(tagName);
        ArrayList<Node> nodes = new ArrayList<>();
        return loadAttr(nodeList, nodes, false, attrTagName);
    }

    public static List<Node> loadAttr(NodeList nodeList, List<Node> nodes, boolean needDeep, String attrTagName) throws Exception{
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if(needDeep) {
                if (item.hasChildNodes()) {
                    loadNode(item.getChildNodes(), nodes, needDeep);
                } else {
                    if(item.hasAttributes()) {
                        NamedNodeMap attributes = item.getAttributes();
                        Node namedItem = attributes.getNamedItem(attrTagName);
                        if(namedItem != null) {
                            nodes.add(namedItem);
                        }
                    }
                }
            }else {
                if(item.hasAttributes()) {
                    NamedNodeMap attributes = item.getAttributes();
                    Node namedItem = attributes.getNamedItem(attrTagName);
                    if(namedItem != null) {
                        nodes.add(namedItem);
                    }
                }
            }
        }
        return nodes;
    }

    public static void toFile(Document doc, String file) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    public static String toString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        Writer writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }
    
    public static void renameNamespaceRecursive(Node node, String namespace)
    {
        Document document = node.getOwnerDocument();
        if (node.getNodeType() == Node.ELEMENT_NODE)
        {
            document.renameNode(node, namespace, node.getNodeName());
        }
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i)
        {
            renameNamespaceRecursive(list.item(i), namespace);
        }
    }

}
