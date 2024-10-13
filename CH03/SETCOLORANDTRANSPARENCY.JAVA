// 程式：設定顏色與透明度
// 範例檔：SetColorAndTransparency.java

import java.awt.*;       
import java.applet.*;  

public class SetColorAndTransparency extends Applet {
   Color C;

   public void init() {
      setBackground(Color.green);    //設定背景色為綠色
   }

   public void paint(Graphics g)    
   {
      C = new Color(255,255,255,255);  // 白色不透明
      g.setColor(C);                   //設定前景色
      g.drawString("歡迎來到Java網路遊戲的世界...",10,30);

      C = new Color(255,0,0,128);       // 紅色半透明
      g.setColor(C);                  // 設定前景色
      g.drawString("歡迎來到Java網路遊戲的世界...",10,60);
   } 
}