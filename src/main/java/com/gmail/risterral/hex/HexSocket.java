package com.gmail.risterral.hex;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HexSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final DataOutputStream out;

    public HexSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public String getContent(boolean endConnection) throws IOException {
        String content;
        while ((content = in.readLine()) != null) {
            if (content.length() == 0) {
                content = in.readLine();
                break;
            }
        }

        if (endConnection) {
            out.close();
            in.close();
            socket.close();
        }

        return content;
    }
}