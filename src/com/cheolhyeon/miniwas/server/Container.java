package com.cheolhyeon.miniwas.server;

import com.cheolhyeon.miniwas.lib.HTTPServletRequest;
import com.cheolhyeon.miniwas.lib.HTTPServletResponse;
import com.cheolhyeon.miniwas.lib.Servlet;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Container {

    private static final int PORT_NUMBER = 3636;
    private static final String APP_ROOT = "../../app";
    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String CLASS_MAP_ELEMENT = "servlet";
    private static final String CLASS_MAP_KEY = "servlet-name";
    private static final String CLASS_MAP_VALUE = "servlet-class";
    private static final String URL_MAP_ELEMENT = "servlet-mapping";
    private static final String URL_MAP_KEY = "servlet-name";
    private static final String URL_MAP_VALUE = "url-pattern";
    private static final String WEB_INF = "WEB-INF";
    private static final String CLASSES = "classes";

    ServletMap servletMap;

    public void run() {
        try {
            init();

            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            Socket socket = serverSocket.accept();

            HTTPServletRequest request = new HTTPServletRequest();
            RequestReader requestReader = new RequestReader(request);
            requestReader.run(socket.getInputStream());

            String url = request.getRequestUrl();
            File file = new File(APP_ROOT + url);
            if (file.isFile()) {
                HTTPServletResponse response = new HTTPServletResponse(file, socket.getOutputStream());
                response.setFileContentType();
                response.sendFile();
                return;
            }
            if (servletMap.isServlet(url)) {
                HTTPServletResponse response = new HTTPServletResponse(socket.getOutputStream());
                servletMap.loadClass(url);
                Servlet servlet = servletMap.getInstance(url);
                servlet.init();
                servlet.service(request, response);
                servlet.destroy();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        servletMap = new ServletMap();
        List<DeploymentDescriptorReader> deploymentDescriptorReaders = getDeploymentDescriptorReaders();
        mapServlet(deploymentDescriptorReaders);
    }

    private void mapServlet(List<DeploymentDescriptorReader> deploymentDescriptorReaders) {
        for (DeploymentDescriptorReader reader : deploymentDescriptorReaders) {
            setElementMap(reader, CLASS_MAP_ELEMENT);
            setElementMap(reader, URL_MAP_ELEMENT);
        }
        servletMap.mapServlet();
    }

    private void setElementMap(DeploymentDescriptorReader reader, String element) {
        ArrayList<String> servlets = reader.getElements(element);
        for (String servlet : servlets) {
            String key = reader.getAppName() + SLASH + reader.getElement(getKey(element), servlet);
            String value = reader.getAppName() + SLASH + reader.getElement(getValue(element), servlet);
            putElementMap(element, key, value);
        }
    }

    private void putElementMap(String element, String key, String value) {
        if (CLASS_MAP_ELEMENT.equals(element)) {
            servletMap.putClassMap(key, File.separator + value.substring(1)
                    .replace(SLASH, File.separator + WEB_INF + File.separator + CLASSES + File.separator)
                    .replace(DOT, File.separator));
            return;
        }
        servletMap.putUrlMap(key, value);
    }

    private String getKey(String element) {
        if (CLASS_MAP_ELEMENT.equals(element)) {
            return CLASS_MAP_KEY;
        }
        return URL_MAP_KEY;
    }

    private String getValue(String element) {
        if (CLASS_MAP_ELEMENT.equals(element)) {
            return CLASS_MAP_VALUE;
        }
        return URL_MAP_VALUE;
    }

    private List<DeploymentDescriptorReader> getDeploymentDescriptorReaders() {
        List<DeploymentDescriptorReader> deploymentDescriptorReaders = new ArrayList<>();
        File file = new File(APP_ROOT);
        String[] fileNames = file.list();
        for (String fileName : fileNames) {
            String appPath = APP_ROOT + File.separator + fileName;
            File application = new File(appPath);
            if (application.isDirectory()) {
                deploymentDescriptorReaders.add(new DeploymentDescriptorReader(appPath));
            }
        }
        return deploymentDescriptorReaders;
    }
}
