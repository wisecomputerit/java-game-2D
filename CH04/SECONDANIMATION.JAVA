// 程式：使用執行緒播放動畫
// 範例檔：SecondAnimation.java

import java.awt.*;
import java.net.*;
import java.applet.*;

public class SecondAnimation extends Applet implements Runnable
{
   URL          ErrorHTML;             //錯誤頁面的URL
   int          currentImage;          //目前影像
   Image        Animation[];           //動畫影像
   MediaTracker MT;                    //影像追蹤器

   Thread       newThread;             //新執行緒

   //===============init()函式和第一版本相同===========================
   public void init()
   {
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
      catch(InterruptedException E)    //若捕捉到例外
      { 
         try                           //建立錯誤頁面的URL
         {
            ErrorHTML = new URL(getCodeBase().toString() + "ErrorHTML.html");
         }
         catch(MalformedURLException ME){ }

         getAppletContext().showDocument(ErrorHTML);  //轉移頁面
      }
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
      //繪製目前影像
      g.drawImage(Animation[currentImage],50,10,250,250,this);
   }

   //===============新的動畫迴圈在此===================================
   public void run()
   {
      while(newThread != null)
      {
         repaint();                       //重新繪製影像

         try
         {
            Thread.sleep(125);            //暫停程式執行125毫秒
         }
         catch(InterruptedException E) 
         {
            try                           //建立錯誤頁面的URL
            {
                ErrorHTML = new URL(getCodeBase().toString() + "ErrorHTML.html");
            }
            catch(MalformedURLException ME){ }

            getAppletContext().showDocument(ErrorHTML);  //轉移頁面
         }
      
         if(currentImage == 7)            //如果已經播放到最後一張影像
            currentImage = 0;             //指定目前影像為第一張影像
         else
            currentImage = currentImage + 1;  //指定影像為下一張影像
      }
   }
}