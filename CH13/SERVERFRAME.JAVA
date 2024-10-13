// 程式：多人連線伺服器
// 範例檔：ServerFrame.java

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;                             //為了建立Vector實體
import java.awt.event.*;

public class ServerFrame extends Frame implements ActionListener
{
   int      ScreenWidth,ScreenHeight; 
   Button   CloseServer;
   TextArea ServerStatus;
   
   ServerThread sThread;

   public ServerFrame()                         //建構函式
   {
      super("遊戲伺服器");                      //建立伺服器視窗
      
      ServerStatus = new TextArea();            //建立TextArea
      CloseServer  = new Button("關閉伺服器");  //建立Button
      ServerStatus.setEditable(false);          //TextArea為不可編輯
      CloseServer.addActionListener(this);      //註冊按鈕的事件處理函式

      add(ServerStatus,BorderLayout.CENTER);    //加入TextArea
      add(CloseServer,BorderLayout.SOUTH);      //加入Button

      //將視窗至於螢幕中央,寬度與高度皆為螢幕之一半
      ScreenWidth  = getToolkit().getScreenSize().width;
      ScreenHeight = getToolkit().getScreenSize().height;
      setSize(ScreenWidth / 2,ScreenHeight / 2);
      setLocation((ScreenWidth - getWidth()) / 2,
                  (ScreenHeight - getHeight()) / 2);
      
      setResizable(false);                      //不可改變視窗大小
      setVisible(true);                         //顯示視窗

      sThread      = new ServerThread(this);    //建立伺服器主執行緒
      sThread.start();                          //啟動執行緒
   }

   public static void main(String srgs[])
   {
      new ServerFrame();                        //建立遊戲伺服器視窗
   }

   //=====實作ActionListener介面=======================================
   public void actionPerformed(ActionEvent e)   //當按鈕按下時
   {
      dispose();
      System.exit(0);                           //關閉視窗
   }
}

class ServerThread extends Thread               //伺服器主執行緒
{
   int          sPort;                          //伺服器接口
   ServerSocket sSocket;                        //伺服器插座
   ServerFrame  frame;

   TextArea     sStatus;

   Vector       Clients;                        //儲存連線客戶端
   Vector       messages;                       //儲存客戶端訊息

   BroadCast        BroadCastWriter;            //廣播訊息執行緒
   CleanDeadConnect Cleaner;                    //清除中斷連線執行緒

   public ServerThread(ServerFrame f)
   {
      Clients  = new Vector();                  //建立Vector實體 
      messages = new Vector();

      try
      {
         sPort   = 6633;                        //指定接口
         sSocket = new ServerSocket(sPort);     //建立伺服器插座  
      }
      catch(IOException E)                      //建立失敗的話...
      {
         System.out.println("發生例外: " + E);
         System.out.println("無法建立伺服器插座!!");
         System.exit(1);                        //結束程式
      }

      this.frame = f;
      sStatus    = frame.ServerStatus;
 
      sStatus.append("遊戲伺服器已啟動..\n\n");
      sStatus.append(">>>>>等待連線<<<<<\n\n");

      BroadCastWriter = new BroadCast(this);    //建立廣播執行緒
      BroadCastWriter.start();                  //啟動

      Cleaner = new CleanDeadConnect(this);     //建立清除中斷連線執行緒
      Cleaner.start();                          //啟動 
   }

   public void run()
   {
      while(true)                               //等待建立連線的迴圈
      {
         try
         {
            Socket cSocket       = sSocket.accept();   //等待連線

            //若連線成功則建立可用來和客戶端溝通的執行緒
            ClientThread cThread = new ClientThread(cSocket,this);
            cThread.start();                           //啟動執行緒

            //在伺服器視窗中加入連線訊息
            sStatus.append(cThread.getClientName() + ":");
            sStatus.append(cThread.getClientAddress() + "已連線 ");
            sStatus.append("目前連線數: " + ClientThread.ConnectNumber +
                           "\n");

            synchronized(Clients)                      //同步處理
            {
               Clients.addElement(cThread);            //加入Vector
            }
         }
         catch(IOException E)                          //發生例外的話
         {
            System.out.println("發生例外: " + E);
            System.out.println("建立客戶端連線失敗!");
            System.exit(2);                            //離開程式
         }
      }
   }

   public void finalize()
   {
      try
      {
         sSocket.close();            //關閉伺服器程式連線
      }
      catch(IOException E){}
     
      sSocket = null;                //釋放資源
   }
}

class ClientThread extends Thread    //用來和客戶端溝通的執行緒
{
   Socket          cSocket;          //和客戶端連線的插座
   PrintStream     write;            //輸出訊息
   BufferedReader  read;             //輸入訊息
   ServerThread    sThread;

   TextArea        sStatus;
   String          m;

   public static int ConnectNumber = 0;    //計算連線數
   
   public ClientThread(Socket cSocket,ServerThread sThread)
   {
      this.cSocket = cSocket;
      this.sThread = sThread; 

      sStatus      = sThread.frame.ServerStatus;  
 
      ConnectNumber++;               //建立新連線,連線數加1
      
      try
      {
         //建立I/O管道
         write = new PrintStream(cSocket.getOutputStream());
         read  = new BufferedReader(
                     new InputStreamReader(cSocket.getInputStream()));
      }
      catch(IOException E)           //發生例外的話
      {
         System.out.println("發生例外: " + E);
         System.out.println("建立I/O管道失敗!");
         System.exit(3);             //離開程式
      }
 
      m = "新夥伴<" + getClientName() + ">加入囉!!! ";
      m = m + "聊天室人數: " + ConnectNumber + " \n\n";
   
      sThread.messages.addElement(m);//廣播新成員加入訊息
   }
  
   public void run()
   {
      while(true)
      {
         try
         {
            String Message = read.readLine(); //讀取客戶端訊息

            synchronized(sThread.messages)    //同步處理
            {
               if(Message != null)            //讀到資料
               {
                  if(Message.equals("bye"))   //如果是bye字串則結束連線
                  {
                     ConnectNumber--;         //連線數減1

                     //廣播成員離開訊息      
                     Message = getClientName() + ":" + getClientAddress() + 
                               "已離開 ";

                     sStatus.append(Message + " ");
                     sStatus.append("目前連線數: " + ConnectNumber + "\n");

                     sThread.messages.addElement(Message);

                     synchronized(sThread.Clients)           //同步處理
                     {
                        sThread.Clients.removeElement(this); //移除連線
                     }

                     break;                   //結束執行緒
                  }
                  else
                  {
                     //廣播客戶端訊息
                     Message = "來自" + getClientName() + ":" + 
                               getClientAddress() + "\n=> " + Message + "\n";

                     sThread.messages.addElement(Message);
                  }
               }
            }
         }
         catch(IOException E)                 //當有連線中斷時
         { 
            break;                            //結束執行緒
         }
      }
   }

   public String getClientName()              //取得客戶端主機名稱
   {
      return cSocket.getInetAddress().getHostName();
   }
  
   public String getClientAddress()           //取得客戶端主機位置
   {
      return cSocket.getInetAddress().getHostAddress(); 
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
}

class BroadCast extends Thread       //這是廣播執行緒
{
   ClientThread c;
   ServerThread sThread;
   String       m;

 
   public BroadCast(ServerThread sThread)
   {
      this.sThread    = sThread;
   }

   public void run()
   {
      while(true)
      { 
         try
         {
            Thread.sleep(3000);      //每3秒廣播一次
         }
         catch(InterruptedException E){}

         synchronized(sThread.messages)                   //同步處理
         {
            if(sThread.messages.isEmpty())                //如果無訊息
               continue;                                  //則繼續等待
           
            m = (String)sThread.messages.firstElement();  //取出第一個訊息
            sThread.messages.removeElement(m);            //移除第一個訊息
         }

         synchronized(sThread.Clients)                    //同步處理
         {
            //對每一個客戶端發送訊息
            for(int i=0;i<sThread.Clients.size();i++)
            {
               c = (ClientThread)sThread.Clients.elementAt(i);
               c.write.println(m);
            }
         }
      }
   }
}

class CleanDeadConnect extends Thread  //清除中斷連線的執行緒
{
   ServerThread sThread;
   ClientThread temp;
   String       m;

   public CleanDeadConnect(ServerThread sThread)
   {
      this.sThread = sThread;
   }

   public void run()
   {  
      while(true)
      {  
         try
         {
            Thread.sleep(10000);       //每10秒清除一次
         }
         catch(InterruptedException E){}

         synchronized(sThread.Clients) //同步處理
         {
            for(int i=0;i<sThread.Clients.size();i++)
            {
               temp = (ClientThread)sThread.Clients.elementAt(i);

               if(!temp.isAlive())                    //中斷的連線
               {
                  ClientThread.ConnectNumber--;       //連線數減1

                  m = temp.getClientName() + ":" + temp.getClientAddress();
                  m = m + "已離開 目前連線數: " + 
                      ClientThread.ConnectNumber + "\n";

                  sThread.frame.ServerStatus.append(m);
                  
                  synchronized(sThread.messages)      //同步處理
                  {
                     sThread.messages.addElement(m);  //廣播訊息
                  }

                  sThread.Clients.removeElementAt(i); //移除連線
               }   
            }
         }
      }
   }
}
