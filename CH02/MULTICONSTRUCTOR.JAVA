// 程式：多重建構方法
// 範例檔：MultiConstructor.java

public class MultiConstructor
{
   int a,b;   //資料成員        
 
   MultiConstructor()             //建構函式1
   {
      a=0;
      b=0;
   }

   MultiConstructor(int i)       //建構函式2
   {
      a=i;
      b=0;
   }

   MultiConstructor(int i,int j) //建構函式3
   {
      a=i;
      b=j;
   }
 
   public static void main(String args[])  //主函式
   {
      //建立類別實體
      MultiConstructor instanceA = new MultiConstructor();
      MultiConstructor instanceB = new MultiConstructor(1);
      MultiConstructor instanceC = new MultiConstructor(1,2);

      System.out.println("instanceA==>a=" + instanceA.a + " b=" + instanceA.b);
      System.out.println("instanceB==>a=" + instanceB.a + " b=" + instanceB.b);
      System.out.println("instanceC==>a=" + instanceC.a + " b=" + instanceC.b);
   }
}