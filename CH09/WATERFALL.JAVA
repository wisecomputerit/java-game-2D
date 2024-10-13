// 程式：瀑布粒子
// 範例檔：WaterFall.java

import java.applet.*;
import java.awt.*;

public class WaterFall extends Applet implements Runnable
{
   final int Max = 1000;
   wparticle p[]; // 瀑布粒子
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
      p = new wparticle[Max];
      for(int i=0; i<Max; i++)
         p[i] = new wparticle();
      
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
      boolean reset = false;
      int i, t =0;
      while(true)
      {
         // 重清畫面
         drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
         drawOffScreen.setColor(Color.white);
         drawOffScreen.drawLine(0,15,10,15);  // 上方障礙物
  
         for(i=0; i<Max; i++)
         {
            // 繪製瀑布粒子
            drawOffScreen.fillOval((int)p[i].X,(int)p[i].Y,3,3);

            // 移動瀑布粒子
            p[i].X = p[i].X + p[i].Vx;
            if(p[i].X > 10)
            {
               // 重力加速度影響
               p[i].Y += p[i].Vy*p[i].time / 1000;
               p[i].Vy = (int) 9.8*p[i].time;
               p[i].time++;
            }

            // 如果超出顯示區域
            if(p[i].Y > AppletHeight)
            {
               // 重設粒子於上方，製造不斷流動的感覺
               p[i].reset();
            }
         }

         // 重繪畫面
         repaint();
 
         // 暫停執行緒 100 毫秒
         try {
             Thread.sleep(100);
         }
         catch (InterruptedException e) { }
      }
   }
}

// 瀑布粒子類別
class wparticle
{
   double X,Y;      // 粒子的位置
   double Vx,Vy;    // 粒子水平與垂直速度
   int time;     // 粒子下落時間

   // 建構子，初始粒子
   public wparticle()
   {
      reset();
   }

   // 重設粒子狀態
   public void reset()
   {
       X = (int) (Math.random() * -40);  // 粒子先產生在視窗外部
       Y = (int) (Math.random() * 5 + 10);  // 模擬粒子在水面的厚度
 
       // 使用亂數決定水平速度
       Vx = Math.random()*3 + 1.0;
       // 垂直速度為 0
       Vy = 0;
       // 落下時間為 0 
       time = 0;
   }
}

