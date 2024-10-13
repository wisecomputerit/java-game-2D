// 程式：伺服端執行緒類別
// 範例檔：serverskt.java

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

class serverskt extends Thread 
{
    ServerSocket skt;   // 伺服端ServerSocket
    Socket Client;      // 接收的客戶端Socket
    TextArea in;

    BufferedReader theInputStream;  // 讀取客戶端資料的緩衝區
    PrintStream theOutputStream;    // 將資料丟出給客戶端的物件
    String readin;                  // 從客戶端讀到的資料

    ChatOneToOne chat;      // 前端Applet介面

    public serverskt(int port, ChatOneToOne chat)
    {
        try {
            // 根據指定的連接埠建立Socket物件
            skt = new ServerSocket(port);
            this.chat = chat;
        }
        catch (IOException e)
        {
            chat.ta.append(e.toString());
        }
    }

    public void run()
    {
        try {
            chat.ta.append("等待連線......");
            // 開始接受連線，客戶端連線之後會得到Socket物件
            Client = skt.accept();
            chat.ta.append("客戶端" + Client.getInetAddress() + "已連線\n");

            // 建立讀取緩衝區
            theInputStream = new BufferedReader(
                new InputStreamReader(Client.getInputStream()));
            // 建立資料丟出物件
            theOutputStream = new PrintStream(Client.getOutputStream());

            // 利用迴圈讀取資料
            while(true)
            {
                readin = theInputStream.readLine();
                chat.ta.append(readin + "\n");
            }
        }
        catch (SocketException e) // 客戶端連線中斷捕捉此物件
        {
            chat.ta.append("連線中斷！\n");
            // 將所有的按鈕設定為可用
            chat.clientBtn.setEnabled(true);
            chat.serverBtn.setEnabled(true);
            chat.tfaddress.setEnabled(true);
            chat.tfport.setEnabled(true);
            try {
            // 關閉ServerSocket與Socket
                skt.close();
                Client.close();
            }
            catch (IOException err)
            {
                chat.ta.append(err.toString());
            }
        }
        catch (IOException e)
        {
            chat.ta.append(e.toString());
        }
    }

    // 伺服端丟出資料的方法
    public void dataout(String data)
    {
        theOutputStream.println(data);
    }
}