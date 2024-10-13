// 程式：客戶端執行緒類別
// 範例檔：clientskt.java

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

class clientskt extends Thread 
{
    Socket skt;        // 客戶端連線Socket物件
    InetAddress host;  // 指定的伺服端位址
    int port;          // 指定的伺服端連接埠
 
    BufferedReader theInputStream;
    PrintStream theOutputStream; 
    String readin;     // 伺服端傳回的資料

    ChatOneToOne chat;  // 前端的Applet介面

    public clientskt(String ip, int p, ChatOneToOne chat)
    {
        try {
            // 取得伺服端的InetAddress物件、通訊連接埠
            host = InetAddress.getByName(ip); 
            port = p;
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
            chat.ta.append("嘗試連線......");
            // 建立Socket物件並嘗試連線
            skt = new Socket(host, port);
            chat.ta.append("連線成功\n");
            // 從InputStream建立讀取緩衝區
            theInputStream = new BufferedReader(
                new InputStreamReader(skt.getInputStream()));
            // 從OutputStream中建立PrintStream物件
            theOutputStream = new PrintStream(skt.getOutputStream());

            // 讀取資料迴圈
            while(true)
            {
                // 讀取伺服端傳來的資料
                readin = theInputStream.readLine();
                // 將之附加在Applet的TextArea上
                chat.ta.append(readin + "\n");
            }
        }
        catch (SocketException e) // 連線中斷時丟出此例外
        {
            chat.ta.append("連線中斷！\n");
            // 將所有的按鈕設定為可用
            chat.clientBtn.setEnabled(true);
            chat.serverBtn.setEnabled(true);
            chat.tfaddress.setEnabled(true);
            chat.tfport.setEnabled(true);
            try {
                // 關閉之前的Socket連線，以便可再度連線
                skt.close();
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

    // 客戶端丟出資料的方法
    public void dataout(String data)
    {
        theOutputStream.println(data);
    }
}