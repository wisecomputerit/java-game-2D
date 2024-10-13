import java.awt.*;
import java.util.*;
import java.applet.*;
import java.awt.event.*;

abstract class SuperSprite     //這是Sprite父類別
{
   int     X,Y,width,height;
   boolean visible,active;

   abstract public void paintSprite(Graphics g);
   abstract public void updateState();

   public int getX()
   {
      return X;
   }

   public int getY()
   {
      return Y;
   }

   public void setLocation(int X,int Y)
   {
      this.X = X;
      this.Y = Y;
   }

   public int getWidth()
   {
      return width;
   }

   public int getHeight()
   {
      return height;
   }

   public void setSize(int width,int height)
   {
      this.width  = width;
      this.height = height;
   }

   public boolean canVisible()
   {
      return visible;
   }

   public void setVisible(boolean v)
   {
      visible = v;
   }

   public boolean canMove()
   {
      return active;
   }

   public void setMove(boolean m)
   {
      active = m;
   }
}

class PigSprite extends SuperSprite    //豬頭Sprite
{
   int     seed;                       //產生亂數用
   Image   SpriteImage,Frame;          //Sprite本身的影像
   Applet  Game;                       //在繪製影像時會用到
   Random  R;
   boolean showPig;                    //顯示豬頭影像

   public PigSprite(Image SpriteImage,Image Frame,Applet Game)
   {
      R = new Random();

      this.SpriteImage = SpriteImage;
      this.Frame       = Frame;
      this.Game        = Game;  
      showPig          = false; 
      
      setVisible(true);                 //可見
      setMove(true);                    //可移動 
   }

   public void updateState()
   {
      if(active == true)
      {
         //更新豬頭影像出現與消失的狀態
         if(R.nextInt(seed) % 100 < 1)
         {
            if(showPig == false)
               showPig = true;
         }
         else if(R.nextInt(seed) % 100 > 95)
         {
            if(showPig == true)
               showPig = false;
         }
      }
   }

   public void paintSprite(Graphics g)  //繪製豬頭Sprite
   {
      if(visible == true)     
      { 
         g.drawImage(Frame,X,Y,Game);   //在最後一個參數傳入Applet

         if(showPig == true)
            g.drawImage(SpriteImage,X + 12,Y + 18,Game);
      }
   }

   public void setSeed(int seed)
   {
      this.seed = seed;
   }

   //測試是否擊中豬頭影像
   public boolean hit(int X,int Y,int P_Width,int P_Height,int H_Width,
                      int H_Height)      
   {
      if((this.X + P_Width >= X) && (this.Y + (P_Height / 2) >= Y) && 
         (X + (H_Width / 2) >= this.X) && (Y + (H_Height / 2) >= this.Y) 
         && showPig)
      {
         showPig = false;
         return true;
      }
      else
         return false;
   }
}

class HammerSprite extends SuperSprite  //鐵鎚Sprite
{
   Image  hammer1,hammer2,currentImage; //鐵鎚影像
   Applet Game; 
 
   public HammerSprite(Image hammer1,Image hammer2,Applet Game)
   {
      this.hammer1 = hammer1;
      this.hammer2 = hammer2;
      this.Game    = Game;

      currentImage = hammer1;

      setLocation(0,0);
      setVisible(false);                //不可見
      setMove(false);                   //不可移動 
   }

   public void updateState()
   {
      //更新鐵鎚影像
      if(currentImage == hammer1)
         currentImage = hammer2;
      else
         currentImage = hammer1;
   }

   public void paintSprite(Graphics g)  //繪製鐵鎚Sprite
   {
      if(visible == true)      
         g.drawImage(currentImage,X,Y,Game); //在最後一個參數傳入Applet
   }
}

public class HitPigHeadv2 extends Applet  //主遊戲類別
       implements Runnable,MouseListener,MouseMotionListener,ActionListener
{
   int          AppletWidth,AppletHeight,FrameWidth,FrameHeight,
                countX,countY,HammerWidth,HammerHeight,score,
                CurrentSecond,GameSecond;
   Image        frame,pig,hammer1,hammer2,OffScreen,PigHead1,bkImage,
                PigHead2;
   Thread       newThread; 
   Graphics     drawOffScreen;
   MediaTracker MT;   

   PigSprite    pigSprite[];
   HammerSprite hammerSprite;

   Panel        Status,Control;
   Label        Time,Score;
   Button       start,end;
   boolean      StartGame,EndGame;
   StartScreen  S_Screen;
   CloseDialog  CD;

   GregorianCalendar time;            //可用來取得時間與日期

   public void init()
   {
      addMouseListener(this);            //註冊事件處理函式
      addMouseMotionListener(this);   
      
      CD      = new CloseDialog(this,new Frame());

      Time    = new Label("時間: 0");
      Score   = new Label("得分: 0");
      end     = new Button("結束遊戲");
      start   = new Button("開始遊戲");
      end.addActionListener(this);
      start.addActionListener(this);
      end.setEnabled(false);

      Status  = new Panel();
      Control = new Panel();
      Status.setLayout(new GridLayout(1,2));
      Control.setLayout(new GridLayout(1,2));
      
      Status.add(Time);
      Status.add(Score);
      Control.add(start);
      Control.add(end);

      setLayout(new BorderLayout());
      add(Status,BorderLayout.NORTH);
      add(Control,BorderLayout.SOUTH);
      
      AppletWidth   = getSize().width;
      AppletHeight  = getSize().height;
      countX        = 3;                  //X軸3個豬頭texture
      countY        = 3;                  //Y軸3個豬頭texture
      score         = 0;                  //紀錄分數
      GameSecond    = 0;
      CurrentSecond = -1;
      StartGame     = true;      
      EndGame       = false;
 
      //使用MediaTracker追蹤影像
      MT       = new MediaTracker(this);
      pig      = getImage(getDocumentBase(),"Images/pig.gif");
      frame    = getImage(getDocumentBase(),"Images/frame.gif");
      hammer1  = getImage(getDocumentBase(),"Images/hammer1.gif");
      hammer2  = getImage(getDocumentBase(),"Images/hammer2.gif");
      PigHead1 = getImage(getDocumentBase(),"Images/pighead1.gif");
      PigHead2 = getImage(getDocumentBase(),"Images/pighead2.gif");
      bkImage        = getImage(getDocumentBase(),"Images/009.jpg");
      MT.addImage(pig,0);
      MT.addImage(frame,0);
      MT.addImage(hammer1,0);
      MT.addImage(hammer2,0);
      MT.addImage(PigHead1,0);
      MT.addImage(PigHead2,0);
      MT.addImage(bkImage,0);

      try
      {
         MT.waitForAll();
      }      
      catch(InterruptedException E){ }   //沒有進行例外處理

      FrameWidth  = frame.getWidth(this);    //豬頭texture的寬度
      FrameHeight = frame.getHeight(this);   //豬頭texture的高度

      pigSprite   = new PigSprite[9];    //使用9個豬頭影像(3*3)
      for(int i=0;i<9;i++)
      {
         //建立豬頭Sprite
         pigSprite[i] = new PigSprite(pig,frame,this);

         //設定位置
         pigSprite[i].setLocation(i%countX*FrameWidth,
                                  i/countY*FrameHeight);
 
         pigSprite[i].setSeed(i+100);    //設定亂數範圍
      }         

      //建立鐵鎚Sprite
      hammerSprite   = new HammerSprite(hammer1,hammer2,this);
      HammerWidth    = hammer1.getWidth(this);   //鐵鎚影像高度
      HammerHeight   = hammer1.getHeight(this);  //鐵鎚影像寬度

      //建立開始畫面
      S_Screen = new StartScreen(AppletWidth,AppletHeight,this,
                                 PigHead1,PigHead2,bkImage);

      //建立次畫面
      OffScreen      = createImage(AppletWidth,AppletHeight);
      drawOffScreen  = OffScreen.getGraphics();

      resize(FrameWidth*countX,FrameHeight*countY + 70);
   }

   public void start()                    //start()函式  
   {
      newThread = new Thread(this);       //建立與啟動新執行緒
      newThread.start();
   }

   public void stop()                     //stop()函式
   {
      newThread = null;                   //將執行緒設為null
   }

   public void paint(Graphics g)
   {
      //只清除此部分區域的影像
      drawOffScreen.clearRect(0,0,AppletWidth,AppletHeight);

      if(StartGame)
         //將次畫面的繪製工具傳入以在次畫面中繪製影像
         S_Screen.paintScreen(drawOffScreen); 
      else
      {  
         //繪製豬頭Sprite
         for(int i=0;i<9;i++)
            pigSprite[i].paintSprite(drawOffScreen);

         //繪製鐵鎚Sprite(注意Z-Order)
         hammerSprite.paintSprite(drawOffScreen);
      }
           
      //將次畫面貼到主畫面上
      g.drawImage(OffScreen,0,35,this); 
   }

   public void update(Graphics g)         //update()函式
   {
      paint(g);                           //只單純呼叫paint()函式
   }

   public void run()
   {
      while(newThread != null)
      {
         repaint();                       //重繪影像

         try
         {
            Thread.sleep(33);             //暫停33毫秒
         }
         catch(InterruptedException E){ }

         if(StartGame)
            S_Screen.UpdateStatus();        //更新開始畫面的動畫狀態
         else
         {
            //time將包含目前系統的時間與日期 
            time   = new GregorianCalendar();

            if(CurrentSecond != time.get(Calendar.SECOND))
            {
               CurrentSecond = time.get(Calendar.SECOND);
            
               GameSecond++;
               Time.setText("時間: " + GameSecond + "秒");
            }

            for(int i=0;i<9;i++)             //更新豬頭Sprite
               pigSprite[i].updateState();
         }
      }
   }

   public void endGame(boolean isEndGame)
   {
      EndGame = isEndGame;
   }

   //=====實作MouseListener介面========================================
   public void mouseExited(MouseEvent e)      //滑鼠離開Component
   {
      hammerSprite.setVisible(false);         //不繪製鐵鎚Sprite
      hammerSprite.setLocation(0,0);          //歸位於原點
   }
   
   public void mouseClicked(MouseEvent e){ }  //滑鼠按鍵被按下後放開
   
   public void mouseEntered(MouseEvent e)     //滑鼠進入Component
   {
      hammerSprite.setVisible(true);          //繪製鐵鎚Sprite

      //將滑鼠游標置中於鐵鎚Sprite中央
      hammerSprite.setLocation(e.getX() - (HammerWidth / 2),
                               e.getY() - (HammerHeight / 2) - 35);
      showStatus("X:" + e.getX() + "," + "Y:" + e.getY());
   }
   
   public void mousePressed(MouseEvent e)     //滑鼠按鍵被按下
   {
      if(StartGame) return;
      
      hammerSprite.updateState();             //更新鐵鎚Sprite

      int X = hammerSprite.getX();            //取得鐵鎚Sprite的X座標
      int Y = hammerSprite.getY();            //取得鐵鎚Sprite的Y座標

      //測試是否擊中豬頭Sprite
      for(int i=0;i<9;i++)
      {
         //如果擊中則增加分數並將分數顯示在狀態列
         if(pigSprite[i].hit(X,Y,FrameWidth,FrameHeight,HammerWidth,
                             HammerHeight) == true)
         {
            score = score + 10;
            Score.setText("得分: " + score);
         }
      }
   }

   public void mouseReleased(MouseEvent e)    //滑鼠按鍵放開
   {
      if(StartGame) return;

      hammerSprite.updateState();             //更新鐵鎚Sprite
   }

   //=====實作MouseMotionListener介面==================================
   public void mouseMoved(MouseEvent e)       //滑鼠移動時
   {
      //將滑鼠游標置中於鐵鎚Sprite中央
      hammerSprite.setLocation(e.getX() - (HammerWidth / 2),
                               e.getY() - (HammerHeight / 2) - 35);
      showStatus("X:" + e.getX() + "," + "Y:" + e.getY());
   }  
 
   public void mouseDragged(MouseEvent e)     //滑鼠拖曳時 
   {
      //將滑鼠游標置中於鐵鎚Sprite中央
      hammerSprite.setLocation(e.getX() - (HammerWidth / 2),
                               e.getY() - (HammerHeight / 2) - 35);
   }

   //=====實作ActionListener介面=======================================
   public void actionPerformed(ActionEvent e)
   {
      if(e.getSource() == start)
      {
         StartGame = false;
 
         start.setEnabled(false);
         end.setEnabled(true);
      }
      
      if(e.getSource() == end)           //當結束遊戲按鈕被按下時
      {
         newThread = null;               //停止動畫迴圈
         CD.show();                      //顯示對話視窗

         if(EndGame)                     //如果要結束遊戲的話
         {
            //恢復遊戲原始設定
            score         = 0;
            GameSecond    = 0;
            CurrentSecond = -1;
            StartGame     = true;
            EndGame       = false;
            start.setEnabled(true);
            end.setEnabled(false);

            Time.setText("時間: 0");
            Score.setText("得分: 0");
         }

         newThread = new Thread(this);   //建立執行緒
         newThread.start();              //啟動執行緒
      }
   }
}

class StartScreen       //開始畫面類別
{
   //=====資料成員=====================================================
   int     width,height,StringWidth,StringHeight,Ascent,Descent,X,Y;
   int     ImageLeftBound,ImageRightBound,ImageX,ImageY,ImageWidth,
           ImageHeight,VX;
   Font    F1,F2,F3;
   Image   Normal,Hit,currentImage,bkImage;
   String  ChineseTitle,EnglishTitle,PressEnter;
   Applet  Game;
   Random  R;
   boolean showPressEnter;

   FontMetrics FM;

   //=====函式成員=====================================================
   public StartScreen(int AppletWidth,int AppletHeight,HitPigHeadv2 Game,
                      Image normal,Image hit, Image bk)
   {
      R      = new Random();     //用來取亂數的類別  

      //繪製字串需要用到的三種字型
      F1     = new Font("TimesRoman",Font.BOLD,72);
      F2     = new Font("TimesRoman",Font.BOLD + Font.ITALIC,36);
      F3     = new Font("TimesRoman",Font.BOLD,20);

      ChineseTitle    = "棒打豬頭";                      //使用F1
      EnglishTitle    = "Hit Pig's Head";                //使用F2
      
      width           = AppletWidth;                     //Applet的寬度
      height          = AppletHeight;                    //Applet的高度

      Normal          = normal;  //小豬影像1
      Hit             = hit;     //小豬影像2
      bkImage         = bk;      //背景影像
 
      ImageWidth      = Normal.getWidth(Game);    //小豬影像的寬度
      ImageHeight     = Normal.getHeight(Game);   //小豬影像的高度
      ImageLeftBound  = 25;                       //小豬影像移動的左邊界
      ImageRightBound = AppletWidth - (25 + ImageWidth);        //右邊界 
      ImageX          = ImageRightBound;          //小豬影像的起始位置 
 
      VX              = -2;      //影像移動的速度
      this.Game       = Game;
      currentImage    = Normal;  //指定目前影像為小豬影像1
      showPressEnter  = true;    //顯示PressEnter字串
   }

   public void UpdateStatus()       //更新動畫狀態的函式
   {
      ImageX = ImageX + VX;         //指定影像的新位置

      if(ImageX <= ImageLeftBound)  //如果碰到左邊界的話 
      {
         currentImage = Hit;        //指定目前影像為小豬影像2

         ImageX = ImageLeftBound;   //設定影像的新位置
         VX     = -VX;              //改變影像移動方向
      }

      if(ImageX >= ImageRightBound) //如果碰到右邊界的話
      {
         currentImage = Normal;     //指定目前影像為小豬影像1

         ImageX = ImageRightBound;  //設定影像的新位置
         VX     = -VX;              //改變影像移動方向
      }
   }

   public void paintScreen(Graphics g)   //繪製動畫的函式
   {
      g.setFont(F1);                     //設定字型
      FM = g.getFontMetrics();

      Ascent       = FM.getAscent();
      Descent      = FM.getDescent();
      StringWidth  = FM.stringWidth(ChineseTitle);
      StringHeight = Ascent + Descent; 
      
      X            = (width - StringWidth) / 2;
      Y            = Ascent;

      g.drawImage(bkImage, 0, 0, Game);
      g.setColor(Color.white);           //設定前景顏色為白色
      g.drawString(ChineseTitle,X,Y);    //繪製中文標題

      Y            = StringHeight;
      g.drawLine(X,Y,X+StringWidth,Y);   //繪製線段1

      X            = X + 30;
      Y            = Y + 5;
      g.drawLine(X,Y,X+StringWidth-60,Y);//繪製線段2

      //===============================================================
      g.setFont(F2);                     //設定字型
      FM = g.getFontMetrics();

      Ascent       = FM.getAscent();
      Descent      = FM.getDescent();
      StringWidth  = FM.stringWidth(EnglishTitle);
      StringHeight = Ascent + Descent; 

      X            = (width - StringWidth) / 2;
      Y            = Y + Ascent;
      g.drawString(EnglishTitle,X,Y);    //繪製英文標題

      //===============================================================
      ImageY       = Y + Descent + 30; 
      g.drawImage(currentImage,ImageX,ImageY,Game);   //繪製小豬影像
   }
}

class CloseDialog extends Dialog implements ActionListener
{
   Panel        P1,P2;
   Button       B1,B2;
   HitPigHeadv2 Game;

   public CloseDialog(HitPigHeadv2 Game,Frame owner)
   {
      super(owner,"離開遊戲...",true);     //呼叫父類別的建構函式

      this.Game = Game;
  
      setLayout(new GridLayout(2,1));      //使用GridLayout
      P1 = new Panel();                    //建立Panel元件
      P2 = new Panel();
      
      P1.add(new Label("確定要離開嗎???"));//在P1中加入標籤
      P2.add(B1 = new Button("確定"));     //在P2中加入B1與B2
      P2.add(B2 = new Button("取消"));
      B1.addActionListener(this);          //註冊B1與B2的事件處理函式
      B2.addActionListener(this);

      add(P1);                             //加入P1與P2到CloseDialog中
      add(P2);
      pack();                              //包裝成最適當大小                
   }

   public void actionPerformed(ActionEvent e)
   {
      if(e.getSource() == B1)              //按下確定鈕
         Game.endGame(true);
      else if(e.getSource() == B2)         //按下取消鈕
         Game.endGame(false);

      hide();                              //隱藏視窗
   }
}