// 程式：遊戲開頭畫面
// 範例檔：HitPigHead.java

import java.awt.*;
import java.util.*;     //為了使用其中的Random類別
import java.applet.*;

class StartScreen       //開始畫面類別
{
   //=====資料成員=====================================================
   int     width,height,StringWidth,StringHeight,Ascent,Descent,X,Y;
   int     ImageLeftBound,ImageRightBound,ImageX,ImageY,ImageWidth,
           ImageHeight,VX;
   Font    F1,F2,F3;
   Image   Normal,bkImage,Hit,currentImage;
   String  ChineseTitle,EnglishTitle,PressEnter;
   HitPigHead  Game;
   Random  R;
   boolean showPressEnter;

   FontMetrics FM;

   //=====函式成員=====================================================
   public StartScreen(int AppletWidth,int AppletHeight,HitPigHead Game,
                      Image normal,Image hit, Image bk)
   {
      R      = new Random();     //用來取亂數的類別  

      //繪製字串需要用到的三種字型
      F1     = new Font("TimesRoman",Font.BOLD,72);
      F2     = new Font("TimesRoman",Font.BOLD + Font.ITALIC,36);
      F3     = new Font("TimesRoman",Font.BOLD,20);

      ChineseTitle    = "棒打豬頭";                      //使用F1
      EnglishTitle    = "Hit Pig's Head";                //使用F2
      PressEnter      = "<<<==請按Enter鍵開始遊戲==>>>"; //使用F3

      width           = AppletWidth;                     //Applet的寬度
      height          = AppletHeight;                    //Applet的高度

      Normal          = normal;  //小豬影像1
      Hit             = hit;     //小豬影像2
      bkImage         = bk;      //背景影像
 
      ImageWidth      = Normal.getWidth(Game);    //小豬影像的寬度
      ImageHeight     = Normal.getHeight(Game);   //小豬影像的高度
      ImageLeftBound  = 25;                       //小豬影像移動的左邊界
      ImageRightBound = AppletWidth - (25 + ImageWidth);        //右邊界 
      ImageX          = ImageRightBound;          //小豬影像的起始位置 
 
      VX              = -3;      //影像移動的速度
      this.Game       = Game;
      currentImage    = Normal;  //指定目前影像為小豬影像1
      showPressEnter  = true;    //顯示PressEnter字串
   }

   public void UpdateStatus()       //更新動畫狀態的函式
   {
      ImageX = ImageX + VX;         //指定影像的新位置

      if(ImageX <= ImageLeftBound)  //如果碰到左邊界的話 
      {
         currentImage = Hit;        //指定目前影像為小豬影像2

         ImageX = ImageLeftBound;   //設定影像的新位置
         VX     = -VX;              //改變影像移動方向
      }

      if(ImageX >= ImageRightBound) //如果碰到右邊界的話
      {
         currentImage = Normal;     //指定目前影像為小豬影像1

         ImageX = ImageRightBound;  //設定影像的新位置
         VX     = -VX;              //改變影像移動方向
      }

      //利用亂數來讓字串閃動
      if(showPressEnter == true)
      {
         if((R.nextInt(5) + 1) % 5 == 0)
            showPressEnter = false;
      }
      else
      {
         if((R.nextInt(5) + 1) % 5 == 0)
            showPressEnter = true;
      }
   }

   public void paintScreen(Graphics g)   //繪製動畫的函式
   {
      g.clearRect(0,0,width,height);     //清除次畫面

      g.setFont(F1);                     //設定字型
      FM = g.getFontMetrics();

      Ascent       = FM.getAscent();
      Descent      = FM.getDescent();
      StringWidth  = FM.stringWidth(ChineseTitle);
      StringHeight = Ascent + Descent; 
      
      X            = (width - StringWidth) / 2;
      Y            = Ascent;
 
      g.drawImage(bkImage, 0, 0, Game);
      g.setColor(Color.white);           //設定前景顏色為白色
      g.drawString(ChineseTitle,X,Y);    //繪製中文標題

      Y            = StringHeight;
      g.drawLine(X,Y,X+StringWidth,Y);   //繪製線段1

      X            = X + 30;
      Y            = Y + 5;
      g.drawLine(X,Y,X+StringWidth-60,Y);//繪製線段2

      //===============================================================
      g.setFont(F2);                     //設定字型
      FM = g.getFontMetrics();

      Ascent       = FM.getAscent();
      Descent      = FM.getDescent();
      StringWidth  = FM.stringWidth(EnglishTitle);
      StringHeight = Ascent + Descent; 

      X            = (width - StringWidth) / 2;
      Y            = Y + Ascent;
      g.drawString(EnglishTitle,X,Y);    //繪製英文標題

      //===============================================================
      ImageY       = Y + Descent + 30; 
      g.drawImage(currentImage,ImageX,ImageY,Game);   //繪製小豬影像

      //===============================================================
      g.setFont(F3);                     //設定字型
      FM = g.getFontMetrics();

      Ascent       = FM.getAscent();
      Descent      = FM.getDescent();
      StringWidth  = FM.stringWidth(PressEnter);
      StringHeight = Ascent + Descent; 

      X            = (width - StringWidth) / 2;
      Y            = ImageY + ImageHeight + Ascent + 30;

      if(showPressEnter)
         g.drawString(PressEnter,X,Y);   //繪製PressEnter字串
   }
}

public class HitPigHead extends Applet implements Runnable
{
   //=====資料成員=====================================================
   int          AppletWidth,AppletHeight;
   Image        OffScreen,bkImage,PigHead_Normal,PigHead_Hit;
   Thread       newThread;
   Graphics     drawOffScreen;
   StartScreen  S_Screen;
   MediaTracker MT;

   //=====函式成員=====================================================
   public void init()
   {
      setBackground(Color.black);        //設定背景顏色

      //載入小豬影像1和2(使用MediaTracker)與背景
      PigHead_Normal = getImage(getDocumentBase(),"Images/pig1.gif");
      PigHead_Hit    = getImage(getDocumentBase(),"Images/pig2.gif");
      bkImage        = getImage(getDocumentBase(),"Images/009.jpg");
      
      MT = new MediaTracker(this);
      MT.addImage(PigHead_Normal,0);
      MT.addImage(PigHead_Hit,0);

      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理

      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度

      //建立開始畫面
      S_Screen = new StartScreen(AppletWidth,AppletHeight,this,
                 PigHead_Normal,PigHead_Hit, bkImage);
            
      //建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();
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
      //如果影像下載發生任何錯誤的話...
      if((MT.statusAll(false) & MediaTracker.ERRORED)  != 0)
      {
         FontMetrics FM = g.getFontMetrics();

         int Ascent      = FM.getAscent();
         int Descent     = FM.getDescent();
         int StringWidth = FM.stringWidth("載入影像發生錯誤...");

         int X = (AppletWidth - StringWidth) / 2;
         int Y = (AppletHeight - (Ascent + Descent)) / 2 + Ascent;

         g.setColor(Color.white);                  //字串設為白色 
         g.drawString("載入影像發生錯誤...",X,Y);  //置中顯示字串

         return;                                   //結束函式執行
      }   

      //將次畫面的繪製工具傳入以在次畫面中繪製影像
      S_Screen.paintScreen(drawOffScreen);

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
         repaint();                      //重新繪製影像

         try
         {
            Thread.sleep(80);            //暫停程式執行80毫秒
         }
         catch(InterruptedException E){ }//沒有進行例外處理
       
         S_Screen.UpdateStatus();        //更新開始畫面的動畫狀態
      }
   }
}