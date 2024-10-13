// 程式：煙火樹粒子
// 範例檔：FireTree.java

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class FireTree extends Applet implements Runnable
{
   final int Max = 1000;
   ftparticle p[]; // 煙火樹粒子
   int AppletWidth,AppletHeight,XCenter,YCenter;
   Image        OffScreen;
   Graphics     drawOffScreen;
   Thread pThread;

   public void init()
   {
      setBackground(Color.black); // 設定背景為黑色

      p = new ftparticle[Max]; // 建立粒子

      // 取得顯像區域
      AppletWidth = getSize().width;
      AppletHeight = getSize().height;

      // 煙火初始位置
      XCenter = AppletWidth/2;
      YCenter = 2*AppletHeight/3;

      // 產生並初始化粒子
      for(int i=0; i<Max; i++)
          p[i] = new ftparticle(XCenter,YCenter);

      // 建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();
   }

   public void start()
   {
      pThread = new Thread(this);
      pThread.start();
   }

   public void stop()
   {
      pThread = null;
   }
 
   public void update(Graphics g)
   {
       paint(g);
   }

   public void paint(Graphics g)
   {
      g.drawImage(OffScreen,0,0,this);
   }

   public void run()
   {
      int i;
      while(true)
      {
         // 重清畫面
         drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
         drawOffScreen.setColor(Color.white);
         drawOffScreen.fillRect(XCenter,YCenter,5,AppletHeight/3);
  
         for(i=0; i<Max; i++)
         {   
            // 如果超出顯像區域，重設粒子
            if(p[i].Y > AppletHeight)
               p[i].reset(XCenter,YCenter);

            // 繪製粒子
            drawOffScreen.setColor(p[i].color);
            drawOffScreen.fillOval((int)p[i].X,(int)p[i].Y,3,3);
            // 粒子水平移動
            p[i].X += p[i].Vx;
            // 粒子受重力影響
            p[i].Y += p[i].Vy;
            // 粒子垂直速度受重力影響
            p[i].Vy += 9.8*p[i].time/5;
            p[i].time++;
         }
    
         // 重繪畫面
         repaint();
 
         // 暫停執行緒 200 毫秒
         try {
             Thread.sleep(200);
         }
         catch (InterruptedException e) { }
      }
   }
}

// 煙火樹粒子類別
class ftparticle
{
   boolean state;  // 目前存活狀態
   double X,Y;      // 粒子的位置
   double Vx,Vy;    // 粒子水平與垂直速度
   int time;
   Color color;  // 粒子顏色

   // 建構子，初始粒子
   public ftparticle(int x,int y)
   {
      X = x;
      Y = y;
      reset(x,y);
   }

   // 重設粒子狀態
   public void reset(int x, int y)
   {
      
       X = x;
       Y = y;
       // 使用亂數決定水平與垂直速度
       Vx = (int)(Math.random()*10 - Math.random()*10);
       Vy = (int)(-Math.random()*20 - 1);
       time = 0;
       // 使用亂數決定粒子顏色
       color = new Color((int)(Math.random()*255),
                         (int) (Math.random()*255),
                         (int) (Math.random()*255));
   }
}

