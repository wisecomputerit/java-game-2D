// 程式：paint()方法的呼叫
// 範例檔：CallPaint.java

import java.awt.*;      //一定要指定使用此類別集合
import java.applet.*;

public class CallPaint extends Applet {
   public void init() {
      System.out.println("init()方法...");
   }

   public void start() {
      System.out.println("start()方法...");
   }

   public void paint(Graphics g) {   //paint()方法在此
      //在座標(10,50) 繪製字串
      g.drawString("歡迎來到Java網路遊戲的世界",10,50);
      // 在主控台顯示方法執行時機
      System.out.println("paint()方法...");
   }
 
   public void stop() {
      System.out.println("stop()方法...");
   }
 
   public void destroy() {
      System.out.println("destory()方法...");
   }
}