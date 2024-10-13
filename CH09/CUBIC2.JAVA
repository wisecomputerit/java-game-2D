// 程式：具遠近感的立體物件 
// 範例檔：Cubic2.java

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Cubic2 extends Applet implements Runnable, KeyListener
{
   int AppletWidth,AppletHeight;
   Image        OffScreen;
   Graphics     drawOffScreen;
   Thread pThread;
   top2 p[];
   char ctrl = 'R';  // 預設向右旋轉

   public void init()
   {
      setBackground(Color.black); // 設定背景為黑色
 
      addKeyListener(this);  // 註冊鍵盤事件處理

      // 取得顯像區域
      AppletWidth = getSize().width;
      AppletHeight = getSize().height;

      // 建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();

      // 建立並初始化頂點
      p = new top2[8];
      p[0] = new top2(100,100,100,AppletWidth,AppletHeight);
      p[1] = new top2(100,100,-100,AppletWidth,AppletHeight);
      p[2] = new top2(-100,100,-100,AppletWidth,AppletHeight);
      p[3] = new top2(-100,100,100,AppletWidth,AppletHeight);
      p[4] = new top2(100,-100,100,AppletWidth,AppletHeight);
      p[5] = new top2(100,-100,-100,AppletWidth,AppletHeight);
      p[6] = new top2(-100,-100,-100,AppletWidth,AppletHeight);
      p[7] = new top2(-100,-100,100,AppletWidth,AppletHeight);
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
         drawOffScreen.setColor(Color.yellow);

        // 繪製定向線
        drawOffScreen.drawLine((int)(AppletWidth/2),
           (int)(AppletHeight/2),(int)p[0].xp,(int)p[0].yp);  

       // 繪製邊長
       drawOffScreen.setColor(Color.white);
       for(i=0; i<=2; i++)
       {
          drawOffScreen.drawLine((int)p[i].xp,(int)p[i].yp,
             (int)p[i+1].xp,(int)p[i+1].yp);
          drawOffScreen.drawLine((int)p[i+4].xp,(int)p[i+4].yp,
             (int)p[i+5].xp,(int)p[i+5].yp);
          drawOffScreen.drawLine((int)p[i].xp,(int)p[i].yp,
             (int)p[i+4].xp,(int)p[i+4].yp);
       }

       drawOffScreen.drawLine((int)p[3].xp,(int)p[3].yp,
          (int)p[0].xp,(int)p[0].yp);
       drawOffScreen.drawLine((int)p[7].xp,(int)p[7].yp,
          (int)p[4].xp,(int)p[4].yp);
       drawOffScreen.drawLine((int)p[3].xp,(int)p[3].yp,
          (int)p[7].xp,(int)p[7].yp);


       for(i=0; i < 8; i++)
           p[i].rotate(ctrl);  // 旋轉頂點

         // 重繪畫面
         repaint();
 
         // 暫停執行緒 50 毫秒
         try {
             Thread.sleep(50);
         }
         catch (InterruptedException e) { }
      }
   }

   //=====實作KeyListener介面==========================================
   public void keyTyped(KeyEvent e)  { }

   public void keyPressed(KeyEvent e)
   {
      int key;
      key = e.getKeyCode();
   
      if(key == KeyEvent.VK_RIGHT)  // 按向右鍵，向右旋轉
         ctrl = 'R';
      else if(key == KeyEvent.VK_LEFT)  // 按向左鍵，向左旋轉
         ctrl = 'L';
      else if(key == KeyEvent.VK_UP)  // 按向上鍵，拉遠
         for(int i=0; i<8; i++)
            p[i].n++;
      else if(key == KeyEvent.VK_DOWN)  // 按向下鍵，拉近
         for(int i=0; i<8; i++)
            p[i].n--;
      else if(key == KeyEvent.VK_PAGE_UP)  // 按Page Up鍵，往上拉
         for(int i=0; i<8; i++)
            p[i].m++;
      else if(key == KeyEvent.VK_PAGE_DOWN)  // 按Page Down鍵，往下拉
         for(int i=0; i<8; i++)
            p[i].m--;
   }

   public void keyReleased(KeyEvent e) {}
}

// 頂點類別
class top2
{ 
   double x;    // 起始x座標 
   double y;    // 起始y座標 
   double z;    // 起始z座標 
   double xp;   // 旋轉後投射於xy平面的x座標 
   double yp;   // 旋轉後投射於xy平面的y座標 
   double b;  // 旋轉角度
   double l;    // 攝影機x座標 
   double m;    // 攝影機y座標 
   double n;    // 攝影機z座標 
   double p;   // 消失點

   int Xo, Yo;

   public top2(double x, double y, double z, int Xo, int Yo)
   {
      this.x = x;
      this.y = y;
      this.z = z;
      xp = 0;
      yp = 0;
      this.Xo = Xo/2;
      this.Yo = Yo/2;
      // 預設觀察點
      l = 2; 
      m = 50;
      n = 50; 
      // 預設消失點
      p = 100;
      b = 0;  // 預設角度0
   }

   public void rotate(char ctrl)
   {
      int i;
      double xt, yt, h;
      switch (ctrl)
      {
        // 按向右鍵
	case 'R':
            b++;
	    break;
        // 按向左鍵
	case 'L':
            b--;
	    break;
    }

    // 套用公式
     xt = x * Math.cos(b*Math.PI/180) +
          z * Math.sin(b*Math.PI/180) + 1;
     yt = y + m;
     h = -x * Math.sin(b*Math.PI/180) / p + 
          z * Math.cos(b*Math.PI/180) / p + n / p + l;
     xp = xt / h + Xo;
     yp = -yt / h + Yo;

    // 避免長時間執行b溢值
    if(b > 360 || b < -360)
        b = 0;
   }    
}
 