// 程式：清除連線執行緒
// 範例檔：CleanDeadConnect.java

class CleanDeadConnect extends Thread  //清除中斷連線的執行緒
{
   ServerThread sThread;
   ClientThread temp;
   String       m;

   public CleanDeadConnect(ServerThread sThread)
   {
      this.sThread = sThread;
   }

   public void run()
   {  
      while(true)
      {  
         try
         {
            Thread.sleep(10000);       //每10秒清除一次
         }
         catch(InterruptedException E){}

         synchronized(sThread.Clients) //同步處理
         {
            for(int i=0;i<sThread.Clients.size();i++)
            {
               temp = (ClientThread)sThread.Clients.elementAt(i);

               if(!temp.isAlive())                    //中斷的連線
               {
                  ClientThread.ConnectNumber--;       //連線數減1

                  m = temp.getClientName() + ":" + temp.getClientAddress();
                  m = m + "已離開 目前連線數: " + 
                      ClientThread.ConnectNumber + "\n";

                  sThread.frame.ServerStatus.append(m);
                  
                  synchronized(sThread.messages)      //同步處理
                  {
                     sThread.messages.addElement(m);  //廣播訊息
                  }

                  sThread.Clients.removeElementAt(i); //移除連線
               }   
            }
         }
      }
   }
}
