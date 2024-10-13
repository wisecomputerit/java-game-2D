// 程式：使用FlowLayout版面配置
// 範例檔：UseFlowLayout.java

import java.awt.*;
import java.applet.*;

public class UseFlowLayout extends Applet
{
   public void init()
   {
      setLayout(new FlowLayout());      //此行有點多餘
      
      for(int i=0;i<5;i++)            
         add(new Button("按鈕" + i));   //加入五個按鈕
   } 
}