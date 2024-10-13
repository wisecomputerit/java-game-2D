// 程式：實作介面
// 範例檔：ImplementInterface.java

interface SuperInterface  //父介面
{
   int a = 5;             //指定了兩個整數資料成員
   int b = 10;            //別忘了其特性為public、final、static
}

interface ChildInterface extends SuperInterface  //子介面
{
   //子介面只定義一個抽象方法，但自父介面繼承了a和b資料成員
   int addNumber(int i,int j);  
}

//此類別實作介面因此必須overriding抽象方法，但可以直接使用介面資料成員
public class ImplementInterface implements ChildInterface
{
   static int total;      //此資料成員宣告為static，為什麼？

   public int addNumber(int i,int j)  
   {
      return i+j;
   }

   public static void main(String args[])
   {
      ImplementInterface I = new ImplementInterface();
      total = I.addNumber(a,b);  //將父介面中的資料成員相加

      System.out.println("a+b=" + total);
   }
}