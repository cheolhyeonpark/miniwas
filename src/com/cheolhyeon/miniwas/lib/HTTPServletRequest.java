package com.cheolhyeon.miniwas.lib;

import java.util.HashMap;
import java.util.Map;

public class HTTPServletRequest {

    private String method;
    private String requestUrl;
    private String httpVersion;
    private Map<String, String> headerMap;
    private Map<String, String> parameterMap;

    public void setFirstLine(String method, String requestUrl, String httpVersion) {
        this.method = method;
        this.requestUrl = requestUrl;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public String getHeader(String key) { return headerMap.get(key); }

    public void setHeader(String key, String value) {
        if (!hasHeaderMap()) {
            headerMap = new HashMap<>();
        }
        headerMap.put(key, value);
    }

    private boolean hasHeaderMap() { return headerMap != null; }

    public String getParameter(String key) { return parameterMap.get(key); }

    public Map<String, String> getParameterMap() { return parameterMap; }

    public void setParameter(String key, String value) {
        if (!hasParameterMap()) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put(key, value);
    }

    private boolean hasParameterMap() { return parameterMap != null; }
}
