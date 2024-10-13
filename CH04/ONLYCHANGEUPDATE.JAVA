// 程式：重新定義update()的可能問題
// 範例檔：OnlyChangeUpdate.java

import java.awt.*;
import java.applet.*;

public class OnlyChangeUpdate extends Applet implements Runnable
{
   int    X,Y,moveX,moveY,width,height;  
   Thread newThread;                      //新執行緒

   public void init()
   {
      X      = 0;                         //X座標
      Y      = 0;                         //Y座標
      moveX  = 40;                        //X軸移動距離
      moveY  = 40;                        //Y軸移動距離
      width  = getSize().width;           //Applet的寬度
      height = getSize().height;          //Applet的高度

      setBackground(Color.black);         //設定背景為黑色
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
      g.setColor(Color.white);            //設定前景顏色為白色
      g.fillOval(X,Y,30,30);              //繪製實心正圓形
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
            Thread.sleep(80);             //暫停程式執行80毫秒
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