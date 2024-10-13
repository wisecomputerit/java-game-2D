// 程式：2.5D迷宮角色類別
// 範例檔：Sprite2.java

class Sprite2 {
   int SizeW;
   int maze[][];
   int indexI = 1, indexJ = 0;  // 角色

   // 建構子，初始角色狀態
   public Sprite2(int w, int[][] m)
   {
      SizeW = w;
      maze = m;     
   }

   public void moveUp()
   {
      // 上方是否可通過
      if(isPassed(indexI, indexJ,'U'))
         indexI--;
   }

   public void moveDown()
   {
      // 下方是否可通過
      if(isPassed(indexI, indexJ,'D'))
         indexI++;
   }

   public void moveRight()
   {
      // 右方是否可通過
      if(isPassed(indexI, indexJ,'R'))
         indexJ++;
   }

   public void moveLeft()
   {
      // 左方是否可通過
      if(isPassed(indexI, indexJ,'L'))
         indexJ--;
   }

   // 根據迷宮陣列判斷是否有障礙物
   private boolean isPassed(int i, int j, char d)
   {
       boolean pass = false;
       switch (d)
       {
           case 'U':
              if(maze[i-1][j] == 0)
                  pass = true;
              break;
           case 'D':
             if(maze[i+1][j] == 0)
                  pass = true;
              break;
           case 'L':
             if(maze[i][j-1] == 0)
                  pass = true;
             break;
           case 'R':
             if(maze[i][j+1] == 0)
                  pass = true;
              break;
       }

       return pass;
   }
}