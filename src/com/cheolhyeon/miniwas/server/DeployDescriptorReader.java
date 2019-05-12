package com.cheolhyeon.miniwas.server;

import java.io.*;
import java.util.ArrayList;

public class DeployDescriptorReader {

    private static final String LEFT_ANGLE_BRACKET = "<";
    private static final String RIGHT_ANGLE_BRACKET = ">";
    private static final String SLASH = "/";

    private String deployDescriptor;

    public DeployDescriptorReader() {}

    public DeployDescriptorReader(String filePath) {
        deployDescriptor = getDeployDescriptor(filePath);
        System.out.println(deployDescriptor);
    }

    public String getDeployDescriptor(String filePath) {
        String xml = "";
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                xml += line.trim();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return xml;
    }

    public ArrayList<String> getElements(String elementName) {
        return getElements(elementName, deployDescriptor);
    }

    public ArrayList<String> getElements(String elementName, String deployDescriptor) {
        ArrayList<String> elements = new ArrayList<>();
        String startTag = getStartTag(elementName);
        String endTag = getEndTag(elementName);
        int startIdx = 0;
        int endIdx = 0;
        while ((startIdx = deployDescriptor.indexOf(startTag, endIdx)) != -1) {
            startIdx += startTag.length();
            endIdx = deployDescriptor.indexOf(endTag, startIdx);
            String element = deployDescriptor.substring(startIdx, endIdx);
            elements.add(element);
        }
        return elements;
    }

    public String getElement(String elementName) {
        return getElement(elementName, deployDescriptor);
    }

    public String getElement(String elementName, String deployDescriptor) {
        String startTag = getStartTag(elementName);
        String endTag = getEndTag(elementName);
        return deployDescriptor.substring(deployDescriptor.indexOf(startTag) + startTag.length()
                , deployDescriptor.indexOf(endTag));
    }

    private String getStartTag(String elementName) {
        return LEFT_ANGLE_BRACKET + elementName + RIGHT_ANGLE_BRACKET;
    }

    private String getEndTag(String elementName) {
        return LEFT_ANGLE_BRACKET + SLASH + elementName + RIGHT_ANGLE_BRACKET;
    }
}
