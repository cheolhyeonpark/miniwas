package com.cheolhyeon.miniwas.server;

import com.cheolhyeon.miniwas.lib.HTTPServletRequest;
import com.cheolhyeon.miniwas.lib.HTTPServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class Container {

    private static final int PORT_NUMBER = 3636;
    private static final String APP_ROOT = "../../app";
    private static final String WEB_INF = "WEB_INF";
    private static final String DEPLOYMENT_DESCRIPTOR = "web.xml";

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

            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            Class clazz = urlClassLoader.loadClass("ClassLoader");
            Object object = clazz.newInstance();
            System.out.println(object.toString());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void init() {

    }

    private void readDeployDescriptor() {
        File file = new File(APP_ROOT);
        String[] fileNames = file.list();
        for (String fileName : fileNames) {
            String appPath = APP_ROOT + File.separator + fileName;
            File application = new File(appPath);
            if (application.isDirectory()) {
                DeploymentDescriptorReader reader = new DeploymentDescriptorReader(appPath
                        + File.separator + WEB_INF + File.separator + DEPLOYMENT_DESCRIPTOR);
            }

        }
    }
}
