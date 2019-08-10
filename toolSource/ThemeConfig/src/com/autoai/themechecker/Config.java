package com.autoai.themechecker;

import com.autoai.themechecker.util.IOUtil;
import com.autoai.themechecker.xmlbean.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

public class Config {
    public static final String PLATFORM_D9 = "D9";
    public static final String PLATFORM_S5 = "S5";
    private static String platform;
    private static String config_path;
    private static String values_path;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("path ars error");
            return;
        }
        platform = args[0];
        config_path = args[1];
        values_path = args[2];
        File curr = new File(".");
        System.out.println("current_path=" + curr.getAbsolutePath());
        System.out.println("config_path=" + config_path);
        System.out.println("values_path=" + values_path);

        Properties properties = prepareKeyAndValues();
        if (properties != null) {
            copyColors(properties);
            copyDimens(properties);
        }
    }

    private static Properties prepareKeyAndValues() {
        File file = new File(config_path);
        if (!file.exists()) {
            System.out.println("config_path ars error, config_path=" + config_path);
            return null;
        }
        String propertyPath = "tmp.properties";
        try {
            IOUtil.fileCopy(config_path, propertyPath);
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
        if(PLATFORM_D9.equals(platform)) {
            //例如
//            String xmlColor1 = "main_text_normal";
//            String xmlColor2 = "main_text_pressed";
//            String color1 = "player_sphere_color_n";
//            String color2 = "player_sphere_color_c";
//            String property1 = properties.getProperty(color1);
//            String property2 = properties.getProperty(color2);
//            properties.setProperty(xmlColor1, property1);
//            properties.setProperty(xmlColor2, property2);
//            properties.remove(color1);
//            properties.remove(color2);

        }if (PLATFORM_S5.equals(platform)){

        }
    }

    public static final String TAG_COLOR = "color";
    public static final String TAG_DIMEN= "dimen";
    public static final String ATTR_NAME = "name";
    private static void copyColors(Properties properties) {
        String colorXml = values_path + "/colors.xml";
        try {
            Document document = XmlUtil.load(colorXml);
            List<Node> colors = XmlUtil.loadNodeList(document, TAG_COLOR);
            List<Node> nodes = XmlUtil.loadAttrList(document, TAG_COLOR, ATTR_NAME);
            for (Node color : colors) {
                System.out.println("--------------");
                Node colorFirstChild = color.getFirstChild();
                System.out.println("color ChildNode.value=" + colorFirstChild.getNodeValue());
                if(color.hasAttributes()) {
                    NamedNodeMap attributes = color.getAttributes();
                    Node namedItem = attributes.getNamedItem(ATTR_NAME);
                    for (Node node : nodes) {
                        if (node.equals(namedItem)) {
                            System.out.println("attr = "+ node.getNodeValue());
                            if (properties.containsKey(node.getNodeValue())) {
                                String property = properties.getProperty(node.getNodeValue());
                                System.out.println("properties value = " + property);
                                colorFirstChild.setNodeValue(property);
                                System.out.println("color ChildNode.value=" + colorFirstChild.getNodeValue());
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

    private static void copyDimens(Properties properties) {
        String dimenXml = values_path + "/dimens.xml";
        try {
            Document document = XmlUtil.load(dimenXml);
            List<Node> dimens = XmlUtil.loadNodeList(document, TAG_DIMEN);
            List<Node> nodes = XmlUtil.loadAttrList(document, TAG_DIMEN, ATTR_NAME);
            for (Node dimen : dimens) {
                System.out.println("--------------");
                Node dimenFirstChild = dimen.getFirstChild();
                System.out.println("dimen ChildNode.value=" + dimenFirstChild.getNodeValue());
                if(dimen.hasAttributes()) {
                    NamedNodeMap attributes = dimen.getAttributes();
                    Node namedItem = attributes.getNamedItem(ATTR_NAME);
                    for (Node node : nodes) {
                        if (node.equals(namedItem)) {
                            System.out.println("attr = "+ node.getNodeValue());
                            if (properties.containsKey(node.getNodeValue())) {
                                String property = properties.getProperty(node.getNodeValue());
                                System.out.println("properties value = " + property);
                                dimenFirstChild.setNodeValue(property+"dp");
                                System.out.println("dimen ChildNode.value=" + dimenFirstChild.getNodeValue());
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
