// 程式：聊天程式介面
// 範例檔：ChatOneToOne.java

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ChatOneToOne extends Frame
{
    Button clientBtn, serverBtn;
    TextArea ta;
    TextField tfaddress, tfport, tftype;
    int port;
    clientskt client;  // 客戶端連線處理執行緒
    serverskt server;  // 伺服端連線處理執行緒
    boolean iamserver; // 旗標，是否成為伺服端
    static ChatOneToOne frm;

    public ChatOneToOne()
    {
        clientBtn = new Button("客戶端");
        serverBtn = new Button("伺服端");
        ta = new TextArea("", 10, 50, TextArea.SCROLLBARS_BOTH);
        tfaddress = new TextField("在此輸入IP位址");  // IP欄位
        tfport = new TextField("連接埠");  // 連接埠欄位
        tftype = new TextField(50);   // 文字輸入欄位

        tftype.addKeyListener(new TFListener()); // 註冊事件
        ta.setEditable(false);

        setLayout(new FlowLayout());  // 設定版面配置
        add(tfaddress);
        add(tfport);
        add(clientBtn);
        add(serverBtn);
        add(ta); 
        add(tftype);
        setSize(400, 300);
        setTitle("一對一聊天程式");

        // 按下「客戶端」按鈕的事件處理
        clientBtn.addActionListener(
            new ActionListener() {  // 匿名類別
                public void actionPerformed(ActionEvent e) {
                    // 取得指定的位址與連接埠
                    port = Integer.parseInt(tfport.getText());
                    // 建立客戶端連線執行緒
                    client = new clientskt(tfaddress.getText(), port, frm);
                    // 啟動執行緒進行連線
                    client.start();
                    // 設定按鈕與上面文字欄位失效
                    tfaddress.setEnabled(false);
                    tfport.setEnabled(false);
                    serverBtn.setEnabled(false);
                    clientBtn.setEnabled(false);
                }
            }
        );

        // 按下「伺服端」按鈕的事件處理
        serverBtn.addActionListener(
            new ActionListener() {  // 匿名類別
                public void actionPerformed(ActionEvent e) {
                    // 取得指定的連接埠
                    port = Integer.parseInt(tfport.getText());
                    // 建立伺服端連線執行緒
                    server = new serverskt(port, frm);
                    // 啟動執行緒傾聽連線
                    server.start();
                    // 標示成為伺服端
                    iamserver = true;
                    tfaddress.setText("成為伺服端");
                    // 設定按鈕與上面文字欄位失效
                    tfaddress.setEnabled(false);
                    tfport.setEnabled(false);
                    serverBtn.setEnabled(false);
                    clientBtn.setEnabled(false);
                }
            }
        );

        addWindowListener(  // 按下關閉鈕時結束程式
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
        );
        show();
    }

    public static void main(String args[])
    {
        frm = new ChatOneToOne();
    }

    // 下方輸入欄位的鍵盤事件處理
    private class TFListener implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {
            // 如果按下的是「Enter」鍵
            if(e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                // 在文字區域顯示輸入內容
                ta.append(">" + tftype.getText() + "\n");
                // 將資料透過連線執行緒送出
                if(iamserver)
                    server.dataout(tftype.getText());
                else
                    client.dataout(tftype.getText());
                // 清除下方文字欄位內容
                tftype.setText("");
            }
        }

        public void keyTyped(KeyEvent e) {}

        public void keyReleased(KeyEvent e) {}
    }
}
