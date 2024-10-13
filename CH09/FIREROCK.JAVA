// 祘Α废采
// 絛ㄒ郎FireRock.java

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class FireRock extends Applet implements Runnable
{
   final int Max = 200;
   fparticle p[]; // 废采
   int AppletWidth,AppletHeight,XCenter,YCenter;
   Image        OffScreen;
   Graphics     drawOffScreen;
   Thread pThread;

   public void init()
   {
      setBackground(Color.black); // 砞﹚璉春堵︹

      p = new fparticle[Max]; // ミ采

      // 眔陪钩跋办
      AppletWidth = getSize().width;
      AppletHeight = getSize().height;

      // 废﹍竚
      XCenter = (int) (Math.random()*AppletWidth);
      YCenter = (int) (Math.random()*AppletHeight);

      // 玻ネ﹍て采
      for(int i=0; i<Max; i++)
          p[i] = new fparticle(XCenter,YCenter);

      // ミΩ礶
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
         // 睲礶
         drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
  
         for(i=0; i<Max; i++)
         {
            // 狦禬陪钩跋办采
            if(p[i].X > AppletWidth || p[i].X < 0 || 
               p[i].Y > AppletHeight)
            {
               p[i].state = false;  
            }

            // 狦采玥酶籹采
            if(p[i].state)
            {
               drawOffScreen.setColor(p[i].color);
               drawOffScreen.fillOval(p[i].X,p[i].Y,3,3);
               // 采キ簿笆
   	       p[i].X += p[i].Vx;
               // 采紇臫
	       p[i].Y += p[i].Vy;
               // 采硉紇臫
	       p[i].Vy += 9*t;
            }                        
         }

         t++;  // 丁糤

         reset = true;  // 安砞ゲ斗竚采篈

         for(i=0; i<Max; i++)
         {
            if(p[i].state)  // 狦Τ采
            {
               reset = false; // 礚惠竚
               break;
            }
         }

         if(reset)  // 狦ゲ斗竚采篈
         {
            // ∕﹚采いみ
            XCenter = (int) (Math.random()*AppletWidth);
            YCenter = (int) (Math.random()*AppletHeight);
            t = 0; // 琁丁耴箂
            // 竚┮Τ采篈
            for(i=0; i<Max; i++)
               p[i].reset(XCenter,YCenter);
         }
      
         // 酶礶
         repaint();
 
         // 既氨磅︽狐 200 睝
         try {
             Thread.sleep(200);
         }
         catch (InterruptedException e) { }
      }
   }
}

// 废采摸
class fparticle
{
   boolean state;  // ヘ玡篈
   int X,Y;      // 采竚
   int Vx,Vy;    // 采キ籔硉
   Color color;  // 采肅︹

   // 篶﹍采
   public fparticle(int x,int y)
   {
      X = x;
      Y = y;
      reset(x,y);
   }

   // 砞采篈
   public void reset(int x, int y)
   {
       state = true; 
       X = x;
       Y = y;
       // ㄏノ睹计∕﹚キ籔硉
       Vx = (int)(Math.random()*25 - Math.random()*25);
       Vy = (int)(Math.random()*25 - Math.random()*25);
       // ㄏノ睹计∕﹚采肅︹
       color = new Color((int)(Math.random()*255),
                         (int) (Math.random()*255),
                         (int) (Math.random()*255));
   }
}

