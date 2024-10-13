// 程式：繪製多邊形
// 範例檔：DrawPolygon.java

import java.awt.*;
import java.applet.*;

public class DrawPolygon extends Applet {
   int X[] = {10,180,90,110,130,50};   //頂點X座標
   int Y[] = {15,55,95,115,135,180};   //頂點Y座標

   public void paint(Graphics g)
   {
      g.drawPolygon(X,Y,6);            //繪製六邊形
   }
}