// 程式：使用GridLayout版面配置
// 範例檔：UseGridLayout.java

import java.awt.*;
import java.applet.*;

public class UseGridLayout extends Applet
{
   public void init()
   {
      setLayout(new GridLayout(3,3));    //指定使用GridLayout
      
      for(int i=0;i<9;i++)
         add(new Button("按鈕" + i));
   } 
}