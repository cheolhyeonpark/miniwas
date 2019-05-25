package com.cheolhyeon.miniwas.server;

import com.cheolhyeon.miniwas.lib.Servlet;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ServletMap {

    private static final String APP_ROOT = "../../app";
    private Map<String, String> urlMap;
    private Map<String, String> classMap;
    private Map<String, String> servletMap;
    private Map<String, Servlet> instanceMap;

    public ServletMap() {
        init();
    }

    private void init() {
        urlMap = new HashMap<>();
        classMap = new HashMap<>();
        servletMap = new HashMap<>();
        instanceMap = new HashMap<>();
    }

    public void putUrlMap(String servletName, String urlName) {
        urlMap.put(servletName, urlName);
    }

    public void putClassMap(String servletName, String className) {
        classMap.put(servletName, className);
    }

    public void putServletMap(String urlName, String className) {
        servletMap.put(urlName, className);
    }

    public void mapServlet() {
        for (String servletName : urlMap.keySet()) {
            servletMap.put(urlMap.get(servletName), classMap.get(servletName));
        }
    }

    public boolean isServlet(String urlPath) {
        return servletMap.keySet().contains(urlPath);
    }

    public void loadClass(String urlPath) {
        if (hasInstance(urlPath)) {
            return;
        }
        try {
            File file = new File(APP_ROOT + servletMap.get(urlPath));
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.getParentFile().toURI().toURL()});
            Class clazz = urlClassLoader
                    .loadClass(servletMap.get(urlPath).substring(servletMap.get(urlPath).lastIndexOf(File.separator)+1));
            Servlet instance = (Servlet) clazz.newInstance();
            instanceMap.put(urlPath, instance);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private boolean hasInstance(String urlPath) {
        return instanceMap.keySet().contains(urlPath);
    }

    public Servlet getInstance(String urlPath) {
        return instanceMap.get(urlPath);
    }
}
