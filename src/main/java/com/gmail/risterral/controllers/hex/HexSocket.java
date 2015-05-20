package com.gmail.risterral.controllers.hex;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HexSocket {
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String HOST = "Host:";

    private final Socket socket;
    private final BufferedReader in;
    private final DataOutputStream out;

    public HexSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public String getContent(boolean endConnection) throws IOException {
        Integer contentLength = 0;

        while (true) {

            String line = in.readLine();

            if (line.contains(CONTENT_LENGTH)) {
                contentLength = Integer.parseInt(line.substring(CONTENT_LENGTH.length(), line.length()));
            }

            if (line.contains(HOST)) {
                break;
            }
        }
        in.read(); //10
        in.read(); //13

        byte[] bytes = new byte[contentLength];
        for (int i = 0; i < contentLength; i++) {
            bytes[i] = (byte) in.read();
        }
        String content = new String(bytes);


        if (endConnection) {
            socket.close();
        }

        return content;
    }
}