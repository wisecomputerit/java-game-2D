// 程式：自動產生迷宮
// 範例檔：AutoMaze.java

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class AutoMaze extends Applet
{
   int GridX=15,GridY=15;  // 迷宮格數
   int Si,Sj,Ei,Ej;        // 入口與出口
   int[][] maze = new int[30][30]; // 迷宮陣列
   int enter=20,width=20;  // 迷宮入口位置與每格的大小

   Button btn; 
   Panel p;
   Image wall1, wall2;
   MediaTracker MT;

   public void init() 
   {
      // 取得外牆圖片
      MT           = new MediaTracker(this);
      wall1 = getImage(getDocumentBase(),"Images/wall1.gif");
      wall2 = getImage(getDocumentBase(),"Images/wall2.gif");
      MT.addImage(wall1,0);
      MT.addImage(wall2,0);
     
      try
      {
         showStatus("影像載入中(Loading Images)...");
         MT.waitForAll();
      }
      catch(InterruptedException E){ }   //沒有進行例外處理

      btn = new Button("建立迷宮");
      // 按下按鈕自動產生迷宮陣列並繪製迷宮
      btn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
               int i,j;
               Si=1;Sj=1;Ei=GridY;Ej=GridX; // 設定出入口
               for (i=0;i<=GridY+1;i++) {
                  for (j=0;j<=GridX+1;j++) {
                     if (i==0 || j==0 || 
                         i==GridY+1 || j==GridX+1)
                        maze[i][j]=15; // 設定外牆
                     else
                        maze[i][j]=3;  // 初始迷宮內部
                    }
                }
                genmaze(Ei,Ej); // 產生迷宮
                maze[Ei][Ej]=maze[Ei][Ej] & 0xd; // 拆掉入口右牆
                repaint();
            }
         }
      );

      p = new Panel();
      p.add(btn);
      setLayout(new BorderLayout()); // 設定版面配置
      add(p, "South");      
   }

   // 遞迴產生迷宮陣列
   public void genmaze(int i, int j)
   {
      int n;
      maze[i][j] |= 0x4;  //  標示此格已設定
      while (maze[i][j+1]==3 || maze[i+1][j]==3 ||
             maze[i][j-1]==3 || maze[i-1][j]==3) // 如果不是外牆
      {
         n=(int)(4*Math.random()+1);  // 決定下一個位置
         if (n==1 && maze[i][j+1]==3) {  // 向右走
            maze[i][j] &= 0xd; // 拆掉右牆
            genmaze(i,j+1);  // 遞迴繼續走訪下一個位置 
            }
         else if (n==2 && maze[i-1][j]==3) { // 向下走
            maze[i][j] &= 0xe;    // 拆掉上牆
            genmaze(i-1,j); // 遞迴繼續走訪下一個位置 
         }
         else if (n==3 && maze[i][j-1]==3) { // 向左走
            maze[i][j-1] &= 0xd;  // 拆掉右牆
            genmaze(i,j-1);  // 遞迴繼續走訪下一個位置 
         }
         else if (n==4 && maze[i+1][j]==3) { // 向上走
            maze[i+1][j] &= 0xe; // 拆掉上牆
            genmaze(i+1,j); // 遞迴繼續走訪下一個位置 
         }
      }      
   }

   // 繪製迷宮
   public void paint(Graphics g)
   {
      int x, y, i, j;
      // 清除螢幕
      g.clearRect(0,0,(GridX+3)*width,(GridY+6)*width);
      // 繪製上外牆
      g.drawImage(wall1,enter,enter,(GridX+1)*width,2*width,0,0,100,100,this);

      // 繪製左外牆
      g.drawImage(wall2,0,2*width,width,(GridY+1)*width+5,0,0,100,100,this);

      // 繪製下外牆
      g.drawImage(wall1,enter,(GridY+1)*width,(GridX+1)*width,(GridY+2)*width,0,0,100,100,this);

      // 繪製右外牆
      g.drawImage(wall2,GridX*width,width,(GridX+1)*width,GridY*width,0,0,100,100,this);

      // 畫迷宮內部
      for (i=1;i<=GridY;i++) {
         for (j=1;j<=GridX;j++) {
            x=(j-1)*width+enter;
            y=(i-1)*width+enter;

            // 畫上牆
            if ((maze[i][j] & 1)==1) 
               g.drawImage(wall1,x-width/5,y,x+width,y+width,0,0,100,100,this);
            
            // 畫右牆
            if ((maze[i][j] & 2)==2) 
               g.drawImage(wall2,x,y,x+width,y+width,0,0,100,100,this);
         }
      }
   }
}