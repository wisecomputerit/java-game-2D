// 程式：笛卡兒數學公式 
// 範例檔：Heart.java

import java.applet.*;
import java.awt.*;

public class Heart extends Applet
{
   int AppletWidth,AppletHeight;
   Image        OffScreen;
   Graphics     drawOffScreen;

   public void init()
   {
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
      double x,y,r;

      for ( i = 0; i <= 90; i++ )
        for ( j = 0; j <= 90; j++ )
        {
            // 轉換為直角座標
            r=Math.PI/45*i*(1-Math.sin(Math.PI/45*j))*18; 
            x=r*Math.cos(Math.PI/45*j)*Math.sin(Math.PI/45*i)
              +AppletWidth/2;
            y=-r*Math.sin(Math.PI/45*j)+AppletHeight/4;

            // 投影在XZ平面
	    drawOffScreen.fillOval((int)x,(int)y,2,2); 
        }

      g.drawImage(OffScreen,0,0,this);
   }
}
