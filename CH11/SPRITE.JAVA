// 程式：2D迷宮角色類別
// 範例檔：Sprite.java

class Sprite {
   int LcX, LcY;  // 角色的位置
   int SizeW, SizeH;  // 圖片大小
   int maze[][];  // 迷宮陣列
   int indexI = 1, indexJ = 1;  // 角色於陣列中的索引位置

   // 建構子，用來初始角色狀態
   public Sprite(int x, int y, int w, int h, int[][] m)
   {
      LcX = x;
      LcY = y;
      SizeW = w;
      SizeH = h;
      maze = m;     
   }

   public void moveUp()  // 角色向上
   {
      if(isPassed(indexI, indexJ,'U')) // 判斷是否可向上移動
      {
         LcY -= SizeW;  // 移動角色Y座標
         indexI--;      // 改變角色（縱向）索引位置
      }
   }

   public void moveDown()
   {
      if(isPassed(indexI, indexJ,'D'))
      {
         LcY += SizeW;
         indexI++;
      }
   }

   public void moveRight()
   {
      if(isPassed(indexI, indexJ,'R'))
      {
         LcX += SizeW;
         indexJ++;
      }
   }

   public void moveLeft()
   {
      if(isPassed(indexI, indexJ,'L'))
      {
         LcX -= SizeW;
         indexJ--;
      }
   }

   // 判斷角色是否可以移動
   private boolean isPassed(int i, int j, char d)
   {
       boolean pass = false;  
       switch (d)
       {
           case 'U':
              if(maze[i-1][j] == 0)  // 上方元素值是否為0
                  pass = true;  
              break;
           case 'D':
             if(maze[i+1][j] == 0)  // 下方元素值是否為0
                  pass = true;
              break;
           case 'L':
             if(maze[i][j-1] == 0)  // 左方元素值是否為0
                  pass = true;
             break;
           case 'R':
             if(maze[i][j+1] == 0) // 右方元素值是否為0
                  pass = true;
              break;
       }

       return pass;
   }
}