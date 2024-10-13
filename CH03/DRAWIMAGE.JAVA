// 程式：繪製影像
// 範例檔：DrawImage.java

import java.awt.*;
import java.applet.*;

public class DrawImage extends Applet {
   Image Background, Plane;

   public void init()
   {
      Background = getImage(getDocumentBase(),"Images/1.gif");
      Plane = getImage(getDocumentBase(),"Images/2.gif");
   }

   public void paint(Graphics g)
   {
      g.drawImage(Background,0,0,320,240,this);  //先繪製背景
      g.drawImage(Plane,110,90,100,60,this);     //再繪製飛機
   }
}