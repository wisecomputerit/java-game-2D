// 程式：斜角地圖拼接
// 範例檔：Map45.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class Map45 extends Applet 
{
   int          AppletWidth, AppletHeight,floorW,floorH,key;
   Image        floor,OffScreen;
   Graphics     drawOffScreen; 
   MediaTracker MT;

   public void init()
   {
      setBackground(Color.white);        //設定背景顏色
  
      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度

      //取得影像
      MT           = new MediaTracker(this);
      floor = getImage(getDocumentBase(),"Images/floor.gif");         
      MT.addImage(floor,0);
     
      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理
      
      //建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();

      floorW = floor.getWidth(this);
      floorH = floor.getHeight(this);
   }

   public void paint(Graphics g)
   {
      int X,Y;
      // 繪製地板，內迴圈每次繪製兩排
      for(int i=0; i <= 16; i++)
         for(int j=0; j <= 10; j++)
         {
            X = j*floorW;
            Y = i*floorH;
            drawOffScreen.drawImage(floor,X,Y,X+floorW,Y+floorH,0,0,floorW,floorH,this);
            X += floorW/2;
            Y += floorH/2;
            drawOffScreen.drawImage(floor,X,Y,X+floorW,Y+floorH,0,0,floorW,floorH,this);
         }

      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }
}

