package UploadAndDownload;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyClient {
    Socket socket = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;

    public MyClient(String host, int port) {

        try {
            socket = new Socket(host, port);//host为服务器IP,port为服务器端口
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(String req) throws IOException {
        System.out.println("this is req:");
        System.out.println(req);
        outputStream.write(req.getBytes());//向服务器端发送数据
    }

    public String  receive() throws IOException {
        byte[] bytes = new byte[1024*4];
//        int recv = 0;
        int bytesRead;
        String re = "";
        int i=0;
        do {//接收服务器发送的数据
            i++;
            bytesRead = inputStream.read(bytes);
//            recv+=bytesRead;
            re += new String(bytes,0,bytesRead);
        } while (bytesRead >= 1024*4);
//        System.out.println("length: "+recv+" num:"+i);
        return re;
    }

    public void close() throws IOException {
        socket.close();
    }
}