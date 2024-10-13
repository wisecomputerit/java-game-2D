// 程式：煙粒子
// 範例檔：Smoke.java

import java.applet.*;
import java.awt.*;

public class Smoke extends Applet implements Runnable
{
   final int Max = 1000;
   sparticle p[]; // 煙粒子
   int AppletWidth,AppletHeight,XCenter,YCenter;
   Image        OffScreen;
   Graphics     drawOffScreen;
   Thread pThread;

   public void init()
   {
      setBackground(Color.black); // 設定背景為黑色

      // 取得顯像區域
      AppletWidth = getSize().width;
      AppletHeight = getSize().height;

      // 產生並初始化粒子
      p = new sparticle[Max];
      for(int i=0; i<Max; i++)
         p[i] = new sparticle(AppletWidth, AppletHeight);
      
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
      int i, speriod = 10, wperiod = 0, c = 255;
      double Wx = 0.0; // 風動影響


      while(true)
      {
         // 重清畫面
         drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);

         for(i=0; i < Max; i++)
         {
            drawOffScreen.setColor(new Color(p[i].color, p[i].color, p[i].color));
            drawOffScreen.fillOval((int)p[i].X,(int)p[i].Y,3,3);
            p[i].X += p[i].Vx + Wx; // 擴散
            p[i].Y -= p[i].Vy; // 上升

            if(p[i].Y < AppletHeight/2)
               p[i].color -= 10;  // 變淡
 
            if(p[i].color < 0)  // 如果完全消失
               p[i].reset(AppletWidth,AppletHeight);
          }

         if(wperiod > 0)
            wperiod--;
         else if(wperiod == 0)
         {
            speriod--;
            Wx =0;
            if(speriod == 0)
            {
               Wx = Math.random()*3 - Math.random()*3; // 風動影響
               wperiod = (int)(Math.random()*10+1); // 風吹時間
               speriod = (int)(Math.random()*10+1); // 下次風吹時間
            }
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
    
   public void wind()
   {
      
   }
}

// 煙粒子類別
class sparticle
{
   double X,Y;      // 粒子的位置
   double Vx,Vy;    // 粒子水平與垂直擴散速度
   int color;     // 粒子顏色

   // 建構子，初始粒子
   public sparticle(int w, int h)
   {
      reset(w, h);
   }

   // 重設粒子狀態
   public void reset(int w, int h)
   {
      X = w / 2;
      Y = h;
      // 以亂數決定水平與垂直擴散速度
      Vx = Math.random()-Math.random();
      Vy = Math.random() * 3 + 1;
      color = 255;
   }
}
