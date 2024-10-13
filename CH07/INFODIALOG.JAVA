// 程式：對話方塊
// 範例檔：InfoDialog.java

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class InfoDialog extends Dialog implements ActionListener
{
   Button B;

   public InfoDialog(Frame Owner,String Title,boolean isModal)
   {
      super(Owner,Title,isModal);       

      setLayout(new GridLayout(4,1));          //指定LayoutManager

      add(new Label("棒打豬頭1.0中文版"));     //加入元件
      add(new Label("小豬頭工作室製作發行"));
      add(new Label("2001/11/15"));
      add(B = new Button("確定"));

      B.addActionListener(this);               //註冊按鈕的事件處理函式

      pack();                                  //包裝對話視窗
   }

   //=====實作ActionListener介面=======================================
   public void actionPerformed(ActionEvent e)
   {
      hide();                                  //隱藏視窗
   }
}