// 程式：建立類別實體
// 範例檔：UseConstructor.java

import java.lang.*;

public class UseConstructor
{
   UseConstructor()   //建構函式，注意其名稱必須和類別一致
   {
      System.out.println("呼叫建構函式...");
   }
   
   public static void main(String args[])  //主函式
   {
      new UseConstructor();                //建立類別實體的方法
      System.out.println("呼叫主函式...");
   }
}