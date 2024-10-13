// 程式：簡單的伺服器
// 範例檔：RedirectServer.java

import java.net.*;
import java.io.*;

public class RedirectServer {
    public static void main(String args[])
    {
        try {
            ServerSocket sktServer;
            Socket sktClient;
            PrintStream send;
            int count = 0;  // 重新導向次數
    
            // 指定連接埠建立物件
            sktServer = new ServerSocket(80); 
            while (true)
            {
                try {
                    System.out.println("伺服器等待連線");
                    sktClient = sktServer.accept(); // 等待連線
                    count++;
                    System.out.println("客戶端連線：" + count);
                    send = new PrintStream(sktClient.getOutputStream());

                    // 以下為HTTP指令，可將瀏覽器重新導向至指定的網頁
                    send.print("HTTP/1.0 302 FOUND\r\n");
                    send.print("Location: http://www.acertwp.com.tw\r\n");

                    sktClient.close();  // 關閉與客戶端連線
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
             }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}