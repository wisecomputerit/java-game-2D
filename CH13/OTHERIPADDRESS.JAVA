// 程式：取得其它電腦位址
// 範例檔：OtherIPAddress.java

import java.net.*;     //為了使用InetAddress類別
import java.awt.*;
import java.awt.event.*;

public class OtherIPAddress extends Frame
{
   InetAddress Computer;
   String      IP,Name;
   TextField tf;
   Button btn;
   String input;
  
   public OtherIPAddress()
   {
      tf = new TextField("輸入網路位址",10);
      btn = new Button("查詢");

      // 以內部匿名類別處理事件
      btn.addActionListener(
          new ActionListener() {
              public void actionPerformed(ActionEvent e)
              {
                  input = tf.getText();
                  try
                  {
                  //取得主機位置實體
                      Computer = InetAddress.getByName(input);

                      IP = Computer.getHostAddress();    //主機位置
                      Name = Computer.getHostName();   //主機名稱
                  }
                  catch(UnknownHostException E){
                      tf.setText(E.toString());
                  }
              }
          }
      );

      // 以內部匿名類別處理事件，可直接關閉視窗
      addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
      );      

      setLayout(new FlowLayout());
      setSize(250, 200);
      setTitle("取得其它電腦位址");
      add(tf);
      add(btn);
      show();
   }

   public static void main(String args[])
   {
       OtherIPAddress frm = new OtherIPAddress();
   }

   public void paint(Graphics g)
   {
      g.drawString("主機位置:" + IP,10,80);
      g.drawString("主機名稱:" + Name,10,100);
   }
}