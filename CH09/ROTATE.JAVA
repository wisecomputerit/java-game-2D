// 程式：旋轉體 
// 範例檔：Rotate.java

import java.applet.*;
import java.awt.*;

public class Rotate extends Applet
{
   int AppletWidth,AppletHeight;
   Image        OffScreen;
   Graphics     drawOffScreen;
   rt p[];

   public void init()
   {
      p = new rt[8];
      p[0] = new rt(250, 0);
      p[1] = new rt(174, 34);
      p[2] = new rt(140, 50);
      p[3] = new rt(120, 120);
      p[4] = new rt(100, 120);
      p[5] = new rt(60, 50);
      p[6] = new rt(28, 34);
      p[7] = new rt(-40, 0);

      setBackground(Color.black); // 設定背景為黑色
 
      // 取得顯像區域
      AppletWidth = getSize().width;
      AppletHeight = getSize().height;

      // 建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();
    }

   public void paint(Graphics g)
   {
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
      drawOffScreen.setColor(Color.white);

      int i,j;
      double xt,yt;

      for(i = 0; i <= 360; i += 30)
      {
          // 計算旋轉座標
          for(j = 0; j < 8; j++)
          {
              p[j].x = p[j].R*Math.cos(i*Math.PI/180);
              p[j].y = p[j].Y;
              p[j].z = p[j].R*Math.sin(i*Math.PI/180);
              // 投影xyz，傾斜20度
              xt = p[j].x;
              yt = p[j].y*Math.cos(20*Math.PI/180) -
                   p[j].z*Math.sin(20*Math.PI/180);

               // 平移x座標 
  	      p[j].x = xt*Math.cos(20*Math.PI/180) - 
                       yt*Math.sin(20*Math.PI/180) + AppletWidth/2;
               // 平移y座標 
              p[j].y = xt*Math.sin(20*Math.PI/180) + 
                       yt*Math.cos(20*Math.PI/180) + AppletHeight/4;
          }

          // 連接每一個頂點  
          for(j = 1; j < 8; j++)
          {
              drawOffScreen.drawLine((int)p[j-1].x,
                 (int)p[j-1].y,(int)p[j].x,(int)p[j].y);
              if(i > 0)
                 drawOffScreen.drawLine((int)p[j].px,
                    (int)p[j].py,(int)p[j].x,(int)p[j].y);

              p[j].px = p[j].x;
              p[j].py = p[j].y;
          }
      }

      g.drawImage(OffScreen,0,0,this);
   }
}

// 旋轉體頂點類別
class rt
{ 
   double Y,R;       // 旋轉體Y、R座標
   double x,y,z;     // 轉換後的直角座標
   double px, py;    // 前一次旋轉前的座標

   public rt(int y, int r)
   {
      Y = y;
      R = r;
      px = 0;
      py = 0;
   }
}
 