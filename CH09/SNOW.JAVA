// 程式：雪花粒子
// 範例檔：Snow.java

import java.applet.*;
import java.awt.*;

public class Snow extends Applet implements Runnable
{
   final int Max = 500;
   snowparticle p[]; // 雪花粒子
   int AppletWidth,AppletHeight;
   Image        OffScreen;
   Graphics     drawOffScreen;
   Thread pThread;

   public void init()
   {
      setBackground(Color.black); // 設定背景為黑色

      p = new snowparticle[Max]; // 建立粒子

      // 取得顯像區域
      AppletWidth = getSize().width;
      AppletHeight = getSize().height;

      // 產生並初始化粒子
      for(int i=0; i<Max; i++)
          p[i] = new snowparticle(AppletWidth,AppletHeight);

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
      int i,wperiod = 0,speriod = 10;
      double Wx = 0;
      while(true)
      {
         // 重清畫面
         drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
         drawOffScreen.setColor(Color.white);
  
         for(i=0; i<Max; i++)
         {
            // 依據雪花粒子位置與大小繪製雪花
            drawOffScreen.fillOval((int)p[i].X,(int)p[i].Y,
               (int)p[i].Size,(int)p[i].Size);
            // 水平移動雪花粒子，已考慮了粒子大小的因素
            p[i].X += Wx/p[i].Size;
            // 垂直移動雪花粒子，已考慮了粒子大小的因素
            p[i].Y += p[i].Vy * p[i].Size;

            // 如果超出顯像區域，重置粒子狀態            
            if(p[i].Y > AppletHeight)
               p[i].reset(AppletWidth,AppletHeight);
         }

         // 考慮風動
         if(wperiod > 0)
            wperiod--;
         else if(wperiod == 0)
         {
            // 考慮沒有風吹動時的處理
            speriod--;
            Wx =0;
            if(speriod == 0)
            {
               Wx = Math.random()*3 - Math.random()*3; // 風動影響
               wperiod = (int)(Math.random()*10+1); // 風吹時間
               speriod = (int)(Math.random()*10+1); // 下次風吹時間
            }
         }

         repaint();
 
         // 暫停執行緒 200 毫秒
         try {
             Thread.sleep(200);
         }
         catch (InterruptedException e) { }
      }
   }
}

// 雪花粒子類別
class snowparticle
{
   double X,Y;      // 粒子的位置
   double Vx,Vy;    // 粒子垂直速度
   double Size;     // 雪花大小

   // 建構子，初始粒子
   public snowparticle(int x,int y)
   {
      reset(x, y);
   }

   // 重設粒子狀態
   public void reset(int x, int y)
   {
       // 決定粒子位置
       X = Math.random()*x;
       Y = -Math.random()*y;
       // 決定水平與垂直速度
       Vx = 0;
       Vy = 1;
       // 以亂數決定粒子大小
       Size = Math.random() * 5 + 1;
   }
}

