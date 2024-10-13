// 程式：置中字串
// 範例檔：CenterString.java

import java.awt.*;
import java.applet.*;

public class CenterString extends Applet {
   FontMetrics FM;
   int Ascent, Descent, Width, 
       AppletWidth, AppletHeight, X, Y;

   public void init()
   {
      AppletWidth  = getSize().width;   //取得顯示區域的寬度
      AppletHeight = getSize().height;  //取得顯示區域的高度
   }

   public void paint(Graphics g)
   {
      g.setFont(new Font("Courier", Font.BOLD, 32));  //設定字型

      FM = g.getFontMetrics();     //取得FontMetrics類別實體

      Ascent = FM.getAscent();               //取得Ascent
      Descent = FM.getDescent();              //取得Descent
      Width = FM.stringWidth("Game Over");  //取得字串寬度

      //計算置中座標
      X = (AppletWidth - Width) / 2;
      Y = (AppletHeight - (Ascent + Descent)) / 2 + Ascent;
          
      g.drawString("Game Over", X, Y);   //繪製字串陰影
      g.setColor(Color.green);
      g.drawString("Game Over", X-2, Y-2);  //繪製字串前景
   }
}