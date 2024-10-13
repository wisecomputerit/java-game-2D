// 程式：使用CardLayout版面配置
// 範例檔：UseCardLayout.java

import java.awt.*;
import java.applet.*;

public class UseCardLayout extends Applet
{
   public void init()
   {
      CardLayout CL = new CardLayout();
      setLayout(CL);       //指定使用CardLayout
      
      for(int i=0;i<5;i++)
         add(new Button("按鈕" + i),"button" + i); //加入按鈕

      CL.show(this,"button4");
      CL.next(this);
   } 
}