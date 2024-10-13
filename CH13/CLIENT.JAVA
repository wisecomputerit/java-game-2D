// 程式：使用Applet的客戶端連線
// 範例檔：Client.java

import java.io.*;
import java.awt.*;
import java.net.*;
import java.applet.*;

public class Client extends Applet implements Runnable
{
   int      port; 
   String   name,message;
   Socket   S;
   Thread   T;
   TextArea TA;

   BufferedReader read; 

   public void init()
   {
      try
      {
         port    = 6633;                         //接口
         name    = getDocumentBase().getHost();  //主機位置
         S       = new Socket(name,port);        //建立插座
         message = null;

         setLayout(new BorderLayout());          //建立並加入TextArea
         TA = new TextArea();
         add(TA,BorderLayout.CENTER);
 
         read    = new BufferedReader(           //讀取資料
                       new InputStreamReader(S.getInputStream()));
      }
      catch(UnknownHostException E){}
      catch(IOException E){}
    
      T = new Thread(this);                      //建立執行緒
      T.start();                                 //啟動執行緒
   }

   public void run()
   {
      while(true)
      {
         try
         {
            message = read.readLine();            //讀取資料

            if(message == null)                   //當連線中斷時
            {
               T = null;
               S.close();
               break;
            }
            else
            {
               TA.append(message);                //加入讀取到的字串到
            }                                     //TextArea中
         }
         catch(IOException E){}
      }
   }
}