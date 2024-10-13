// 程式：方法重新定義
// 範例檔：Overriding.java

class SuperClass               //父類別
{
   private void Method_1()     //私有函式成員，無法被繼承和修改
   {
      System.out.println("SuperClass.Method_1()");
   }

   public void Method_2()      //公開函式成員，可以被繼承和修改
   {
      Method_1();
   }
}

class ChildClass extends SuperClass  //子類別
{
   public void Method_2()     //Overriding父類別的Method_2()函式
   {
      System.out.println("ChildClass.Method_2()");
   }
}

public class Overriding
{
   public static void main(String args[])
   {
      //建立類別實體
      SuperClass S = new SuperClass();
      ChildClass C = new ChildClass();

      S.Method_2();   //呼叫SuperClass的Method_3()
      C.Method_2();   //呼叫ChildClass的Method_2()
   }
}