package com.cheolhyeon.miniwas.lib;

public class Servlet {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    public void init() {

    }

    public void service(HTTPServletRequest request, HTTPServletResponse response) {
        String method = request.getMethod();
        switch (method) {
            case GET:
                doGet(request, response);
                break;
            case POST:
                doPost(request, response);
                break;
            case PUT:
                doPut(request, response);
                break;
            case DELETE:
                doDelete(request, response);
            default:
                break;
        }
    }

    public void doGet(HTTPServletRequest request, HTTPServletResponse response) {

    }

    public void doPost(HTTPServletRequest request, HTTPServletResponse response) {

    }

    public void doPut(HTTPServletRequest request, HTTPServletResponse response) {

    }

    public void doDelete(HTTPServletRequest request, HTTPServletResponse response) {

    }

    public void destroy() {

    }
}
