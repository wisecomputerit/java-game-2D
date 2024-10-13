// 程式：平面迷宮製作
// 範例檔：Maze1.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class Maze1 extends Applet 
             implements KeyListener, Runnable
{
   int          ImageWidth,ImageHeight,ImageX,ImageY,AppletWidth,
                AppletHeight,floorW,key;
   int sx;    // 角色貼圖來源位置
   char direction = 'R';  // 角色貼圖面向判斷用
   Image        character,floor,block1,block2,OffScreen;
   Thread       newThread;
   Graphics     drawOffScreen; 
   MediaTracker MT;
   Sprite s;   // 角色狀態物件
   // 迷宮陣列
   int[][] maze = {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                   {0,0,0,0,0,1,0,0,2,0,0,1,1,0,0,0,2,2},
                   {2,0,1,0,1,0,0,0,2,0,1,0,0,1,1,0,2,2},
                   {2,0,0,0,0,1,1,1,2,0,1,0,1,1,0,0,2,2},
                   {2,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,2,2},
                   {2,1,0,1,0,1,1,0,1,1,0,0,1,0,2,0,2,2},
                   {2,1,0,0,0,0,1,0,0,2,0,0,0,0,2,2,2,2},
                   {2,1,0,0,2,0,1,1,1,2,0,1,0,0,2,1,1,2},
                   {2,1,1,1,1,0,0,0,1,2,0,0,0,0,1,1,0,2},
                   {2,0,0,0,0,0,0,0,1,0,0,1,1,0,2,1,0,2},
                   {2,0,2,0,1,0,0,0,1,0,1,0,0,0,2,1,0,2},
                   {2,1,2,0,0,0,1,1,1,0,1,0,1,1,2,1,0,2},
                   {2,0,0,0,2,0,0,0,1,1,0,0,0,0,0,0,0,0},
                   {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

   public void init()
   {
      addKeyListener(this);              //註冊事件處理函式

      setBackground(Color.white);        //設定背景顏色
  
      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度
      sx = 0;

      //取得影像
      MT           = new MediaTracker(this);
      character = getImage(getDocumentBase(),"Images/character.gif");
      floor = getImage(getDocumentBase(),"Images/floor.jpg");         
      block1 = getImage(getDocumentBase(),"Images/block1.gif");
      block2 = getImage(getDocumentBase(),"Images/block2.gif");
      MT.addImage(character,0);
      MT.addImage(floor,0);
      MT.addImage(block1,0);
      MT.addImage(block2,0);
     
      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理
      
      //建立次畫面
      OffScreen     = createImage(AppletWidth,AppletHeight);
      drawOffScreen = OffScreen.getGraphics();

      floorW = floor.getWidth(this);
      ImageWidth    = character.getWidth(this) / 3;       // 單格影像的高度
      ImageHeight   = character.getHeight(this) / 4;      // 單格影像的寬度
      ImageX        = floorW;   //影像的X座標
      ImageY        = floorW/3;     //影像的Y座標

      // 初始角色物件
      s = new Sprite(ImageX, ImageY, ImageWidth, ImageHeight, maze);
   }

   // 啟動動畫迴圈執行緒
   public void start()
   {
      newThread = new Thread(this);
      newThread.start();
   }

   public void stop()
   {
      newThread = null;
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

    // =============== 繪圖 =========================== 
   
    public void run()
    {
        while(true)
        {
           drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);
           // 繪製地板
           for(int i=0; i <= AppletHeight; i+= floorW)
              for(int j=0; j <= AppletWidth; j+=floorW)
                 drawOffScreen.drawImage(floor,j,i,j+floorW,i+floorW,
                     0,0,floorW,floorW,this);
           // 根據迷宮陣列繪製障礙物
           for(int i=0; i < 14; i++)
              for(int j=0; j < 18; j++)
                  if(maze[i][j] == 1)  // 繪製蘿蔔
                     drawOffScreen.drawImage(block1,j*floorW,i*floorW,
                          (j+1)*floorW,(i+1)*floorW,0,0,floorW,floorW,this); 
                  else if(maze[i][j] == 2)  // 繪製香菇
                     drawOffScreen.drawImage(block2,j*floorW,i*floorW,
                          (j+1)*floorW,(i+1)*floorW,0,0,floorW,floorW,this);
        
           // 繪製角色
           switch (direction)
           {
               case 'U':    // 角色向上
                  drawOffScreen.drawImage(character,s.LcX,s.LcY,
                     s.LcX+s.SizeW,s.LcY+s.SizeH,sx,0, sx+s.SizeW,
                     s.SizeH,this);
                  break;
               case 'D':   // 角色向下
                  drawOffScreen.drawImage(character,s.LcX,s.LcY,
                     s.LcX+s.SizeW,s.LcY+s.SizeH,sx,s.SizeH*2,
                     sx+s.SizeW, s.SizeH*3,this);
                  break;
               case 'L':   // 角色向左
                   drawOffScreen.drawImage(character,s.LcX,s.LcY,
                     s.LcX+s.SizeW,s.LcY+s.SizeH,sx,s.SizeH*3,
                     sx+s.SizeW, s.SizeH*4,this);
                  break;
               case 'R':   // 角色向右
                  drawOffScreen.drawImage(character,s.LcX,s.LcY,
                     s.LcX+s.SizeW,s.LcY+s.SizeH,sx, s.SizeH,
                     sx+s.SizeW, s.SizeH*2,this);
                  break;
           }
           repaint();
           // 角色走動動畫
           sx += s.SizeW;
           if(sx > s.SizeW*2)
              sx = 0;

           try {
               Thread.sleep(200);
           }
           catch (InterruptedException e)
           {

           }
        }
    }

   //=====實作KeyListener介面==========================================
   public void keyTyped(KeyEvent e)  { }

   public void keyPressed(KeyEvent e)
   {
      key = e.getKeyCode();
   
      if(key == KeyEvent.VK_RIGHT)  // 按向右鍵
      {
          direction = 'R';  // 改變角色繪圖面向
          s.moveRight();   //  改變人物狀態
      }
      else if(key == KeyEvent.VK_LEFT)  // 按向左鍵
      {
          direction = 'L';
          s.moveLeft();
      }
      else if(key == KeyEvent.VK_UP)  // 按向上鍵
      {
         direction = 'U';       
         s.moveUp();
      }
      else if(key == KeyEvent.VK_DOWN)  // 按向下鍵
      {
         direction = 'D';       
         s.moveDown();
      }
   }

   public void keyReleased(KeyEvent e) {}
}

