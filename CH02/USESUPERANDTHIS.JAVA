// 程式：使用super和this關鍵字
// 範例檔：UseSuperAndThis.java

class SuperClass        //父類別
{
   public SuperClass()  //建構方法
   {
      System.out.println("SuperClass...");
   }

   public int addNumber(int i,int j)  //公開方法成員
   {
      return i+j;    
   }
}

class ChildClass extends SuperClass   //子類別
{
   private int i,j,k;          //資料成員           

   public ChildClass() //建構方法1
   {
      super();         //呼叫父類別建構方法
   }

   public ChildClass(int i,int j)     //建構方法2
   {
      this();          //呼叫子類別建構方法
      i = i;      //將參數i指定給類別本身的資料成員i
      j = j;      //將參數j指定給類別本身的資料成員j
   }

   public void addNumber()
   {
      k = super.addNumber(i,j);      /*呼叫父類別中的addNumber()方法，傳入的i為this.i、j為
                                       this.j*/

      System.out.println("k=" + k);  //列印i+j的結果
   }
}

public class UseSuperAndThis
{
   public static void main(String args[])
   {
      ChildClass C1,C2;              

      C1 = new ChildClass();         //建立類別實體
      C2 = new ChildClass(5,10);
      C1.addNumber();                //呼叫方法
      C2.addNumber();                
   }
}