package edu.school21.sockets;
import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ClientMain {
    public static BufferedReader reader;
    public static BufferedWriter out;
    public static BufferedReader in;
    public static String nickName;
    public static Socket socket;
    public static boolean flagStop = false;

    public static void main(String[] args) throws IOException {
            socket = new Socket("localhost", 6666);
            if (socket.isConnected()) {
                reader = new BufferedReader(new InputStreamReader(System.in));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for (int i = 0; i < 4; i++) {
                    System.out.println(in.readLine());
                }
                String flagStr = "\n";
                while (true) {
                    String string = reader.readLine();
                    if (string.equals("exit")) {
                        socket.close();
                        reader.close();
                        out.close();
                        in.close();
                        flagStop = true;
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
                for (int i = 0; i < 3; i++) {
                    System.out.println(in.readLine());
                }

                if (!flagStop) {
                    new InputMessage().start();
                    new OutputMessage().start();
                }
            }
            else {
                socket.close();
                throw new RuntimeException("Error with socket");
            }
    }

    public static class InputMessage extends Thread {
        @Override
        public void run() {
            while(!flagStop) {
                try {
                    String string = in.readLine();
                    if (string != null) {
                        if (string.equals("exit")) {
                            socket.close();
                            in.close();
                            reader.close();
                            out.close();
                            flagStop = true;
                            break;
                        }
                        System.out.println(string + "\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class OutputMessage extends Thread {
        @Override
        public void run() {
            while(!flagStop) {
                try {
                    String string = reader.readLine();
                    if (string != null) {
                        if (string.equals("exit")) {
                            socket.close();
                            in.close();
                            reader.close();
                            out.close();
                            flagStop = true;
                            break;
                        }
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        out.write(dateFormat.format(timestamp) + " " + nickName + ": " + string + "\n");
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
