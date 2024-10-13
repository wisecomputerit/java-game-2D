// 程式：Applet程式的生命週期
// 範例檔：LifeCycle.java

import java.applet.*;

public class LifeCycle extends Applet{
   public void init() {
      System.out.println("init()方法...");
   }

   public void start() {
      System.out.println("start()方法...");
   }
 
   public void stop() {
      System.out.println("stop()方法...");
   }
 
   public void destroy() {
      System.out.println("destory()方法...");
   }
}