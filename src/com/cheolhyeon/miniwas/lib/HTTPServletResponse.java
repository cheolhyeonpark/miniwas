package com.cheolhyeon.miniwas.lib;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPServletResponse {

    private static final String HTTP_VER_1_1 = "HTTP/1.1";
    private static final int STATUS_CODE_OK = 200;
    private static final String STATUS_TEXT_OK = "OK";
    private static final int BUFFER_SIZE = 128;

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

    private boolean hasHeaderMap() { return headerMap != null; }

    public void sendFile() throws IOException {
        writeHeader();
        List<Byte> fileBytes = getFileBytes();
        int writeCount = 0;
        byte[] bufferByte = new byte[fileBytes.size()];
        for (byte fileByte : fileBytes) {
            bufferByte[writeCount] = fileByte;
            writeCount++;
        }
        outputStream.write(bufferByte);
        outputStream.flush();
    }

    private List<Byte> getFileBytes() {
        List<Byte> fileBytes = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int readCount = 0;
            byte[] bufferByte = new byte[BUFFER_SIZE];
            while ((readCount = fileInputStream.read(bufferByte)) != -1) {
                for (int i = 0; i < readCount; i++) {
                    fileBytes.add(bufferByte[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;
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
