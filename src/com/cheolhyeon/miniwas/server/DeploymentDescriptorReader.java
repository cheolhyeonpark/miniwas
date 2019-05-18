package com.cheolhyeon.miniwas.server;

import java.io.*;
import java.util.ArrayList;

public class DeploymentDescriptorReader {

    private static final String WEB_INF = "WEB-INF";
    private static final String DEPLOYMENT_DESCRIPTOR = "web.xml";
    private static final String LEFT_ANGLE_BRACKET = "<";
    private static final String RIGHT_ANGLE_BRACKET = ">";
    private static final String SLASH = "/";

    private String appName;
    private String deploymentDescriptor;

    public DeploymentDescriptorReader() {}

    public DeploymentDescriptorReader(String appPath) {
        this.appName = appPath.substring(appPath.lastIndexOf(SLASH));
        deploymentDescriptor = getDeploymentDescriptor(appPath + File.separator + WEB_INF + File.separator + DEPLOYMENT_DESCRIPTOR);
    }

    public String getDeploymentDescriptor(String filePath) {
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
        return getElements(elementName, deploymentDescriptor);
    }

    public ArrayList<String> getElements(String elementName, String deploymentDescriptor) {
        ArrayList<String> elements = new ArrayList<>();
        String startTag = getStartTag(elementName);
        String endTag = getEndTag(elementName);
        int startIdx = 0;
        int endIdx = 0;
        while ((startIdx = deploymentDescriptor.indexOf(startTag, endIdx)) != -1) {
            startIdx += startTag.length();
            endIdx = deploymentDescriptor.indexOf(endTag, startIdx);
            String element = deploymentDescriptor.substring(startIdx, endIdx);
            elements.add(element);
        }
        return elements;
    }

    public String getElement(String elementName) {
        return getElement(elementName, deploymentDescriptor);
    }

    public String getElement(String elementName, String deploymentDescriptor) {
        String startTag = getStartTag(elementName);
        String endTag = getEndTag(elementName);
        return deploymentDescriptor.substring(deploymentDescriptor.indexOf(startTag) + startTag.length()
                , deploymentDescriptor.indexOf(endTag));
    }

    private String getStartTag(String elementName) {
        return LEFT_ANGLE_BRACKET + elementName + RIGHT_ANGLE_BRACKET;
    }

    private String getEndTag(String elementName) {
        return LEFT_ANGLE_BRACKET + SLASH + elementName + RIGHT_ANGLE_BRACKET;
    }

    public String getAppName() {
        return appName;
    }
}
