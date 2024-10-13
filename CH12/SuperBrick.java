/*程式：SuperBrick.java
 *說明：以下為磚塊組資料類別，包含磚塊組陣列資料、磚塊組旋轉方式
 *      所有磚塊組以SuperBrick為基礎類別衍生各類別，其中各磚塊組
 *		類型旋轉還分二向及四向。
 */
 
//所有磚塊組類型的基礎類別
public class SuperBrick{
	//磚塊組資料
	public int BRICK_ARRAY[][][];//紀錄磚塊組資料陣列
	public SuperBrick(){
		//建立紀錄磚塊組的陣列並將資料清空
		BRICK_ARRAY=new int[4][5][5];
		for(int i=0;i<4;i++){
			for(int j=0;j<5;j++){
				for(int k=0;k<5;k++){
					BRICK_ARRAY[i][j][k]=0;
				}			
			}			
		}		
	}
	
	//旋轉磚塊組(順時針旋轉_只適用四個方向皆不同之磚塊組組)
	protected void turnBrick_Four(){
		
		for(int i=1;i<=3;i++){
			for(int x=0;x<=4;x++){
				for(int y=0;y<=4;y++){
					BRICK_ARRAY[i][x][y]=BRICK_ARRAY[i-1][4-y][x];
				}				
			}
		}
	}

	//旋轉磚塊組(順時針旋轉_只適用兩個方向之磚塊組組)
	protected void turnBrick_Two(){
		int COUNT=0;
		for(int i=1;i<=3;i++){			
			for(int x=0;x<=4;x++){
				for(int y=0;y<=4;y++){
					if(i%2==0){
						BRICK_ARRAY[i][x][y]=BRICK_ARRAY[i-1][4-y][x];
					}else{
						BRICK_ARRAY[i][x][y]=BRICK_ARRAY[i-1][y][4-x];
					}
				}	
			}
		}
	}	
	
	//設定磚塊組資料
	private void setBrickArray(){}
}

//磚塊組類別_田字型
class Brick_BB extends SuperBrick{
	//建構子
	public Brick_BB(){
		super();
		setBrickArray();//設定磚塊組資料		
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		for(int i=0;i<=3;i++){
			BRICK_ARRAY[i][3][1]=1;//田字型
			BRICK_ARRAY[i][3][2]=1;
			BRICK_ARRAY[i][2][1]=1;
			BRICK_ARRAY[i][2][2]=1;		
		}
	}	
}
	
//磚塊組類別_倒梯形
class Brick_UT extends SuperBrick{
	
	//建構子
	public Brick_UT(){
		super();
		setBrickArray();//設定磚塊組資料		
		turnBrick_Four();//旋轉磚塊組
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		BRICK_ARRAY[0][2][1]=1;//倒梯形
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//磚塊組類別_Ｌ形
class Brick_L extends SuperBrick{
	//建構子
	public Brick_L(){
		super();
		setBrickArray();//設定磚塊組資料		
		turnBrick_Four();//旋轉磚塊組
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		BRICK_ARRAY[0][1][1]=1;//Ｌ形
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//磚塊組類別_倒Ｌ形
class Brick_UL extends SuperBrick{
	//建構子
	public Brick_UL(){
		super();
		setBrickArray();//設定磚塊組資料		
		turnBrick_Four();//旋轉磚塊組
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		BRICK_ARRAY[0][3][1]=1;//倒Ｌ形
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//磚塊組類別_一字形
class Brick_I extends SuperBrick{
	//建構子
	public Brick_I(){
		super();
		setBrickArray();//設定磚塊組資料		
		turnBrick_Two();//旋轉磚塊組
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		BRICK_ARRAY[0][1][2]=1;//一字形
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;
		BRICK_ARRAY[0][4][2]=1;		
	}
}

//磚塊組類別_Ｚ形
class Brick_Z extends SuperBrick{
	//建構子
	public Brick_Z(){
		super();
		setBrickArray();//設定磚塊組資料		
		turnBrick_Two();//旋轉磚塊組
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		BRICK_ARRAY[0][1][1]=1;//Ｚ形
		BRICK_ARRAY[0][2][1]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//磚塊組類別_倒Ｚ形
class Brick_UZ extends SuperBrick{
	//建構子
	public Brick_UZ(){
		super();
		setBrickArray();//設定磚塊組資料		
		turnBrick_Two();//旋轉磚塊組
	}
	
	//設定磚塊組資料
	private void setBrickArray(){
		BRICK_ARRAY[0][2][1]=1;//倒Ｚ形
		BRICK_ARRAY[0][3][1]=1;
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;		
	}
}