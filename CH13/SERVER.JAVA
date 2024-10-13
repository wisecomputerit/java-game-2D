// 程式：簡單的連線伺服端
// 範例檔：Server.java

import java.io.*;
import java.net.*;

public class Server 
{
   public static void main(String args[])
   {
      try
      {
         ServerSocket S = new ServerSocket(6633);
         System.out.println("等待連線中...");

         Socket       C = S.accept();            //等待客戶端連線

         //取得輸出串流並輸出一字串給客戶端
         PrintStream write = new PrintStream(C.getOutputStream());
         write.println(C.getInetAddress().getHostName() + "歡迎光臨");

         C.close();   //關閉連線
         S.close();
      }
      catch(IOException E){}
   }  
}