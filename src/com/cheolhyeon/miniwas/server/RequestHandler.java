package com.cheolhyeon.miniwas.server;

import com.cheolhyeon.miniwas.lib.HTTPServletRequest;
import com.cheolhyeon.miniwas.lib.HTTPServletResponse;
import com.cheolhyeon.miniwas.lib.Servlet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private static final String APP_ROOT = "../../app";

    private Socket socket;
    private ServletMap servletMap;

    public RequestHandler(Socket socket, ServletMap servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
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
}
