// 程式：AWT元件事件處理
// 範例檔：GameFrame2.java

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;

class GameFrame2 extends Frame 
      implements WindowListener,ActionListener
{
   Panel    main,top,center;                //main將包含top與center
   Menu     MU;                             //主選單選項
   MenuBar  MB;                             //選單列
   MenuItem MI1,MI2;                        //選單選項

   InfoDialog   DW;                         //對話視窗
   
   public GameFrame2(String Title,int AppletWidth,int AppletHeight,
                    Applet Game)
   {
      super(Title);                         //建立視窗
      main      = new Panel();              //建立Panel
      top       = new Panel();
      center    = new Panel();

      //建立對話視窗
      DW        = new InfoDialog(this,"基本資訊",true);

      addWindowListener(this);

      main.setLayout(new BorderLayout());   //main使用BorderLayout
      center.setLayout(new CardLayout());   //center使用CardLayout
                                            //top使用預設的FlowLayout

      main.add(top,BorderLayout.NORTH);     //將top置入main上方
      main.add(center,BorderLayout.CENTER); //將center置入main中央

      top.add(new Button("按鈕1"));         //在top中置入兩個按鈕
      top.add(new Button("按鈕2"));

      center.add(Game,"main");              //在center中置入Applet
      add(main);                            //在視窗中置入main

      //建立選單
      MB = new MenuBar();
      MU = new Menu("選項");
      MU.add(MI1 = new MenuItem("基本資訊"));
      MU.add(MI2 = new MenuItem("離開遊戲"));
      MB.add(MU);
      setMenuBar(MB);                       //將選單加入視窗中

      MI1.addActionListener(this);          //替選項註冊事件處理函式
      MI2.addActionListener(this);
      
      setResizable(false);                     //不可改變視窗大小
      setSize(AppletWidth,AppletHeight + 100); //為何高度要加100
      show();                                  //顯示視窗             
   }

   //=====實作WindowListener介面=======================================
   public void windowClosing(WindowEvent e)
   { 
      dispose();                               //棄置視窗
   }

   public void windowOpened(WindowEvent e){}
   public void windowClosed(WindowEvent e){}
   public void windowIconified(WindowEvent e){}
   public void windowActivated(WindowEvent e){}
   public void windowDeactivated(WindowEvent e){}
   public void windowDeiconified(WindowEvent e){}

   //=====實作ActionListener介面=======================================
   public void actionPerformed(ActionEvent e)
   {
      if(e.getSource() == MI1)          //當選項1被按下時
      {
         DW.show();                     //顯示對話視窗
      }   
      else if(e.getSource() == MI2)     //當選項2被按下時
         dispose();                     //棄置視窗
   }
}