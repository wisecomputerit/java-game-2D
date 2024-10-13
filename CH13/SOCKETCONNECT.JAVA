// 程式：取得網頁的首頁資料
// 範例檔：SocketConnect.java

import java.net.*;
import java.io.*;

public class SocketConnect {
    public static void main(String args[])
    {
        try {

            String data, host;
            int port = 80;  // HTTP伺服器預設連接埠
            PrintStream theOutputStream;

            host = args[0];
            Socket skt = new Socket(host, port);

            theOutputStream = new PrintStream(skt.getOutputStream());
            // 送出HTTP指令，此指令可取得首頁資料
            theOutputStream.println("GET /");  

            BufferedReader buf = new 
                BufferedReader(new InputStreamReader(skt.getInputStream()));

            // 取得連線資料
            while((data = buf.readLine()) != null)
                System.out.println(data);

            skt.close(); // 關閉Socket
        }
        catch (UnknownHostException e) {  // 無法解析位址
            System.out.println(e.getMessage());
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("沒有指定位址");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
