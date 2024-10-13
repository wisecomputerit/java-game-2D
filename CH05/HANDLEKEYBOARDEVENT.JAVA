// 程式：人物走動
// 範例檔：HandleKeyboardEvent.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class HandleKeyboardEvent extends Applet 
             implements KeyListener
{
   int          ImageWidth,ImageHeight,ImageX,ImageY,AppletWidth,
                AppletHeight,key;
   int sx;
   Image        character,OffScreen;
   Thread       newThread;
   Graphics     drawOffScreen; 
   MediaTracker MT;

   public void init()
   {
      addKeyListener(this);              //註冊事件處理函式

      setBackground(Color.white);        //設定背景顏色
  
      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度
      sx = 0;

      //取得影像
      MT           = new MediaTracker(this);
      for(int i=0;i<5;i++)
      {
         character = getImage(getDocumentBase(),"Images/character.gif");
         
         MT.addImage(character,0);
      }
     
      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理
      
      //建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();
      
      ImageWidth    = character.getWidth(this) / 3;       //影像的高度
      ImageHeight   = character.getHeight(this) / 4;      //影像的寬度
      ImageX        = (AppletWidth - ImageWidth) / 3; //影像的X座標
      ImageY        = (AppletHeight - ImageHeight) / 3;     //影像的Y座標

      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
      drawOffScreen.drawImage(character,ImageX,ImageY,ImageX+72,ImageY+93,
                                 0, 0, 24, 31,this);
   }

   public void paint(Graphics g)
   {
      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }

   public void update(Graphics g)        //update()函式
   {
      paint(g);                          //只單純呼叫paint()函式
   }
   
   //=====實作KeyListener介面==========================================
   public void keyTyped(KeyEvent e)  { }

   public void keyPressed(KeyEvent e)
   {
      key = e.getKeyCode();

      // 清除次畫面
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
     
      if(key == KeyEvent.VK_RIGHT)
      {
         drawOffScreen.drawImage(character,ImageX,ImageY,ImageX+72,ImageY+93,
                                 sx, 31, sx+24, 62,this);
         
         repaint();
         // 人物走動
         ImageX += 2;
         sx += 24;
         if(sx > 48)
             sx = 0;

      }
      else if(key == KeyEvent.VK_LEFT)
      {
         drawOffScreen.drawImage(character,ImageX,ImageY,ImageX+72,ImageY+93,
                                 sx, 93, sx+24, 124,this);
         repaint();
         // 人物走動
         ImageX -= 2;
         sx += 24;
         if(sx > 48)
             sx = 0;
      }
      else if(key == KeyEvent.VK_UP)
      {
         drawOffScreen.drawImage(character,ImageX,ImageY,ImageX+72,ImageY+93,
                                 sx, 0, sx+24, 31,this);
         repaint();
         // 人物走動
         ImageY -= 2;
         sx += 24;
         if(sx > 48)
             sx = 0;
      }
      else if(key == KeyEvent.VK_DOWN)
      {
         drawOffScreen.drawImage(character,ImageX,ImageY,ImageX+72,ImageY+93,
                                 sx, 62, sx+24, 93,this);
         repaint();
         // 人物走動
         ImageY += 2;
         sx += 24;
         if(sx > 48)
             sx = 0;
      }
   }

   public void keyReleased(KeyEvent e)
   {
       sx = 0;
   }
}