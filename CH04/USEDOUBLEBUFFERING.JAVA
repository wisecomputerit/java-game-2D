// 程式：使用雙緩衝區
// 範例檔：UseDoubleBuffering.java

import java.awt.*;
import java.applet.*;

public class UseDoubleBuffering extends Applet implements Runnable
{
   int    X,Y,moveX,moveY,width,height;  
   Thread newThread;                      //新執行緒

   Image    OffScreen;                    //次畫面
   Graphics drawOffScreen;                //繪製次畫面的Graphics實體

   public void init()
   {
      X      = 0;                         //X座標
      Y      = 0;                         //Y座標
      moveX  = 2;                         //X軸移動距離
      moveY  = 3;                         //Y軸移動距離
      width  = getSize().width;           //Applet的寬度
      height = getSize().height;          //Applet的高度

      OffScreen     = createImage(width,height); //建立次畫面
      drawOffScreen = OffScreen.getGraphics();   //取得次畫面的繪製類別
   }

   public void start()                    //start()函式  
   {
      newThread = new Thread(this);       //建立與啟動新執行緒
      newThread.start();
   }

   public void stop()                     //stop()函式
   {
      newThread = null;                   //將執行緒設為null
   }

   public void paint(Graphics g)
   {
      //底下這兩行的作用為清除次畫面
      drawOffScreen.setColor(Color.black);
      drawOffScreen.fillRect(0,0,width,height);

      //底下這兩行的作用為在次畫面上繪製實心正圓形
      drawOffScreen.setColor(Color.white);
      drawOffScreen.fillOval(X,Y,30,30);  

      //將次畫面貼到主畫面上
      g.drawImage(OffScreen,0,0,this); 
   }

   public void update(Graphics g)         //update()函式
   {
      paint(g);                           //只單純呼叫paint()函式
   }

   public void run()
   {
      while(newThread != null)            //動畫迴圈
      {
         repaint();                       //重新繪製影像

         try
         {
            Thread.sleep(50);             //暫停程式執行50毫秒
         }
         catch(InterruptedException E){ }
       
         X = X + moveX;                   //計算新的X座標
         Y = Y + moveY;                   //計算新的Y座標
    
         //碰撞到邊界時就會反彈
         if(X >= (width - 30))
         {
            X     = width - 30;
            moveX = -moveX;
         }
         if(X <= 0)
         {
            X = 0;
            moveX = -moveX;
         } 
         if(Y >= (height - 30))
         {
            Y     = height - 30;
            moveY = -moveY;
         }
         if(Y <= 0)
         {
            Y = 0;
            moveY = -moveY;
         } 
      }
   }
}