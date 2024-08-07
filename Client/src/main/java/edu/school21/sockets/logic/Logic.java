package edu.school21.sockets.logic;

import edu.school21.sockets.service.InputMessage;
import edu.school21.sockets.service.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;


@Component
public class Logic {

    @Autowired
    private BufferedReader reader;

    @Autowired
    private BufferedWriter out;

    @Autowired
    private BufferedReader in;

    private String nickName;

    private boolean flagStop = false;

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isFlagStop() {
        return flagStop;
    }

    public void setFlagStop(boolean flagStop) {
        this.flagStop = flagStop;
    }

    public void startProgram(Socket socket) throws IOException {
//        reader = new BufferedReader(new InputStreamReader(System.in));
//        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        for (int i = 0; i < 4; i++) {
            System.out.println(in.readLine());
        }
        String flagStr = "\n";
        while (true) {
            String string = reader.readLine();
            if (string.equals("3")) {
                close(socket);
                break;
            }
            out.write(string + "\n");
            out.flush();
            flagStr = in.readLine();
            if (flagStr.contains("Successful")) {
                nickName = flagStr.substring(10).trim();
                System.out.println("Successful");
                break;
            }
            System.out.println(flagStr);
        }
        if (!flagStop) {
            for (int i = 0; i < 3; i++) {
                System.out.println(in.readLine());
            }
            String inputString = reader.readLine();
            int mode = Integer.parseInt(inputString);
            out.write(inputString + "\n");
            out.flush();
            if (mode == 1) {
                String nameRoom = reader.readLine();
                out.write(nameRoom + "\n");
                out.flush();
            }
            else if (mode == 2) {
                int cnt = 0;
                String nameRoom = "\n";
                while (!nameRoom.contains("Exit")) {
                    nameRoom = in.readLine();
                    System.out.println(nameRoom);
                    cnt++;
                }

                nameRoom = reader.readLine();
                int number = Integer.parseInt(nameRoom);
                if (number == cnt) {
                    close(socket);
                    return;
                }
                out.write(nameRoom + "\n");
                out.flush();
            }
            else {
                close(socket);
                return;
            }
            new InputMessage(socket, this).start();
            new OutputMessage(socket, this).start();
        }
    }

    public void close(Socket socket) throws IOException {
        socket.close();
        reader.close();
        out.close();
        in.close();
        flagStop = true;
    }

}
