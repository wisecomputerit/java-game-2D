// 程式：立體座標定位
// 範例檔：Map45Rotate.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class Map45Rotate extends Applet 
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
      // 繪製地板
      for(int i=1; i <= 15; i++)
         for(int j=1; j <= 15; j++)
         {
            X = rotateX(j,i);  // X 座標轉換
            Y = rotateY(j,i);  // Y 座標轉換
            drawOffScreen.drawImage(floor,X,Y,X+floorW,
               Y+floorH,0,0,floorW,floorH,this);
         }

      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }

   // X 座標轉換
   private int rotateX(int j, int i)
   {
      int x;
      // 根據圖片大小先算出原來座標
      x = (j-i)*floorW + 450;    
      // 進行座標轉換
      x = (int) (x*Math.cos(60*Math.PI/180));

      // 傳回座標值
      return  x;
   }
   
   // Y 座標轉換
   private int rotateY(int j, int i)
   {
      int x, y;
      // 根據圖片大小先算出原來座標
      x = j*floorW + 150; 
      y = -i*floorH;
      // 進行座標轉換
      y = (int ) -(y*Math.cos(-15*Math.PI/180) + 
           x*Math.sin(60*Math.PI/180)*Math.sin(-15*Math.PI/180));
      y = y - i * floorH/2;

      // 傳回座標值
      return y ;
   }
}

