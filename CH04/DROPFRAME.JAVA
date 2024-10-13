// 程式：使用過高的FPS
// 範例檔：DropFrame.java

import java.awt.*;
import java.applet.*;

public class DropFrame extends Applet implements Runnable
{
   int          SPF;                   //動畫播放的SPF
   int          currentImage;          //目前影像
   Image        Animation[];           //動畫影像
   MediaTracker MT;                    //影像追蹤器

   Thread       newThread;             //新執行緒

   Image    OffScreen;                 //次畫面
   Graphics drawOffScreen;             //繪製次畫面的Graphics實體

   //===============init()函式和第一版本相同===========================
   public void init()
   {
      SPF          = 11;               //相當於90FPS

      currentImage = 0;                //指定目前影像為第一張影像
      Animation    = new Image[8];     //與使用八張動畫影像
      MT           = new MediaTracker(this);
  
      for(int i=0;i<8;i++)             //注意此迴圈中的程式碼
      {
         Animation[i] 
         = getImage(getDocumentBase(),"Images/Freedom" + (i+1) + ".gif");

         MT.addImage(Animation[i],0);
      }
 
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
      drawOffScreen.drawImage(Animation[currentImage],0,0,250,250,this);

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
         }
         catch(InterruptedException E){ }
              
         currentImage = (++currentImage) % 8;  //指定目前影像
      }
   }
}