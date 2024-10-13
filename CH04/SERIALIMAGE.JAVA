// 程式：連續影像繪製
// 範例檔：SerialImage.java

import java.awt.*;
import java.applet.*;
import java.awt.image.*;

public class SerialImage extends Applet implements Runnable
{
   int          AppletWidth,AppletHeight,currentImage;          
   Image        Animation[],SerialImage,OffScreen;
   Thread       newThread;                          //新執行緒
   Graphics     drawOffScreen;
   MediaTracker MT;

   ImageProducer       Source;             //連續影像來源         
   CropImageFilter     CutImage;           //用來剪裁影像
    
   public void init()
   {
      MT            = new MediaTracker(this);
      Animation     = new Image[8];        //8張影像
      currentImage  = 0;                   //目前影像為第一張

      AppletWidth   = getSize().width;     //Applet的寬度
      AppletHeight  = getSize().height;    //Applet的高度

      //次畫面及繪製工具
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();

      //取得連續影像
      SerialImage = getImage(getDocumentBase(),"Images/serial.gif");
      MT.addImage(SerialImage,0);
      try
      {
         showStatus("影像載入中...");  //在狀態列顯示資訊
         MT.waitForAll();
      }
      catch(InterruptedException E)
      { 
         E.printStackTrace();          //列印錯誤資訊
      }

      //剪裁連續影像
      Source = SerialImage.getSource();
      for(int i=0;i<8;i++)
      {
         CutImage = new CropImageFilter(i*250,0,250,250);

         Animation[i] 
         = createImage(new FilteredImageSource(Source,CutImage));

         drawOffScreen.drawImage(Animation[i],0,0,this);
      }
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
      //底下這行的作用為清除次畫面
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
  
      //底下這行的作用為在次畫面上繪製目前影像
      drawOffScreen.drawImage(Animation[currentImage],0,0,this);
      
      //將次畫面貼到主畫面上
      g.drawImage(OffScreen,0,0,this); 
   }

   public void update(Graphics g)         //update()函式
   {
      paint(g);                           //只單純呼叫paint()函式
   }

   public void run()
   {
      while(newThread != null)            //動畫迴圈
      {
         repaint();                       //重新繪製影像

         try
         {
            Thread.sleep(80);             //暫停程式執行80毫秒
         }
         catch(InterruptedException E){ }
       
         currentImage = (++currentImage) % 8;  //指定目前影像
      }  
   }
}