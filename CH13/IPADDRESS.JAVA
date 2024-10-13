// 程式：取得本機位址
// 範例檔：IPAddress.java

import java.net.*;     //為了使用InetAddress類別
import java.awt.*;
import java.applet.*;

public class IPAddress extends Applet
{
   InetAddress myComputer;
   String      IP,Name;
  
   public void init()
   {
      try
      {
         myComputer = InetAddress.getLocalHost();  //取得主機位置實體
      }
      catch(UnknownHostException E){}

      IP         = myComputer.getHostAddress();    //主機位置
      Name       = myComputer.getHostName();       //主機名稱
   }

   public void paint(Graphics g)
   {
      g.drawString("主機位置:" + IP,10,20);
      g.drawString("主機名稱:" + Name,10,40);
   }
}