// 程式：處理客戶端連線多緒執行緒
// 範例檔：ClientThread.java

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;                             //為了建立Vector實體
import java.awt.event.*;

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
