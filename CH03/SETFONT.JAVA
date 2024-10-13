// 程式：設定字型
// 範例檔：SetFont.java

import java.awt.*;
import java.applet.*;

public class SetFont extends Applet {
   Font F;

   public void paint(Graphics g)
   {
      F = new Font("細明體",Font.BOLD+Font.ITALIC,36);  //粗斜體
      g.setFont(F);
      g.drawString("字型測試一...",10,30);

      F = new Font("標楷體",Font.PLAIN,18);             //一般樣式
      g.setFont(F);
      g.drawString("字型測試二...",10,60);

      F = new Font("新細明體",Font.BOLD,9);             //粗體
      g.setFont(F);
      g.drawString("字型測試三...",10,90);
   }
}