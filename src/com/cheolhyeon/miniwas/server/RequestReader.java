package com.cheolhyeon.miniwas.server;

import com.cheolhyeon.miniwas.lib.HTTPServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RequestReader {

    private static final byte CR = '\r';
    private static final byte LF = '\n';
    private static final String SPACE = " ";
    private static final String COLON = ":";
    private static final String QUESTION_MARK = "\\?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL_SIGN = "=";
    private static final String GET_METHOD = "GET";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";

    private boolean isBody = false;
    private int contentLength = 0;
    private StringBuilder lineBuilder = new StringBuilder();
    private byte previousByte = (byte) -1;
    private List<Byte> bodyByteList;
    private HTTPServletRequest request;

    public RequestReader(HTTPServletRequest request) {
        this.request = request;
    }

    public void run(InputStream inputStream) {
        try {
            readRequest(inputStream);
            setRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readRequest(InputStream inputStream) throws IOException {
        byte[] readBuffer = new byte[128];
        while (!isEndOfRequest()) {
            inputStream.read(readBuffer);
            readData(readBuffer);
        }
    }

    private void readData(byte[] readBuffer) {
        for (byte thisByte : readBuffer) {
            if (isEndOfRequest()) {
                return;
            }
            if (isBody) {
                readBodyByte(thisByte);
                continue;
            }
            if (isEndOfLine(previousByte, thisByte)) {
                readLine();
                lineBuilder.setLength(0);
                continue;
            }
            lineBuilder.append((char) thisByte);
            previousByte = thisByte;
        }
    }

    private void setRequest() {
        if (hasQuery()) {
            setRequestParameter(request.getRequestUrl().split(QUESTION_MARK)[1]);
        }
        if (hasParameter()) {
            try {
                setRequestParameter(getQuery());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private String getQuery() throws UnsupportedEncodingException {
        byte[] bodyBytes = getBytes(bodyByteList);
        String query = new String(bodyBytes, "UTF-8");
        return query;
    }

    private byte[] getBytes(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    private void setRequestParameter(String query) {
        String[] parameters = query.split(AMPERSAND);
        for (String parameter : parameters) {
            String[] keyAndValue = parameter.split(EQUAL_SIGN);
            request.setParameter(keyAndValue[0], keyAndValue[1]);
        }
    }

    private boolean hasParameter() {
        return contentLength > 0 && "application/x-www-form-urlencoded".equals(request.getHeader(CONTENT_TYPE).trim());
    }

    private boolean hasQuery() {
        return GET_METHOD.equals(request.getMethod()) && request.getRequestUrl().contains("?");
    }

    private boolean isEndOfRequest() {
        return isBody && contentLength < 1 || contentLength > 0 && bodyByteList.size() >= contentLength;
    }

    private void readBodyByte(byte thisByte) {
        bodyByteList.add(thisByte);
    }

    private boolean isEndOfLine(byte previousByte, byte thisByte) {
        return thisByte == LF && previousByte == CR;
    }

    private void readLine() {
        String oneLine = lineBuilder.substring(0, lineBuilder.length() - 1);
        if (isFirstLine()) {
            String[] firstLine = oneLine.split(SPACE);
            request.setFirstLine(firstLine[0], firstLine[1], firstLine[2]);
            request.setHeader(null, null);
            return;
        }
        readHeaderLine(oneLine);
    }

    private void readHeaderLine(String oneLine) {
        if (isLastOfHeader(oneLine)) {
            initBodyVar();
            return;
        }
        int indexOfColon = oneLine.indexOf(COLON);
        String headerName = oneLine.substring(0, indexOfColon).trim();
        String headerValue = oneLine.substring(indexOfColon + 1).trim();
        request.setHeader(headerName, headerValue);
    }

    private void initBodyVar() {
        isBody = true;
        if (request.getHeader(CONTENT_LENGTH) != null) {
            bodyByteList = new ArrayList<>();
            contentLength = Integer.parseInt(request.getHeader(CONTENT_LENGTH));
        }
    }

    private boolean isLastOfHeader(String oneLine) {
        return oneLine.length() < 1;
    }

    private boolean isFirstLine() {
        return request.getHeaderMap() == null;
    }
}
