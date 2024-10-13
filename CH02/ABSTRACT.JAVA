// 程式：抽象類別與方法
// 範例檔：Abstract.java

abstract class SuperClass           //抽象父類別
{
   abstract public void Method_1(); //抽象函式，以「;」代表函式主體

   protected void Method_2()        //保護函式成員，可以被繼承     
   {
      System.out.println("SuperClass.Method_2()");
   }
} 

class ChildClass extends SuperClass //子類別 
{
   public void Method_1()           //Overriding抽象函式
   {
      System.out.println("ChildClass.Method_1()");
   }
}

public class Abstract 
{
   public static void main(String args[])
   {
      ChildClass C = new ChildClass();  //建立類別實體
      C.Method_1();   //呼叫Method_1()  
      C.Method_2();   //呼叫Method_2()
   }
}