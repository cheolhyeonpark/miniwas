package com.cheolhyeon.miniwas.server;

import com.cheolhyeon.miniwas.lib.HTTPServletRequest;
import com.cheolhyeon.miniwas.lib.HTTPServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Container {

    private static final int PORT_NUMBER = 3636;
    private static final String FILE_PATH = "../../app";

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            Socket socket = serverSocket.accept();
            HTTPServletRequest request = new HTTPServletRequest();
            RequestReader requestReader = new RequestReader(request);
            requestReader.run(socket.getInputStream());

            String url = request.getRequestUrl();
            File file = new File(FILE_PATH + url);
            if (file.isFile()) {
                HTTPServletResponse response = new HTTPServletResponse(file, socket.getOutputStream());
                response.setFileContentType();
                response.sendFile();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
