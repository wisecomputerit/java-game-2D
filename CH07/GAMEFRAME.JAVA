// 程式：跳脫瀏覽器的Applet
// 範例檔：GameFrame.java

import java.awt.*;
import java.applet.*;

class GameFrame
{
   Frame gameFrame;        //裝載Applet的視窗
   Panel main,top,center;  //main將包含top與center

   public GameFrame(String Title,int AppletWidth,int AppletHeight,
                    Applet Game)
   {
      gameFrame = new Frame(Title);         //建立視窗
      main      = new Panel();              //建立Panel
      top       = new Panel();
      center    = new Panel();

      main.setLayout(new BorderLayout());   //main使用BorderLayout
      center.setLayout(new CardLayout());   //center使用CardLayout
                                            //top使用預設的FlowLayout

      main.add(top,BorderLayout.NORTH);     //將top置入main上方
      main.add(center,BorderLayout.CENTER); //將center置入main中央

      top.add(new Button("按鈕1"));         //在top中置入兩個按鈕
      top.add(new Button("按鈕2"));

      center.add(Game,"main");              //在center中置入Applet

      gameFrame.add(main,BorderLayout.CENTER); //在視窗中置入main
      gameFrame.setResizable(false);        //不可改變視窗大小
      gameFrame.setSize(AppletWidth,AppletHeight + 100); //為何高度要加100
      gameFrame.show();                     //顯示視窗             
   }
}