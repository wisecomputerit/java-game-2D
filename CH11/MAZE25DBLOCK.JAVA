// 程式：加入障礙物的斜角地圖
// 範例檔：Maze25DBlock.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class Maze25DBlock extends Applet
{
   int          AppletWidth, AppletHeight,floorW,floorH,
                blockW,blockH,charW,key,sx;
   Image        floor,block,OffScreen,character2;
   Thread       newThread;
   Graphics     drawOffScreen; 
   MediaTracker MT;
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
      setBackground(Color.white);        //設定背景顏色
  
      AppletWidth  = getSize().width;    //取得Applet的高度
      AppletHeight = getSize().height;   //取得Applet的寬度

      //取得影像
      MT           = new MediaTracker(this);
      floor = getImage(getDocumentBase(),"Images/floor.gif");         
      block = getImage(getDocumentBase(),"Images/block3.gif");        
      character2 = getImage(getDocumentBase(),"Images/character2.gif");          
      MT.addImage(floor,0);
      MT.addImage(block,0);
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
 
      sx = 0;
   }

   public void paint(Graphics g)
   {
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);

       int X,Y;
      // 繪製地板
       for(int i=1; i < 16; i++)
          for(int j=1; j < 16; j++)
          {
             X = rotateX(j,i);
             Y = rotateY(j,i);
             drawOffScreen.drawImage(floor,X,Y,X+floorW,Y+floorH,0,0,floorW,floorH,this);
          }

      // 繪製障礙物
       for(int i=0; i < 16; i++)
          for(int j=0; j < 16; j++)
          {
             if(maze[i][j] != 0) {
                X = rotateX(j,i);
                Y = rotateY(j,i);
                drawOffScreen.drawImage(block,X,Y+4,X+blockW,
                    Y+blockH+4,0,0,blockW,blockH,this);
             }
          }        

      //將次畫面貼到主畫面中
      g.drawImage(OffScreen,0,0,this);
   }

   private int rotateX(int j, int i)
   {
      int x;
      x = (j-i)*floorW + 500;
      return (int) (x*Math.cos(60*Math.PI/180));
   }
   
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
}

