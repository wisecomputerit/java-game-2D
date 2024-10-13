// 程式：鏈狀呼叫建構方法
// 範例檔：Constructor.java

class SuperClass extends Object
{
   SuperClass()
   {
      System.out.println("I'm SuperClass...");
   }
}

class ChildClass1 extends SuperClass
{
   ChildClass1()
   {
      System.out.println("I'm ChildClass1...");
   }
}

class ChildClass2 extends ChildClass1
{
   //不指定建構函式
}

public class Constructor
{
   public static void main(String args[])
   {
      new ChildClass2();
   }
}