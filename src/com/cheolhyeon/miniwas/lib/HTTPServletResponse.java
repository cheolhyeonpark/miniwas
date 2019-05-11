package com.cheolhyeon.miniwas.lib;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HTTPServletResponse {

    private static final String HTTP_VER_1_1 = "HTTP/1.1";
    private static final int STATUS_CODE_OK = 200;
    private static final String STATUS_TEXT_OK = "OK";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_PNG = "image/png";
    private static final String CONTENT_TYPE_JPG = "image/jpeg";
    private static final String CONTENT_TYPE_HTML = "text/html";

    private String httpVersion;
    private int statusCode;
    private String statusText;
    private Map<String, String> headerMap;
    private File file;
    private OutputStream outputStream;
    private PrintWriter writer;

    public HTTPServletResponse(OutputStream outputStream) {
        init(outputStream);
    }

    public HTTPServletResponse(File file, OutputStream outputStream) {
        init(outputStream);
        this.file = file;
    }

    private void init(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.writer = new PrintWriter(new OutputStreamWriter(outputStream));
        setStatusLine(HTTP_VER_1_1, STATUS_CODE_OK, STATUS_TEXT_OK);
    }

    public void setStatusLine(String httpVersion, int statusCode, String statusText) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public void setHeader(String key, String value) {
        if (!hasHeaderMap()) {
            headerMap = new HashMap<>();
        }
        headerMap.put(key, value);
    }

    public void setFileContentType() {
        if (file.getName().endsWith(".png")) {
            setHeader(CONTENT_TYPE, CONTENT_TYPE_PNG);
            return;
        }
        if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
            setHeader(CONTENT_TYPE, CONTENT_TYPE_JPG);
            return;
        }
        if (file.getName().endsWith(".html")) {
            setHeader(CONTENT_TYPE, CONTENT_TYPE_HTML);
            return;
        }
    }

    private boolean hasHeaderMap() { return headerMap != null; }

    public void sendFile() {
        writeHeader();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);) {
            int readByte = -1;
            while ((readByte = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(readByte);
            }
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getWriter() {
        writeHeader();
        return writer;
    }

    private void writeHeader() {
        writer.println(httpVersion + " " + statusCode + " " + statusText);
        for (String key : headerMap.keySet()) {
            writer.println(key + ": " + headerMap.get(key));
        }
        writer.println();
        writer.flush();
    }
}
