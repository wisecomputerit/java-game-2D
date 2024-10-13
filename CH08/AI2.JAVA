// 程式：角色跳離移動
// 範例檔：AI2.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

class Sprite
{
   int spriteX,spriteY,VX,VY,AppletWidth,AppletHeight;
   AI2 Game;
   Image    Hammer, Pig;

   public Sprite(Image Hammer, Image Pig,AI2 Game,int AppletWidth,int AppletHeight)
   {
      this.Game = Game; 
      this.Hammer = Hammer;
      this.Pig = Pig;
      this.AppletWidth  = AppletWidth;
      this.AppletHeight = AppletHeight;

      spriteX   = 0;
      spriteY   = 0;
      VX        = 0;
      VY        = 0;
   }

   public void updateState()
   {
      if(spriteX + 25 > Game.getX())
         VX = 1;
      else if(spriteX + 25 < Game.getX())
         VX = -1;
      else
         VX = 0;

      if(spriteY + 25 > Game.getY())
         VY = 1;
      else if(spriteY + 25 < Game.getY())
         VY = -1;
      else
         VY = 0;

      spriteX = spriteX + VX;
      spriteY = spriteY + VY;

      if(spriteX < 0)
         spriteX = 0;
      else if(spriteX > AppletWidth - 50)
         spriteX = AppletWidth - 50;

      if(spriteY < 0)
         spriteY = 0;
      else if(spriteY > AppletHeight - 50)
         spriteY = AppletWidth - 50;
   }

   public void paintSprite(Graphics g)
   {  
      g.drawImage(Pig,spriteX,spriteY,60,50,Game);
      g.drawImage(Hammer,Game.getX(),Game.getY(),50, 50,Game);
   }
}

public class AI2 extends Applet 
       implements Runnable,MouseListener,MouseMotionListener
{
   int      mouseX,mouseY,AppletWidth,AppletHeight;   
   Image    OffScreen, Hammer, Pig;
   Thread   newThread; 
   Graphics drawOffScreen;
   boolean  update;
 
   Sprite   S;

   public void init()
   {
      mouseX       = 0;
      mouseY       = 0;
      AppletWidth  = getSize().width;
      AppletHeight = getSize().height;
      update       = false;


      Hammer   = getImage(getDocumentBase(),"Images/hammer1.gif");
      Pig      = getImage(getDocumentBase(),"Images/pighead1.gif");
      //建立跳離Sprite
      S            = new Sprite(Hammer,Pig,this,AppletWidth,AppletHeight); 

      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);


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

      //繪製Sprite到次畫面中
      S.paintSprite(drawOffScreen);
           
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

         if(update)
            S.updateState();              //更新Sprite
      }
   }

   //==================================================================
   public int getX()    //讓Sprite取得滑鼠X座標
   {
      return mouseX;
   }

   public int getY()    //讓Sprite取得滑鼠Y座標
   {
      return mouseY;
   }

   //=====實作MouseListener介面========================================
   public void mouseExited(MouseEvent e)      //滑鼠離開Component
   {
      update = false;                         //不更新Sprite
   }
   
   public void mouseClicked(MouseEvent e){}   //滑鼠按鍵被按下後放開
   
   public void mouseEntered(MouseEvent e)     //滑鼠進入Component
   {
      update = true;                          //更新Sprite
   }
   
   public void mousePressed(MouseEvent e){}   //滑鼠按鍵被按下
   
   public void mouseReleased(MouseEvent e){}  //滑鼠按鍵放開
  
   //=====實作MouseMotionListener介面==================================
   public void mouseMoved(MouseEvent e)       //滑鼠移動時
   {
      mouseX = e.getX();                      //取得滑鼠X座標
      mouseY = e.getY();                      //取得滑鼠Y座標
      showStatus("(" + mouseX + "," + mouseY + ")");
   }  
 
   public void mouseDragged(MouseEvent e){}   //滑鼠拖曳時 
}