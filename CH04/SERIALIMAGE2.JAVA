// 程式：二維連續影像播放
// 範例檔：SerialImage2.java

import java.awt.*;
import java.applet.*;

public class SerialImage2 extends Applet implements Runnable
{
   int          SPF;                   //動畫播放的SPF
   int sx, sy;                         //影像來源區域
   Image        Animation;             //動畫影像
   MediaTracker MT;                    //影像追蹤器

   Thread       newThread;             //新執行緒

   Image    OffScreen;                 //次畫面
   Graphics drawOffScreen;             //繪製次畫面的Graphics實體

   //===============init()函式和第一版本相同===========================
   public void init()
   {
      SPF          = 100;             
      MT           = new MediaTracker(this);
  
      Animation
         = getImage(getDocumentBase(),"Images/Cg0430.jpg");

      MT.addImage(Animation,0);
 
      try 
      {
         showStatus("影像載入中...");  //在狀態列顯示資訊
         MT.waitForAll();              //等待所有影像下載
      }
      catch(InterruptedException E){ }

      OffScreen     = createImage(250,250);      //建立次畫面
      drawOffScreen = OffScreen.getGraphics();   //取得次畫面的繪製類別
   }
   //==================================================================

   public void start()   //start()函式  
   {
      //建立與啟動新執行緒
      newThread = new Thread(this);
      newThread.start();
   }

   public void stop()    //stop()函式
   {
      newThread = null;  //將執行緒設為null
   }

   //==================================================================

   public void paint(Graphics g)    //已將差勁的動畫迴圈抽離
   {
      //底下這兩行的作用為清除次畫面
      drawOffScreen.setColor(Color.black);
      drawOffScreen.fillRect(0,0,250,250);

      //底下這行的作用為在次畫面上繪製目前影像
      drawOffScreen.drawImage(Animation,80,30,208,126,sx,sy,sx+128,sy+96,this);

      //將次畫面貼到主畫面上
      g.drawImage(OffScreen,0,0,250,250,this); 
   }

   public void update(Graphics g)
   {
      paint(g);
   }

   //===============新的動畫迴圈在此===================================
   public void run()
   {
      while(newThread != null)
      {
         repaint();                            //重新繪製影像

         try
         {
            Thread.sleep(SPF);                 //使用指定的SPF
            //計算來源區域
            sx += 128;
            if(sx == 640)
            {
               sy += 96;
               sx=0;
               if (sy == 480)
                  sy=0;
            }
         }
         catch(InterruptedException E){ }
      }
   }
}