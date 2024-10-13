// 程式：使用static關鍵字
// 範例檔：UseStaticMember.java

class StaticClass
{
   public static int i;                     //static資料成員

   public static int addNumber(int i,int j) //static方法成員
   {
      return i+j;
   }
}

public class UseStaticMember
{
   public static void main(String args[])
   {
      StaticClass S1 = new StaticClass();   //建立類別實體
      S1.i = S1.addNumber(10,20);           //呼叫static方法 
      System.out.println("10+20=" + S1.i);  //存取static成員
   
      //另一種使用static成員的方法，類別名稱加成員名稱
      StaticClass.i = StaticClass.addNumber(30,40);
      System.out.println("30+40=" + StaticClass.i);     

      StaticClass S2 = new StaticClass();   //再建立類別實體
      System.out.println("StaticClass.i=" + S2.i);  //猜看看結果是...
   }
}