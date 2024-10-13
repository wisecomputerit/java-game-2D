// 程式：電子小時鐘
// 範例檔：Clock.java

import java.awt.*;
import java.util.*;         //為了使用其中的GregorianCalendar類別
import java.applet.*;

public class Clock extends Applet implements Runnable
{
   //=====資料成員=====================================================
   int          hour,minute,second,ImageWidth,ImageHeight,X,Y,
                width,height;
   Image        digit[],Background,OffScreen;
   Thread       newThread;
   boolean      showSeparator;
   Graphics     drawOffScreen;
   MediaTracker MT;

   GregorianCalendar time;  //可用來取得時間與日期

   //=====函式成員=====================================================
   public void init()
   {
      X      = 50;          //時間數字被繪製的起始位置
      Y      = 110;
      MT     = new MediaTracker(this); 
      digit  = new Image[11];
      width  = 210;         //重繪區域的大小
      height = 135;

      //使用MediaTracker追蹤影像
      Background = getImage(getDocumentBase(),"Images/clock.jpg");
      MT.addImage(Background,0);

      for(int i=0;i<11;i++)
      {
         digit[i] = getImage(getDocumentBase(),"Images/" + i + ".jpg");
         MT.addImage(digit[i],0);
      }

      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }

      //取得影像的寬度與高度
      ImageWidth  = digit[0].getWidth(this);
      ImageHeight = digit[0].getHeight(this);

      //建立次畫面
      OffScreen     = createImage(300,300);
      drawOffScreen = OffScreen.getGraphics();

      showSeparator = true; //顯示時間分隔符號
   }

   public void start()                   //start()函式  
   {
      newThread = new Thread(this);      //建立與啟動新執行緒
      newThread.start();
   }

   public void stop()                    //stop()函式
   {
      newThread = null;                  //將執行緒設為null
   }

   public void paint(Graphics g)
   {
      //只清除此部分區域的影像
      drawOffScreen.clearRect(X,Y,width,height);
     
      //繪製背景影像
      drawOffScreen.drawImage(Background,0,0,this);

      if(hour < 10)     //繪製小時
      {
         drawOffScreen.drawImage(digit[0],X,Y,this);
         drawOffScreen.drawImage(digit[hour],X+ImageWidth,Y,this);
      }
      else
      {
         drawOffScreen.drawImage(digit[hour/10],X,Y,this);
         drawOffScreen.drawImage(digit[hour%10],X+ImageWidth,Y,this);
      }

      if(minute < 10)   //繪製分鐘
      {
          
         drawOffScreen.drawImage(digit[0],X+ImageWidth*2+10,Y,this);
         drawOffScreen.drawImage(digit[minute],X+ImageWidth*3+10,Y,this);
      }
      else
      {
         drawOffScreen.drawImage(digit[minute/10],X+ImageWidth*2+10,Y,this);
         drawOffScreen.drawImage(digit[minute%10],X+ImageWidth*3+10,Y,this);
      }

      if(second < 10)   //繪製秒數
      {
         drawOffScreen.drawImage(digit[0],X+ImageWidth*4+15,
                                 Y+ImageHeight-30,15,30,this);
         drawOffScreen.drawImage(digit[second],X+ImageWidth*4+30,
                                 Y+ImageHeight-30,15,30,this);
      }
      else
      {
         drawOffScreen.drawImage(digit[second/10],X+ImageWidth*4+15,
                                 Y+ImageHeight-30,15,30,this);
         drawOffScreen.drawImage(digit[second%10],X+ImageWidth*4+30,
                                 Y+ImageHeight-30,15,30,this);
      }     

      if(showSeparator) //繪製時間分隔符號(閃爍效果)
         drawOffScreen.drawImage(digit[10],X+ImageWidth*2,Y+10,this);

      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }

   public void update(Graphics g)        //update()函式
   {
      paint(g);                          //只單純呼叫paint()函式
   }

   public void run()
   {  
      while(newThread != null)           //動畫迴圈
      {  
         //time將包含目前系統的時間與日期 
         time   = new GregorianCalendar();

         //取出目前時間的小時、分鐘和秒數
         hour   = time.get(Calendar.HOUR_OF_DAY);
         minute = time.get(Calendar.MINUTE);
         second = time.get(Calendar.SECOND);

         if(second % 2 == 0)             //控制時間分隔符號的閃動
            showSeparator = true;
         else
            showSeparator = false;
 
         repaint(X,Y,width,height);      //重新繪製影像

         try
         {
            Thread.sleep(500);           //暫停程式執行500毫秒
         }
         catch(InterruptedException E){ }//沒有進行例外處理
      }
   }
}