// 程式：使用BorderLayout版面配置
// 範例檔：UseBorderLayout.java

import java.awt.*;
import java.applet.*;

public class UseBorderLayout extends Applet
{
   public void init()
   {
      setLayout(new BorderLayout());    //指定使用BorderLayout
      
      add(new Button("按鈕1"),BorderLayout.NORTH);  //在上方加入按鈕
      add(new Button("按鈕2"),BorderLayout.SOUTH);  //在下方加入按鈕
      add(new Button("按鈕3"),BorderLayout.WEST);   //在左方加入按鈕
      add(new Button("按鈕4"),BorderLayout.EAST);   //在右方加入按鈕
      add(new Button("按鈕5"),BorderLayout.CENTER); //在中央加入按鈕
   } 
}