// 程式：廣播執行緒
// 範例檔：BroadCast.java

class BroadCast extends Thread       //這是廣播執行緒
{
   ClientThread c;
   ServerThread sThread;
   String       m;

 
   public BroadCast(ServerThread sThread)
   {
      this.sThread    = sThread;
   }

   public void run()
   {
      while(true)
      { 
         try
         {
            Thread.sleep(3000);      //每3秒廣播一次
         }
         catch(InterruptedException E){}

         synchronized(sThread.messages)                   //同步處理
         {
            if(sThread.messages.isEmpty())                //如果無訊息
               continue;                                  //則繼續等待
           
            m = (String)sThread.messages.firstElement();  //取出第一個訊息
            sThread.messages.removeElement(m);            //移除第一個訊息
         }

         synchronized(sThread.Clients)                    //同步處理
         {
            //對每一個客戶端發送訊息
            for(int i=0;i<sThread.Clients.size();i++)
            {
               c = (ClientThread)sThread.Clients.elementAt(i);
               c.write.println(m);
            }
         }
      }
   }
}