// 程式：橫向背景捲動
// 範例檔：ScrollScreen.java

import java.awt.*;
import java.applet.*;

public class ScrollScreen extends Applet implements Runnable
{
   int          AppletWidth,AppletHeight,ImageWidth,X1,X2;
   Image        scrollImage,scrollImage2,OffScreen, UFO;
   Thread       newThread; 
   Graphics     drawOffScreen;
   MediaTracker MT;

   public void init()
   {
      AppletWidth  = getSize().width;
      AppletHeight = getSize().height;

      //取得捲動畫面影像
      MT           = new MediaTracker(this);
      scrollImage  = getImage(getDocumentBase(),"Images/scroll.gif"); 
      UFO = getImage(getDocumentBase(),"Images/6.gif"); 
      MT.addImage(scrollImage,0); 
      MT.addImage(UFO,0);

      try
      {
         MT.waitForAll();
      }      
      catch(InterruptedException E){ }   //沒有進行例外處理  

      //複製捲動畫面影像
      scrollImage2 = createImage(scrollImage.getSource());

      ImageWidth   = scrollImage.getWidth(this);

      X1           = 0;                 //設定scrollImage起始位置
      X2           = -ImageWidth;       //設定scrollImage2起始位置
       
      //建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();
   }

   public void start()                    //start()函式  
   {
      newThread = new Thread(this);       //建立與啟動新執行緒
      newThread.start();
   }

   public void stop()                     //stop()函式
   {
      newThread = null;                   //將執行緒設為null
   }

   public void paint(Graphics g)
   {
      //只清除此部分區域的影像
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);

      //繪製捲動影像
      drawOffScreen.drawImage(scrollImage,X1,0,this);
      drawOffScreen.drawImage(scrollImage2,X2,0,this);
      drawOffScreen.drawImage(UFO,200,100,50,50,this);
                 
      //將次畫面貼到主畫面上
      g.drawImage(OffScreen,0,0,this); 
   }

   public void update(Graphics g)         //update()函式
   {
      paint(g);                           //只單純呼叫paint()函式
   }

   public void run()
   {
      while(newThread != null)
      {
         repaint();                       //重繪影像

         try
         {
            Thread.sleep(33);             //暫停33毫秒
         }
         catch(InterruptedException E){ }

         //更新影像位置(產生捲動效果)
         if(X1 == AppletWidth)
            X1 = -ImageWidth + 1;
         else
            X1++;

         if(X2 == AppletWidth)
            X2 = -ImageWidth + 1;
         else
            X2++;
      }
   }
}