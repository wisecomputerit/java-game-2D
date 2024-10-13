// 程式：設定圖形深度
// 範例檔：ShowDepth.java

import java.awt.*;
import java.applet.*;

public class ShowDepth extends Applet
{
   public void init()
   {
       setBackground(Color.red);  //設定背景顏色
   }

   public void paint(Graphics g)
   {
       g.setColor(Color.black);     //設定長方形陰影顏色
       g.fillRect(50, 50, 200, 90); //繪製長方形陰影
       g.setColor(Color.white);     //設定長方形填滿顏色
       g.fillRect(45, 45, 190, 80); //繪製填滿長方形
       g.setColor(Color.black);     //設定長方形外框
       g.drawRect(54, 54, 170, 60); //繪製長方形外框
       g.setColor(Color.black);     //設定文字陰影
       g.setFont(new Font("Courier", Font.BOLD, 28));  //設定字型
       g.drawString("Game Over", 60, 100);  //繪製文字陰影
       g.setColor(Color.green);     //設定文字顏色
       g.drawString("Game Over", 58, 98);  //繪製文字
   }
}