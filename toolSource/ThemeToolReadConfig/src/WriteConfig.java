import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import utils.IOUtil;
import utils.XmlUtil;

public class WriteConfig {
    private static String sPlatform;
    private static String sConfig_path;
    private static String sValues_path;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("path ars error");
            return;
        }
        sPlatform = args[0];
        sConfig_path = args[1];
        sValues_path = args[2];
        File curr = new File(".");
        System.out.println("current_path=" + curr.getAbsolutePath());
        System.out.println("sConfig_path=" + sConfig_path);
        System.out.println("sValues_path=" + sValues_path);

        Properties properties = prepareKeyAndValues();
        if (properties != null) {
            copyColors(properties, sValues_path + "/colors.xml");
            copyDimens(properties, sValues_path + "/dimens.xml");
        }
    }

    private static Properties prepareKeyAndValues() {
        File file = new File(sConfig_path);
        if (!file.exists()) {
            System.out.println("sConfig_path ars error, sConfig_path=" + sConfig_path);
            return null;
        }
        String propertyPath = "tmp.properties";
        try {
            IOUtil.fileCopy(sConfig_path, propertyPath);
            FileInputStream fis = new FileInputStream(propertyPath);
            Properties properties = new Properties();
            properties.load(fis);
            processPlatform(properties);
            System.out.println("read tmp.properties success");
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean delete = new File(propertyPath).delete();
            if (delete) {
                System.out.println("delete tmp.properties success");
            }
        }
        return null;
    }

    private static void processPlatform(Properties properties) {
        //适配平台
        if(Platform.PLATFORM_D9.equals(sPlatform)) {

        }if (Platform.PLATFORM_S5.equals(sPlatform)){

        }
    }

    public static final String TAG_COLOR = "color";
    public static final String TAG_DIMEN= "dimen";
    public static final String TAG_INTEGER= "integer";
    public static final String ATTR_NAME = "name";
	public static void copyColors(Properties properties, String colorXml) {
        try {
            Document document = XmlUtil.load(colorXml);
            List<Node> colors = XmlUtil.loadNodeList(document, TAG_COLOR);
            List<Node> nodes = XmlUtil.loadAttrList(document, TAG_COLOR, ATTR_NAME);
            for (Node color : colors) {
                System.out.println("**********************");
                Node colorFirstChild = color.getFirstChild();
                String oldValue = colorFirstChild.getNodeValue();
                if(color.hasAttributes()) {
                    NamedNodeMap attributes = color.getAttributes();
                    Node namedItem = attributes.getNamedItem(ATTR_NAME);
                    for (Node node : nodes) {
                        if (node.equals(namedItem)) {
                            String nodeValue = node.getNodeValue();
                            System.out.println("attr.name = "+ nodeValue);
                            if (properties.containsKey(nodeValue)) {
                                String property = properties.getProperty(nodeValue);
                                colorFirstChild.setNodeValue(property);
                                System.out.println("change value from:" + oldValue + " to:" + colorFirstChild.getNodeValue());
                            }
                            break;
                        }
                    }
                }
            }
            XmlUtil.toFile(document, colorXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void copyDimens(Properties properties, String dimenXml) {
        try {
            Document document = XmlUtil.load(dimenXml);
            List<Node> dimens = XmlUtil.loadNodeList(document, TAG_DIMEN);
            List<Node> nodes = XmlUtil.loadAttrList(document, TAG_DIMEN, ATTR_NAME);
            for (Node dimen : dimens) {
                System.out.println("**********************");
                Node dimenFirstChild = dimen.getFirstChild();
                String oldValue = dimenFirstChild.getNodeValue();
                if(dimen.hasAttributes()) {
                    NamedNodeMap attributes = dimen.getAttributes();
                    Node namedItem = attributes.getNamedItem(ATTR_NAME);
                    for (Node node : nodes) {
                        if (node.equals(namedItem)) {
                            String nodeValue = node.getNodeValue();
                            System.out.println("attr.name = "+ nodeValue);
                            if (properties.containsKey(nodeValue)) {
                                String property = properties.getProperty(nodeValue);
                                if(property.endsWith("px")){
									dimenFirstChild.setNodeValue(property);
								}else {
									dimenFirstChild.setNodeValue(property+"px");
								}
                                System.out.println("change value from:" + oldValue + " to:" + dimenFirstChild.getNodeValue());
                            }
                            break;
                        }
                    }
                }
            }

            //integer
            List<Node> integers = XmlUtil.loadNodeList(document, TAG_INTEGER);
            List<Node> intnodes = XmlUtil.loadAttrList(document, TAG_INTEGER, ATTR_NAME);
            for (Node integer : integers) {
                System.out.println("**********************");
                Node intFirstChild = integer.getFirstChild();
                String oldValue = intFirstChild.getNodeValue();
                if(integer.hasAttributes()) {
                    NamedNodeMap attributes = integer.getAttributes();
                    Node namedItem = attributes.getNamedItem(ATTR_NAME);
                    for (Node node : intnodes) {
                        if (node.equals(namedItem)) {
                            String nodeValue = node.getNodeValue();
                            System.out.println("attr.name = "+ nodeValue);
                            if (properties.containsKey(nodeValue)) {
                                String property = properties.getProperty(nodeValue);
                                intFirstChild.setNodeValue(property);
                                System.out.println("change value from:" + oldValue + " to:" + intFirstChild.getNodeValue());
                            }
                            break;
                        }
                    }
                }
            }
            XmlUtil.toFile(document, dimenXml);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
