// 程式：使用紋理貼圖
// 範例檔：UseTexture.java

import java.awt.*;
import java.applet.*;

public class UseTexture extends Applet
{
   int          ImageWidth,ImageHeight,countX,countY; 
   Image        texture;
   MediaTracker MT;

   public void init()
   {
      countX  = 5;   //X軸使用的紋理數
      countY  = 5;   //Y軸使用的紋理數

      //取得紋理圖形
      texture = getImage(getDocumentBase(),"Images/texture.gif");
      MT      = new MediaTracker(this);
      MT.addImage(texture,0);
   
      try
      {
         MT.waitForAll();
      }      
      catch(InterruptedException E){ }        //沒有進行例外處理

      ImageWidth  = texture.getWidth(this);   //紋理寬度
      ImageHeight = texture.getHeight(this);  //紋理高度

      //重設Applet的大小
      resize(ImageWidth*countX,ImageHeight*countY);   
   }

   public void paint(Graphics g)
   {
      //使用紋理填滿背景
      for(int i=0;i<(countX * countY);i++)
      {
         g.drawImage(texture,i%countX*ImageWidth,i/countY*ImageHeight,
                     this);
      }
   }
}