package com.cheolhyeon.miniwas.server;

import java.util.HashMap;
import java.util.Map;

public class ServletMap {

    private Map<String, String> urlMap;
    private Map<String, String> classMap;
    private Map<String, String> servletMap;

    public ServletMap() {
        init();
    }

    private void init() {
        urlMap = new HashMap<>();
        classMap = new HashMap<>();
        servletMap = new HashMap<>();
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
}
