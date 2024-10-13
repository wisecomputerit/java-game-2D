// 程式：多人聊天室客戶端程式
// 範例檔：ClientApplet.java

import java.io.*;
import java.awt.*;
import java.net.*;
import java.applet.*;
import java.awt.event.*;

public class ClientApplet extends Applet 
       implements Runnable,ActionListener
{
   int             sPort;                //伺服器程式接口
   String          sName;                //伺服器位置
   Socket          cSocket;              //連往伺服器的插座
   boolean         dead; 
    
   PrintStream     write;                //I/O管道
   BufferedReader  read;

   Thread          T;                    //用來接收訊息的執行緒
   
   Font            F;
   Panel           south;
   Button          sendMessage;
   TextArea        Message;
   TextField       send;

   public void init()
   {
      setLayout(new BorderLayout());     //使用BorderLayout

      dead        = false;
 
      //建立介面相關AWT元件
      F           = new Font("Dialog",Font.BOLD,16);
      south       = new Panel();
      sendMessage = new Button("傳送訊息");
      Message     = new TextArea();
      send        = new TextField(40);

      south.add(send); 
      south.add(sendMessage);
      
      Message.setFont(F);                          //設定字型
      Message.setEditable(false);                  //不可編輯
      Message.append("歡迎使用良葛格的聊天室\n\n");
      Message.setBackground(Color.black);          //設定背景顏色
      Message.setForeground(Color.white);          //設定前景顏色

      add(south,BorderLayout.SOUTH);
      add(Message,BorderLayout.CENTER);
   
      sendMessage.addActionListener(this);         //註冊按鈕的事件處理函式

      try
      {
         sPort   = 6633;                           //指定伺服器接口
         sName   = getDocumentBase().getHost();    //指定伺服器位置
         cSocket = new Socket(sName,sPort);        //建立插座
     
         //建立I/O管道
         write   = new PrintStream(cSocket.getOutputStream());
         read    = new BufferedReader(
                       new InputStreamReader(cSocket.getInputStream())); 
      }
      catch(UnknownHostException E)                //建立插座失敗
      {
         Message.append("發生例外: 無法識別的主機!");
         sendMessage.setEnabled(false);
      }
      catch(IOException E)                         //建立I/O管道失敗
      {
         Message.append("發生例外: 建立I/O管道失敗!");
         sendMessage.setEnabled(false);
      }

      T = new Thread(this);                        //建立接收訊息執行緒
      T.start();                                   //啟動執行緒
   }

   public void run()
   {
      String m;

      while(!dead)
      {
         try
         {
            m = read.readLine();             //接收訊息
 
            if(m != null)
            {
               Message.append("\n" + m);     //將訊息加入TextArea中
            }
         }
         catch(IOException E)                //發生例外
         {
            Message.append("\n\n遠端伺服器已關閉或網路連線發生錯誤...");
            Message.append("\n\n請嘗試重新連線...");
            sendMessage.setEnabled(false);

            break;
         }
      }
   }

   public void finalize()
   {
      try
      {
         read.close();
         write.close();
         cSocket.close();            //關閉伺服器程式連線
      }
      catch(IOException E){}
     
      cSocket = null;                //釋放資源
   }

   //=====實作ActionListener===========================================
   public void actionPerformed(ActionEvent e)
   {
      if(send.getText().equals(""))  //輸入為空白
      {
         return;
      }
      else if(send.getText().equals("bye"))
      {
         dead = true;

         Message.append("\n已結束連線.....");
      }

      write.println(send.getText()); //發送訊息
      send.setText("");              //清除文字區域 
   }
}