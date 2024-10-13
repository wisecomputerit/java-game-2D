// 程式：火焰中的飛機
// 範例檔：Airplane.java

import java.awt.*;
import java.util.*;
import java.applet.*;
import java.awt.event.*;

abstract class SuperSprite     //這是Sprite父類別
{
   int     X,Y,width,height;
   boolean visible,active;

   abstract public void paintSprite(Graphics g);
   abstract public void updateState();

   public int getX()
   {
      return X;
   }

   public int getY()
   {
      return Y;
   }

   public void setLocation(int X,int Y)
   {
      this.X = X;
      this.Y = Y;
   }

   public int getWidth()
   {
      return width;
   }

   public int getHeight()
   {
      return height;
   }

   public void setSize(int width,int height)
   {
      this.width  = width;
      this.height = height;
   }

   public boolean canVisible()
   {
      return visible;
   }

   public void setVisible(boolean v)
   {
      visible = v;
   }

   public boolean canMove()
   {
      return active;
   }

   public void setMove(boolean m)
   {
      active = m;
   }
}

class ImageSprite extends SuperSprite   //影像Sprite
{
   Image  SpriteImage;                  //Sprite本身的影像
   Applet Game;                         //在繪製影像時會用到
  
   public ImageSprite(Image SpriteImage,Applet Game)
   {
      this.SpriteImage   = SpriteImage;
      this.Game          = Game;   
            
      setLocation(0,0);                 //設定起始位置
      setVisible(true);                 //可見
      setMove(true);                    //可移動 
   }

   public void updateState()
   {
   }

   public void paintSprite(Graphics g)  //繪製Sprite
   {
      if(visible == true)      
         g.drawImage(SpriteImage,X,Y,Game);  //在最後一個參數傳入Applet
   }
}

class BulletSprite extends ImageSprite  //子彈Sprite
{
   int AppletWidth,AppletHeight;

   public BulletSprite(Image bullet,Applet Game,int AppletWidth,
                       int AppletHeight)
   {
      super(bullet,Game);               //呼叫父類別的建構函式

      this.AppletWidth  = AppletWidth;
      this.AppletHeight = AppletHeight;

      setVisible(false);               //不可見
      setMove(false);                  //不可移動
   }

   public void updateState()           //更新子彈Sprite的狀態
   {
      if(active == true)
      {
         if(X > AppletWidth)           //當子彈穿過右邊界時
         {
            setVisible(false);         //設定子彈Sprite為不可見
            setMove(false);            //設定子彈Sprite為不可移動
         }
         else
            X = X + 10;                //向右移動20像素
      }
   }

   public void paintSprite(Graphics g) //繪製子彈Sprite
   {
      if(visible == true)
         g.drawImage(SpriteImage,X,Y,Game);
   }
}

public class Airplane extends Applet   //此為遊戲主類別
             implements Runnable,MouseListener,MouseMotionListener
{
   int          AppletWidth,AppletHeight,ImageWidth,ImageHeight,
                currentImage,mouseX,mouseY,X,Y;
   Image        background[],airplane,bullet,OffScreen;
   Random       R;
   Thread       newThread; 
   Graphics     drawOffScreen;
   MediaTracker MT;

   ImageSprite  airplaneSprite,BulletSprite;

   public void init()
   {
      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);   

      background   = new Image[5];
      MT           = new MediaTracker(this);
      R            = new Random();
      currentImage = 0;
      AppletWidth  = getSize().width;
      AppletHeight = getSize().height;

      //使用MediaTracker取得必要影像
      for(int i=0;i<5;i++)
      {
         background[i] = getImage(getDocumentBase(),"Images/thunder" +
                                  i + ".gif");

         MT.addImage(background[i],0);
      }

      airplane = getImage(getDocumentBase(),"Images/airplane.gif");
      bullet   = getImage(getDocumentBase(),"Images/bullet.gif"); 
      MT.addImage(airplane,0); 
      MT.addImage(bullet,0);

      try
      {
         MT.waitForAll();
      }      
      catch(InterruptedException E){ }   //沒有進行例外處理

      ImageWidth     = airplane.getWidth(this);
      ImageHeight    = airplane.getHeight(this);

      //建立Sprite
      airplaneSprite = new ImageSprite(airplane,this);
      BulletSprite   = new BulletSprite(bullet,this,AppletWidth,
                                        AppletHeight);
 
      //建立次畫面
      OffScreen      = createImage(AppletWidth,AppletHeight);
      drawOffScreen  = OffScreen.getGraphics();
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

      //注意繪製順序(Z-Order)
      drawOffScreen.drawImage(background[currentImage],0,0,this);
      airplaneSprite.paintSprite(drawOffScreen);
      BulletSprite.paintSprite(drawOffScreen);
           
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

         if(R.nextInt(10) < 1)            //更新畫框動畫
            currentImage = R.nextInt(5);

         BulletSprite.updateState();      //更新子彈Sprite狀態
      }
   }

   //=====實作MouseListener介面========================================
   public void mouseExited(MouseEvent e){}    //滑鼠離開Component
   public void mouseClicked(MouseEvent e){}   //滑鼠按鍵被按下後放開
    public void mouseReleased(MouseEvent e){} //滑鼠按鍵放開
   
   public void mouseEntered(MouseEvent e)     //滑鼠進入Component
   {
      mouseX = e.getX();
      mouseY = e.getY();

      //設定飛機的邊界動作
      if(mouseX < ImageWidth / 2)
         X = ImageWidth / 2;
      else if(mouseX > AppletWidth - (ImageWidth / 2))
         X = AppletWidth - (ImageWidth / 2);
      else
         X = mouseX;

      if(mouseY < ImageHeight / 2)
         Y = ImageHeight / 2;
      else if(mouseY > AppletHeight - (ImageHeight / 2))
         Y = AppletHeight - (ImageHeight / 2);
      else
         Y = mouseY;

      //設定飛機影像的正確位置
      airplaneSprite.setLocation(X - (ImageWidth / 2),Y - (ImageHeight / 2));
   }

   public void mousePressed(MouseEvent e)     //滑鼠按鍵被按下
   {
      //發射子彈
      if(BulletSprite.canVisible() == false && 
         BulletSprite.canMove() == false)
      {
         BulletSprite.setLocation(X + (ImageWidth / 2),Y);
         BulletSprite.setVisible(true);
         BulletSprite.setMove(true);
      }
   }
   
   //=====實作MouseMotionListener介面==================================
   public void mouseMoved(MouseEvent e)       //滑鼠移動時
   {
      //移動飛機
      mouseX = e.getX();
      mouseY = e.getY();

      //設定飛機的邊界動作
      if(mouseX < ImageWidth / 2)
         X = ImageWidth / 2;
      else if(mouseX > AppletWidth - (ImageWidth / 2))
         X = AppletWidth - (ImageWidth / 2);
      else
         X = mouseX;

      if(mouseY < ImageHeight / 2)
         Y = ImageHeight / 2;
      else if(mouseY > AppletHeight - (ImageHeight / 2))
         Y = AppletHeight - (ImageHeight / 2);
      else
         Y = mouseY;

      //設定飛機影像的正確位置
      airplaneSprite.setLocation(X - (ImageWidth / 2),Y - (ImageHeight / 2));  
   }  
 
   public void mouseDragged(MouseEvent e)     //滑鼠拖曳時 
   {
   }
}