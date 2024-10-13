// 程式：使用影像追蹤
// 範例檔：UseMediaTracker.java

import java.net.*;
import java.awt.*;
import java.applet.*;

public class UseMediaTracker extends Applet
{
   int          AppletWidth,AppletHeight,Ascent,Descent,StringWidth,X,Y;
   Image        Bg,Ship1,Ship2,Plane1,Plane2;
   MediaTracker MT;

   public void init()
   {
     AppletWidth  = getSize().width;   //取得Applet的顯示寬度
     AppletHeight = getSize().height;  //取得Applet的顯示高度

      //指定必須取得的影像
      Bg     = getImage(getDocumentBase(),"Images/1.gif");
      Ship1  = getImage(getDocumentBase(),"Images/3.gif");
      Ship2  = getImage(getDocumentBase(),"Images/4.gif");
      Plane1 = getImage(getDocumentBase(),"Images/5.gif");
      Plane2 = getImage(getDocumentBase(),"Images/6.gif");

      MT = new MediaTracker(this);     //建立MediaTracker類別實體
  
      //將必須取得的影像加入追蹤清單中
      MT.addImage(Bg    ,0);
      MT.addImage(Ship1 ,0);
      MT.addImage(Ship2 ,0);
      MT.addImage(Plane1,0);
      MT.addImage(Plane2,0);

      try
      {        
         showStatus("影像載入中...");  //在狀態列顯示資訊
         MT.waitForAll();              //等待所有影像下載
      }
      catch(InterruptedException E){ }    
   }

   public void paint(Graphics g)
   {
      //如果影像下載發生任何錯誤的話...
      if((MT.statusAll(false) & MediaTracker.ERRORED)  != 0)
      {
         FontMetrics FM = g.getFontMetrics();

         Ascent      = FM.getAscent();
         Descent     = FM.getDescent();
         StringWidth = FM.stringWidth("載入影像發生錯誤...");

         X = (AppletWidth - StringWidth) / 2;
         Y = (AppletHeight - (Ascent + Descent)) / 2 + Ascent;

         setBackground(Color.black);               //背景設為黑色
         g.setColor(Color.white);                  //字串設為白色 
         g.drawString("載入影像發生錯誤...",X,Y);  //置中顯示字串

         return;                                   //結束函式執行
      }   
     
      //如過正確下載所有影像的話則將所有影像繪製在Applet中
      g.drawImage(Bg    ,0,0,320,240,this);
      g.drawImage(Ship1 ,20,140,80,80,this);
      g.drawImage(Ship2 ,180,140,80,80,this);
      g.drawImage(Plane1,30,10,100,100,this);
      g.drawImage(Plane2,250,50,50,50,this);
   }
}