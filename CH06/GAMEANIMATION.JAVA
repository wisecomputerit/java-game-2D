// 程式：角色碰撞實例
// 範例檔：GameAnimation.java

import java.awt.*;
import java.applet.*;

class Sprite1
{
   public  int     X,Y,width,height,VX,VY;

   int     AppletWidth,AppletHeight;
   boolean visible;
   Image UFO; // 小飛碟

   public Sprite1(int AppletWidth,int AppletHeight)
   {
      this.AppletWidth  = AppletWidth;   //Applet高度
      this.AppletHeight = AppletHeight;  //Applet寬度
      
      X       = AppletWidth / 2;     //起始X座標
      Y       = 0;     //起始Y座標
      VX      = -4;     //X軸速度
      VY      = -3;     //Y軸速度
      width   = 30;    //Sprite高度
      height  = 30;    //Sprite寬度
      visible = true;  //可見
   }

   public void updateState(Sprite2 s)    //更新Sprite狀態的函式
   {  
      X = X + VX;      //移動X軸方向
      Y = Y + VY;      //移動Y軸方向

      //碰撞偵測，若Sprite1和Sprite2相撞的話則改變速度為反方向
      if((X + width >= s.X) && (Y + height >= s.Y) && 
         (s.X + s.width >= X) && (s.Y + s.height >= Y))
      {
         VX   = -VX;
         VY   = -VY;
         s.VX = -s.VX;
         s.VY = -s.VY;
      }

      //底下的if-else判斷式在設定Sprite1的邊界動作
      if(X < 0) 
      {
         X  = 0;
         VX = -VX;
      }
      else if(X > AppletWidth - width) 
      {
         X  = AppletWidth - width; 
         VX = -VX;
      }

      if(Y < 0)
      {
         Y  = 0;
         VY = -VY;
      }
      else if(Y > AppletHeight - height)
      {
         Y  = AppletHeight - height;
         VY = -VY;
      }
   }

   public void paintSprite(Graphics g, Applet Game)   //繪製Sprite本身的函式
   {
      if(visible)
         g.drawImage(UFO,X,Y,width,height,Game);
   }
}

class Sprite2
{
   public  int     X,Y,width,height,VX,VY;

   int     AppletWidth,AppletHeight;
   boolean visible;
   Image beast;  // 大怪獸
  
   public Sprite2(int AppletWidth,int AppletHeight)
   {
      this.AppletWidth  = AppletWidth;   //Applet高度
      this.AppletHeight = AppletHeight;  //Applet寬度
      
      //設定Sprite2的位置、速度與大小
      X       = AppletWidth - width;  
      Y       = AppletHeight - height; 
      VX      = 5;
      VY      = 2;
      width   = 60;
      height  = 60;
      visible = true;
   }

   public void updateState(Sprite1 s)    //更新Sprite狀態的函式
   {
      X = X + VX;
      Y = Y + VY;

      //底下的if-else判斷式在設定Sprite的邊界動作
      if(X + width < 0) 
      {
         X = AppletWidth;
      }
      else if(X > AppletWidth) 
      {
         X = -width; 
      }

      if(Y < 0)
      {
         Y  = 0;
         VY = -VY;
      }
      else if(Y > AppletHeight - height)
      {
         Y  = AppletHeight - height;
         VY = -VY;
      }
   }

   public void paintSprite(Graphics g, Applet Game)   //繪製Sprite本身的函式
   {
      if(visible)
         g.drawImage(beast,X,Y,width,height,Game);
   }
}

public class GameAnimation extends Applet implements Runnable
{
   int      AppletWidth,AppletHeight;
   Image    OffScreen, bk;  
   Thread   newThread;                  
   Graphics drawOffScreen;              

   Sprite1 a;  
   Sprite2 b;

   public void init()
   {
      setBackground(Color.white);

      AppletWidth   = getSize().width;           //Applet的寬度
      AppletHeight  = getSize().height;          //Applet的高度

      a = new Sprite1(AppletWidth,AppletHeight); //建立Sprite1
      b = new Sprite2(AppletWidth,AppletHeight); //建立Sprite2
      a.UFO = getImage(getDocumentBase(),"Images/6.gif");
      b.beast = getImage(getDocumentBase(),"Images/1.gif");

      // 星空背景
      bk = getImage(getDocumentBase(),"Images/009.jpg");

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
      drawOffScreen.drawImage(bk,0,0,AppletWidth,AppletHeight,this);

      a.paintSprite(drawOffScreen, this);       //將Sprite1繪製在次畫面中
      b.paintSprite(drawOffScreen, this);       //將Sprite1繪製在次畫面中

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
            Thread.sleep(80);             //暫停80毫秒
         }
         catch(InterruptedException E){ }

         a.updateState(b);                //更新Sprite狀態
         b.updateState(a);
      }
   }
}