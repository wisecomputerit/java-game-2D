// 程式：棒打豬頭
// 範例檔：HitPigHeadFrame.java

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

class PigSprite extends SuperSprite    //豬頭Sprite
{
   int     seed;                       //產生亂數用
   Image   SpriteImage,Frame;          //Sprite本身的影像
   Applet  Game;                       //在繪製影像時會用到
   Random  R;
   boolean showPig;                    //顯示豬頭影像

   public PigSprite(Image SpriteImage,Image Frame,Applet Game)
   {
      R = new Random();

      this.SpriteImage = SpriteImage;
      this.Frame       = Frame;
      this.Game        = Game;  
      showPig          = false; 
      
      setVisible(true);                 //可見
      setMove(true);                    //可移動 
   }

   public void updateState()
   {
      if(active == true)
      {
         //更新豬頭影像出現與消失的狀態
         if(R.nextInt(seed) % 100 < 1)
         {
            if(showPig == false)
               showPig = true;
         }
         else if(R.nextInt(seed) % 100 > 95)
         {
            if(showPig == true)
               showPig = false;
         }
      }
   }

   public void paintSprite(Graphics g)  //繪製豬頭Sprite
   {
      if(visible == true)     
      { 
         g.drawImage(Frame,X,Y,Game);   //在最後一個參數傳入Applet

         if(showPig == true)
            g.drawImage(SpriteImage,X + 12,Y + 18,Game);
      }
   }

   public void setSeed(int seed)
   {
      this.seed = seed;
   }

   //測試是否擊中豬頭影像
   public boolean hit(int X,int Y,int P_Width,int P_Height,int H_Width,
                      int H_Height)      
   {
      if((this.X + P_Width >= X) && (this.Y + (P_Height / 2) >= Y) && 
         (X + (H_Width / 2) >= this.X) && (Y + (H_Height / 2) >= this.Y) 
         && showPig)
      {
         showPig = false;
         return true;
      }
      else
         return false;
   }
}

class HammerSprite extends SuperSprite  //鐵鎚Sprite
{
   Image  hammer1,hammer2,currentImage; //鐵鎚影像
   Applet Game; 
 
   public HammerSprite(Image hammer1,Image hammer2,Applet Game)
   {
      this.hammer1 = hammer1;
      this.hammer2 = hammer2;
      this.Game    = Game;

      currentImage = hammer1;

      setLocation(0,0);
      setVisible(false);                //不可見
      setMove(false);                   //不可移動 
   }

   public void updateState()
   {
      //更新鐵鎚影像
      if(currentImage == hammer1)
         currentImage = hammer2;
      else
         currentImage = hammer1;
   }

   public void paintSprite(Graphics g)  //繪製鐵鎚Sprite
   {
      if(visible == true)      
         g.drawImage(currentImage,X,Y,Game); //在最後一個參數傳入Applet
   }
}

public class HitPigHeadFrame extends Applet  //主遊戲類別
             implements Runnable,MouseListener,MouseMotionListener
{
   int          AppletWidth,AppletHeight,FrameWidth,FrameHeight,
                countX,countY,HammerWidth,HammerHeight,score;
   Image        frame,pig,hammer1,hammer2,OffScreen;
   Thread       newThread; 
   Graphics     drawOffScreen;
   MediaTracker MT;   

   PigSprite    pigSprite[];
   HammerSprite hammerSprite;
   GameFrame G;

   public void init()
   {
      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);   

      AppletWidth  = getSize().width;
      AppletHeight = getSize().height;

      countX       = 3;                  //X軸3個豬頭texture
      countY       = 3;                  //Y軸3個豬頭texture
      score        = 0;                  //紀錄分數

      //使用MediaTracker追蹤影像
      MT      = new MediaTracker(this);
      pig     = getImage(getDocumentBase(),"Images/pig.gif");
      frame   = getImage(getDocumentBase(),"Images/frame.gif");
      hammer1 = getImage(getDocumentBase(),"Images/hammer1.gif");
      hammer2 = getImage(getDocumentBase(),"Images/hammer2.gif");
      MT.addImage(pig,0);
      MT.addImage(frame,0);
      MT.addImage(hammer1,0);
      MT.addImage(hammer2,0);

      try
      {
         MT.waitForAll();
      }      
      catch(InterruptedException E){ }   //沒有進行例外處理

      FrameWidth  = frame.getWidth(this);    //豬頭texture的寬度
      FrameHeight = frame.getHeight(this);   //豬頭texture的高度

      pigSprite   = new PigSprite[9];    //使用9個豬頭影像(3*3)
      for(int i=0;i<9;i++)
      {
         //建立豬頭Sprite
         pigSprite[i] = new PigSprite(pig,frame,this);

         //設定位置
         pigSprite[i].setLocation(i%countX*FrameWidth,
                                  i/countY*FrameHeight);
 
         pigSprite[i].setSeed(i+100);    //設定亂數範圍
      }         

      //建立鐵鎚Sprite
      hammerSprite   = new HammerSprite(hammer1,hammer2,this);
      HammerWidth    = hammer1.getWidth(this);   //鐵鎚影像高度
      HammerHeight   = hammer1.getHeight(this);  //鐵鎚影像寬度

      //建立次畫面
      OffScreen      = createImage(AppletWidth,AppletHeight);
      drawOffScreen  = OffScreen.getGraphics();

      resize(FrameWidth*countX,FrameHeight*countY);
      G = new GameFrame("棒打豬頭 v1.0 中文版",AppletWidth,AppletHeight,this);
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

      //繪製豬頭Sprite
      for(int i=0;i<9;i++)
         pigSprite[i].paintSprite(drawOffScreen);

      //繪製鐵鎚Sprite(注意Z-Order)
      hammerSprite.paintSprite(drawOffScreen);
           
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

         for(int i=0;i<9;i++)             //更新豬頭Sprite
            pigSprite[i].updateState();
      }
   }

   //=====實作MouseListener介面========================================
   public void mouseExited(MouseEvent e)      //滑鼠離開Component
   {
      hammerSprite.setVisible(false);         //不繪製鐵鎚Sprite
      hammerSprite.setLocation(0,0);          //歸位於原點
   }
   
   public void mouseClicked(MouseEvent e){ }  //滑鼠按鍵被按下後放開
   
   public void mouseEntered(MouseEvent e)     //滑鼠進入Component
   {
      hammerSprite.setVisible(true);          //繪製鐵鎚Sprite

      //將滑鼠游標置中於鐵鎚Sprite中央
      hammerSprite.setLocation(e.getX() - (HammerWidth / 2),
                               e.getY() - (HammerHeight / 2));
   }
   
   public void mousePressed(MouseEvent e)     //滑鼠按鍵被按下
   {
      hammerSprite.updateState();             //更新鐵鎚Sprite

      int X = hammerSprite.getX();            //取得鐵鎚Sprite的X座標
      int Y = hammerSprite.getY();            //取得鐵鎚Sprite的Y座標

      //測試是否擊中豬頭Sprite
      for(int i=0;i<9;i++)
      {
         //如果擊中則增加分數並將分數顯示在狀態列
         if(pigSprite[i].hit(X,Y,FrameWidth,FrameHeight,HammerWidth,
                             HammerHeight) == true)
         {
            score = score + 10;
            showStatus("目前得分: " + score);
         }
      }
   }

   public void mouseReleased(MouseEvent e)    //滑鼠按鍵放開
   {
      hammerSprite.updateState();             //更新鐵鎚Sprite
   }

   //=====實作MouseMotionListener介面==================================
   public void mouseMoved(MouseEvent e)       //滑鼠移動時
   {
      //將滑鼠游標置中於鐵鎚Sprite中央
      hammerSprite.setLocation(e.getX() - (HammerWidth / 2),
                               e.getY() - (HammerHeight / 2));
   }  
 
   public void mouseDragged(MouseEvent e)     //滑鼠拖曳時 
   {
      //將滑鼠游標置中於鐵鎚Sprite中央
      hammerSprite.setLocation(e.getX() - (HammerWidth / 2),
                               e.getY() - (HammerHeight / 2));
   }
}