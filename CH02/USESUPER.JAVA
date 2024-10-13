// 程式：使用Super關鍵字
// 範例檔：UseSuper.java

class SuperClass         //父類別
{
   SuperClass()          //建構函式1
   {
      System.out.println("I'm SuperClass...");
   }

   SuperClass(String s)  //建構函式2
   {
      System.out.println(s);
   }
}

class ChildClass extends SuperClass  //子類別
{
   ChildClass()          //建構函式1
   {
      //注意，此建構函式沒有任何程式碼
   }

   ChildClass(String s)  //建構函式2
   {
      super(s);
      System.out.println("I'm ChildClass...");
   }
}

public class UseSuper
{
   public static void main(String args[])
   {
      //建立子類別實體
      new ChildClass();
      new ChildClass("呼叫父類別建構函式...");
   }
}
