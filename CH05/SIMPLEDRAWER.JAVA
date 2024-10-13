// 程式：簡單的繪圖遊戲
// 範例檔：SimpleDrawer.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class SimpleDrawer extends Applet 
             implements MouseListener,MouseMotionListener
{
   int      AppletWidth,AppletHeight,drawX,drawY;
   Image    OffScreen;
   Cursor   drawCursor,eraseCursor;
   Graphics drawOffScreen;
      
   public void init()
   {
      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);   

      setBackground(Color.white);        //設定背景為白色

      AppletWidth   = getSize().width;   //取得Applet的高度
      AppletHeight  = getSize().height;  //取得Applet的寬度

      drawCursor    = new Cursor(Cursor.CROSSHAIR_CURSOR);
      eraseCursor   = new Cursor(Cursor.HAND_CURSOR);

      //建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();

      showStatus("請開始繪圖...");
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
   
   //=====實作MouseListener介面========================================
   public void mouseExited(MouseEvent e)      //滑鼠離開Component
   {
      showStatus("繪圖動作結束...");
   }

   public void mouseClicked(MouseEvent e)     //滑鼠按鍵被按下後放開
   {
      //如果是右鍵產生的mouseClicked事件的話
      if((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
      {
         //清除次畫面
         drawOffScreen.setColor(getBackground());
         drawOffScreen.fillRect(0,0,AppletWidth,AppletHeight);
         drawOffScreen.setColor(getForeground());
         repaint();
      }   
   }

   public void mouseEntered(MouseEvent e)     //滑鼠進入Component
   {
      setCursor(drawCursor);
      showStatus("請開始繪圖...");
   }

   public void mousePressed(MouseEvent e)     //滑鼠按鍵被按下
   {
      //如果是左鍵產生的mousePressed事件的話
      if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
      {
         //繪製一個寬度與高度各為1的實心矩形
         drawX = e.getX();
         drawY = e.getY();
         drawOffScreen.fillRect(drawX,drawY,1,1);
         repaint();
      }
   }

   public void mouseReleased(MouseEvent e)    //滑鼠按鍵放開
   {
      showStatus("按下" + e.getClickCount() + "次");  
   }

   //=====實作MouseMotionListener介面==================================
   public void mouseMoved(MouseEvent e)       //滑鼠移動時
   {
      if(e.isShiftDown())                     //如果配合按下shift鍵
      {
         setCursor(eraseCursor);        

         //此為橡皮擦的功能
         drawX = e.getX();
         drawY = e.getY();
         drawOffScreen.setColor(getBackground());
         drawOffScreen.fillRect(drawX - 5,drawY - 5,10,10);
         drawOffScreen.setColor(getForeground());
         repaint();
     }
     else
        setCursor(drawCursor);
   }  
 
   public void mouseDragged(MouseEvent e)     //滑鼠拖曳時 
   {
      //如果是按下左鍵拖曳的話
      if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
      {
         //繪製粗線條
         drawOffScreen.fillRect(drawX-3,drawY-3,6,6);
         drawX = e.getX();
         drawY = e.getY();
         repaint();
      }
   }
}