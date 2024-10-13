// 程式：2.5D斜角迷宮
// 範例檔：Maze25D.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class Maze25D extends Applet
    implements Runnable, KeyListener  // 實作多執行緒介面與事件處理
{
   int          AppletWidth, AppletHeight,floorW,floorH,
                blockW,blockH,charW,key,sx;
   Image        floor,block,block2,OffScreen,character2;
   Thread       newThread;
   Graphics     drawOffScreen; 
   MediaTracker MT;
   Sprite2 s;  // Sprite2是角色類別
   // 迷宮陣列
   int[][] maze = {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                   {0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,1},
                   {1,0,1,0,0,0,0,0,1,0,1,0,0,0,1,1},
                   {1,0,0,0,1,1,1,1,1,0,1,0,1,1,0,1},
                   {1,1,0,0,0,0,0,0,0,0,0,1,1,1,0,1},
                   {1,1,0,1,0,1,1,0,1,1,0,0,1,0,0,1},
                   {1,1,0,0,0,0,1,0,0,1,0,0,0,0,1,1},
                   {1,1,0,0,2,0,1,1,1,1,0,1,0,0,1,1},
                   {1,1,1,1,1,0,0,0,1,1,0,0,0,0,1,1},
                   {1,0,0,0,0,0,0,0,1,0,0,1,1,0,1,1},
                   {1,0,1,0,1,0,0,0,1,0,1,0,0,0,1,1},
                   {1,1,1,0,0,0,1,1,1,0,1,0,1,1,1,1},
                   {1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,1},
                   {1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,1},
                   {1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0},
                   {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

   public void init()
   {
      addKeyListener(this);              //註冊事件處理函式

      setBackground(Color.white);        //設定背景顏色
  
      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度

      //取得影像
      MT           = new MediaTracker(this);
      floor = getImage(getDocumentBase(),"Images/floor.gif");         
      block = getImage(getDocumentBase(),"Images/block3.gif");        
      block2 = getImage(getDocumentBase(),"Images/block4.gif");    
      character2 = getImage(getDocumentBase(),"Images/character2.gif");          
      MT.addImage(floor,0);
      MT.addImage(block,0);
      MT.addImage(block2,0);
      MT.addImage(character2,0);
     
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
      floorH = floor.getHeight(this);
      blockW = block.getWidth(this);
      blockH = block.getHeight(this);
      charW = character2.getHeight(this);
 
      sx = 0;  // 動畫圖片來源座標

      s = new Sprite2(charW, maze);  // 初始角色狀態
   }

   public void start()
   {
      newThread = new Thread(this);
      newThread.start();
   }

   public void stop()
   {
      newThread = null;
   }

   public void update(Graphics g)   //update()函式
   {
      paint(g);                     //只單純呼叫paint()函式
   }

   public void paint(Graphics g)
   {
      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }

   // X 座標轉換
   private int rotateX(int j, int i)
   {
      int x;
      x = (j-i)*floorW + 500;
      return (int) (x*Math.cos(60*Math.PI/180));
   }

   // Y 座標轉換   
   private int rotateY(int j, int i)
   {
      int x, y;
      x = j*floorW + 150;
      y = -i*floorH;
      y = (int )-(y*Math.cos(-15*Math.PI/180) + 
             x*Math.sin(60*Math.PI/180)*Math.sin(-15*Math.PI/180));
      y = y - i * floorH/2;
      return y ;
   }

   // 利用多執行緒進行動畫繪製
    public void run()
    {
        while(true)
        {
          // 清除緩衝區繪圖頁
          drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);

           int X,Y;
          // 繪製地板
           for(int i=1; i < 16; i++)
              for(int j=1; j < 16; j++)
              {
                 // 根據索引值進行座標轉換
                 X = rotateX(j,i);
                 Y = rotateY(j,i);
                 drawOffScreen.drawImage(floor,X,Y,X+floorW,
                      Y+floorH,0,0,floorW,floorH,this);
              }

           // 繪製障礙物
           for(int i=0; i < 16; i++)
              for(int j=0; j < 16; j++)
              {
                 if(maze[i][j] != 0) {
                 // 根據索引值進行座標轉換
                    X = rotateX(j,i);
                    Y = rotateY(j,i);
                    drawOffScreen.drawImage(block,X,Y+4,X+blockW,
                        Y+blockH+4,0,0,blockW,blockH,this);
                 }
              }        

           // 根據索引值進行座標轉換
           X = rotateX(s.indexJ,s.indexI) - 5;
           Y = rotateY(s.indexJ,s.indexI) - 5;

           // 繪製角色
           drawOffScreen.drawImage(character2,X,Y,X+30,Y+30,
                                 sx, 0, sx+50,50,this);

           // 判斷右方有無障礙物，如果有就補貼障礙物
           if(maze[s.indexI][s.indexJ+1] != 0) {
              X = rotateX(s.indexJ+1,s.indexI);
              Y = rotateY(s.indexJ+1,s.indexI);
              drawOffScreen.drawImage(block2,X,Y+4,X+blockW,
                    Y+blockH+4,0,0,blockW,blockH,this);
           }

           // 判斷下方有無障礙物，如果有就補貼障礙物
           if(maze[s.indexI+1][s.indexJ] != 0) {
              X = rotateX(s.indexJ,s.indexI+1);
              Y = rotateY(s.indexJ,s.indexI+1);
              drawOffScreen.drawImage(block2,X,Y+4,X+blockW,
                    Y+blockH+4,0,0,blockW,blockH,this);
           }

           // 判斷右下方有無障礙物，如果有就補貼障礙物
           if(maze[s.indexI+1][s.indexJ+1] != 0) {
              X = rotateX(s.indexJ+1,s.indexI+1);
              Y = rotateY(s.indexJ+1,s.indexI+1);
              drawOffScreen.drawImage(block2,X,Y+4,X+blockW,
                    Y+blockH+4,0,0,blockW,blockH,this);
           }

           repaint();
           // 角色走動動畫
           sx += 50;
           if(sx >= 300)
              sx = 0;

           try {
               Thread.sleep(200);
           }
           catch (InterruptedException e)
           {

           }
        }
    }

   // ============鍵盤事件處理================

   public void keyTyped(KeyEvent e)  { }

   public void keyPressed(KeyEvent e)
   {
      key = e.getKeyCode();

      // 為配合斜角地圖，鍵盤的操作方向作了改變
      if(key == KeyEvent.VK_RIGHT)
         s.moveUp();  // 右鍵改為地圖的往上移動
      else if(key == KeyEvent.VK_LEFT)
         s.moveDown(); // 左鍵改為地圖的往下移動
      else if(key == KeyEvent.VK_UP)
          s.moveLeft();  // 上鍵改為地圖的往左移動
      else if(key == KeyEvent.VK_DOWN)
          s.moveRight(); // 下鍵改為地圖的往右移動
   }

   public void keyReleased(KeyEvent e) {}
}