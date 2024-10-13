// 程式：處理滑鼠事件
// 範例檔：HandleMouseEvent.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;   //為了處理事件

//除了Runnable之外，尚實作了MouseListener及MouseMotionListener
public class HandleMouseEvent extends Applet 
             implements Runnable,MouseListener,MouseMotionListener
{
   int          AppletWidth,AppletHeight,hammerX,hammerY,
                hammerWidth,hammerHeight,clickCount;
   Image        hammer1,hammer2,OffScreen,currentHammer;
   Thread       newThread;
   boolean      showHammer;
   Graphics     drawOffScreen;
   MediaTracker MT;

   public void init()
   {  
      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);   

      setBackground(Color.gray);         //設定背景顏色
    
      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度
      clickCount   = 0;                  //mouseClick事件發生的次數
      
      //取得鐵鎚影像
      hammer1      = getImage(getDocumentBase(),"Images/hammer1.gif");
      hammer2      = getImage(getDocumentBase(),"Images/hammer2.gif");

      MT           = new MediaTracker(this); 
      MT.addImage(hammer1,0);
      MT.addImage(hammer2,0);
      
      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理
      
      hammerWidth   = hammer1.getWidth(this);   //取得鐵鎚影像寬度
      hammerHeight  = hammer1.getHeight(this);  //取得鐵鎚影像高度
      currentHammer = hammer1;                  //使用鐵鎚影像1
      showHammer    = false;                    //先不顯示鐵鎚

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
      //清除次畫面
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);

      //繪製鐵鎚影像
      if(showHammer)
         drawOffScreen.drawImage(currentHammer,hammerX,hammerY,this);

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
      }
   }

   //=====實作MouseListener介面========================================
   public void mouseExited(MouseEvent e)      //滑鼠離開Component
   {
      if(showHammer) showHammer = false;      //不繪製鐵鎚
   }

   public void mouseClicked(MouseEvent e)     //滑鼠按鍵被按下後放開
   {
      showStatus("揮動鐵鎚" + (++clickCount) + "次");
   }

   public void mouseEntered(MouseEvent e)     //滑鼠進入Component
   {
      if(!showHammer) showHammer = true;      //繪製鐵鎚
   }

   public void mousePressed(MouseEvent e)     //滑鼠按鍵被按下
   {
      currentHammer = hammer2;                //繪製鐵鎚影像2
   }

   public void mouseReleased(MouseEvent e)    //滑鼠按鍵放開
   {
      currentHammer = hammer1;                //繪製鐵鎚影像1
   }

   //=====實作MouseMotionListener介面==================================
   public void mouseMoved(MouseEvent e)       //滑鼠移動時
   {
      //設定鐵鎚影像的座標
      hammerX = e.getX() - (hammerWidth / 2);
      hammerY = e.getY() - (hammerHeight / 2);

      //碰到邊界時的狀態
      if(hammerX <= 0) 
         hammerX = 0;
      if(hammerX >= (AppletWidth - hammerWidth))
         hammerX = AppletWidth - hammerWidth;
      if(hammerY <= 0)
         hammerY = 0;
      if(hammerY >= (AppletHeight - hammerHeight))
         hammerY = AppletHeight - hammerHeight;

      showStatus("(" + hammerX + "," + hammerY + ")");
   }  
 
   public void mouseDragged(MouseEvent e)     //滑鼠拖曳時 
   {
      //設定鐵鎚影像的座標
      hammerX = e.getX() - (hammerWidth / 2);
      hammerY = e.getY() - (hammerHeight / 2);

      //碰到邊界時的狀態
      if(hammerX <= 0) 
         hammerX = 0;
      if(hammerX >= (AppletWidth - hammerWidth))
         hammerX = AppletWidth - hammerWidth;
      if(hammerY <= 0)
         hammerY = 0;
      if(hammerY >= (AppletHeight - hammerHeight))
         hammerY = AppletHeight - hammerHeight;

      showStatus("(" + hammerX + "," + hammerY + ")");
   }
}