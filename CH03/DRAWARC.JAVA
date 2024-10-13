// 程式：繪製弧形
// 範例檔：DrawArc.java

import java.awt.*;
import java.applet.*;

public class DrawArc extends Applet {
   public void paint(Graphics g)
   {
      g.drawArc(50, 50, 100, 100, 30, 300);  //繪製弧形
      g.setColor(Color.green);
      g.fillArc(200, 150, 100, 100, 30, 300);  //繪製填滿弧形
   }
}