// 程式：立體五角錐旋轉
// 範例檔：Cubic.java

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Cubic extends Applet implements Runnable, KeyListener
{
   int AppletWidth,AppletHeight;
   Image        OffScreen;
   Graphics     drawOffScreen;
   Thread pThread;
   top p[];
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
      p = new top[5];
      p[0] = new top(0,100,0,AppletWidth,AppletHeight);
      p[1] = new top(100,0,0,AppletWidth,AppletHeight);
      p[2] = new top(0,0,-100,AppletWidth,AppletHeight);
      p[3] = new top(-100,0,0,AppletWidth,AppletHeight);
      p[4] = new top(0,0,100,AppletWidth,AppletHeight);
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

         // 由上頂點連接四個底部頂點
        drawOffScreen.setColor(Color.white);
        for(i = 1; i < 5; i++)
           drawOffScreen.drawLine((int)p[0].xp,
              (int)p[0].yp,(int)p[i].xp,(int)p[i].yp);

        // 依序連接四個底部頂點
        for(i = 1; i < 4; i++)
           drawOffScreen.drawLine((int)p[i].xp,
              (int)p[i].yp,(int)p[i+1].xp,(int)p[i+1].yp);

        drawOffScreen.drawLine((int)p[4].xp,(int)p[4].yp,
              (int)p[1].xp,(int)p[1].yp);

        for(i=0; i < 5; i++)
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
   
      if(key == KeyEvent.VK_RIGHT)  // 按向右鍵
         ctrl = 'R';
      else if(key == KeyEvent.VK_LEFT)  // 按向左鍵
         ctrl = 'L';
      else if(key == KeyEvent.VK_UP)  // 按向上鍵
         ctrl = 'U';
      else if(key == KeyEvent.VK_DOWN)  // 按向下鍵
         ctrl = 'D';
      else if(key == KeyEvent.VK_PAGE_UP)  // 按Page Up鍵
         ctrl = 'Q';
      else if(key == KeyEvent.VK_PAGE_DOWN)  // 按Page Down鍵
         ctrl = 'A';
   }

   public void keyReleased(KeyEvent e) {}
}

// 頂點類別
class top
{ 
   double x;    // 起始x座標 
   double y;    // 起始y座標 
   double z;    // 起始z座標 
   double xp;   // 旋轉後投射於xy平面的x座標 
   double yp;   // 旋轉後投射於xy平面的y座標 
   double a, b, c;  // 旋轉角度
   int Xo, Yo;

   public top(double x, double y, double z, int Xo, int Yo)
   {
      this.x = x;
      this.y = y;
      this.z = z;
      xp = 0;
      yp = 0;
      this.Xo = Xo/2;
      this.Yo = Yo/2;
      a = 0;
      b = 0;
      c = 0;
   }

   public void rotate(char ctrl)
   {
      int i;
      double xt, yt, zt;
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
        // 按向上鍵
        case 'U':
            a--;
            break;
        // 按向下鍵
        case 'D':
            a++;
            break;
        // 按Page Up鍵
        case 'Q':
            c++;
            break;
        // 按Page Down鍵
        case 'A':
            c--;
            break;
    }

    // 套用公式
     xt = x*Math.cos(Math.PI/180*b) + z*Math.sin(Math.PI/180*b);
     yt = y;
     zt = -x*Math.sin(Math.PI/180*b) + z*Math.cos(Math.PI/180*b);
     yt = yt*Math.cos(Math.PI/180*a) - zt*Math.sin(Math.PI/180*a);
     // Xo用來平移x座標
     xp = xt*Math.cos(Math.PI/180*c) - yt*Math.sin(Math.PI/180*c) + Xo;  
     // Yo用來平移y座標
     yp = -xt*Math.sin(Math.PI/180*c) - yt*Math.cos(Math.PI/180*c) + Yo; 

    // 避免長時間執行a、b、c溢值
    if(a > 360 || a < -360)
        a = 0;
    if(b > 360 || b < -360)
        b = 0;
    if(c > 360 || c < -360)
        c = 0;
   }    
}
 