// 程式：使用Applet
// 範例檔：AppletClassicExample.java

import java.awt.*;                               //指定欲使用的Package
import java.applet.*;  

public class AppletClassicExample extends Applet //指定繼承Applet類別
{
   public void paint(Graphics g)                 //用來繪製字串的函式
   {
      g.drawString("歡迎來到Java網路遊戲的世界...",10,50);
   } 
}