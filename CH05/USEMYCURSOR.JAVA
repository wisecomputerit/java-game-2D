z// 程式：使用自訂游標
// 範例檔：UseMyCursor.java
 
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class UseMyCursor extends Applet 
             implements MouseListener,MouseMotionListener
{
   int      AppletWidth,AppletHeight,drawX,drawY;
   Image        MyCursor1,MyCursor2;
   Cursor       myCursor1,myCursor2;
   Toolkit      TK;
   MediaTracker MT;
   Image    OffScreen;
   Graphics drawOffScreen;

   public void init()
   {
      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);   

      TK        = getToolkit();          //取得Toolkit

      //取得自訂游標影像
      MyCursor1 = getImage(getDocumentBase(),"Images/hammer1.gif");
      MyCursor2 = getImage(getDocumentBase(),"Images/hammer2.gif");
      MT        = new MediaTracker(this); 
      MT.addImage(MyCursor1,0);
      MT.addImage(MyCursor2,0);

      AppletWidth   = getSize().width;   //取得Applet的高度
      AppletHeight  = getSize().height;  //取得Applet的寬度

      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();

      try
      {
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理    

      //建立新游標
      try
      {
         myCursor1 = TK.createCustomCursor(MyCursor1,new Point(0,0),"hammer1"); 
         myCursor2 = TK.createCustomCursor(MyCursor2,new Point(0,0),"hammer2");   
      }
      catch(IndexOutOfBoundsException E){ }
  
      setCursor(myCursor1);              //設定使用新游標
   }

   public void update(Graphics g)        //update()函式
   {
      paint(g);                          //只單純呼叫paint()函式
   }

   public void paint(Graphics g)
   {
      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }


   //=====實作MouseListener介面========================================

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

   public void mousePressed(MouseEvent e)     //滑鼠按鍵被按下
   {
      setCursor(myCursor2);
      //如果是左鍵產生的mousePressed事件的話
      if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
      {
         drawX = e.getX();
         drawY = e.getY();
         drawOffScreen.drawOval(drawX+20,drawY+30,1,1);
         drawOffScreen.drawOval(drawX+15,drawY+25,10,10);
         drawOffScreen.drawOval(drawX+10,drawY+20,20,20);
         repaint();
      }
   }

   public void mouseExited(MouseEvent e){ }   //滑鼠離開Component
     
    public void mouseEntered(MouseEvent e){ }  //滑鼠進入Component
    
    public void mouseReleased(MouseEvent e)    //滑鼠按鍵放開
    {
       setCursor(myCursor1);
    }
 
    //=====實作MouseMotionListener介面==================================
    public void mouseMoved(MouseEvent e)       //滑鼠移動時
    {
       drawX = e.getX();
       drawY = e.getY();
       showStatus("(" + e.getX() + "," + e.getY() + ")");
    }  
  
    public void mouseDragged(MouseEvent e){ }  //滑鼠拖曳時 
}
