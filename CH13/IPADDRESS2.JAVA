import java.net.*;     //為了使用InetAddress類別
import java.awt.*;

public class IPAddress2 
{
   public static void main(String args[])
   {
      InetAddress myComputer;
      String      IP,Name;

      try
      {
         myComputer = InetAddress.getLocalHost();  //取得主機位置實體
       
         IP         = myComputer.getHostAddress();    //主機位置
         Name       = myComputer.getHostName();       //主機名稱

         System.out.println("主機位置:" + IP);
         System.out.println("主機名稱:" + Name);
      }
      catch(UnknownHostException E){}
   }
}