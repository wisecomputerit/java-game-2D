/*程式：SingleMine.java
 *說明：踩地雷遊戲主要控制類別
 */
 
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.swing.*;

class SingleMine implements MouseListener,
							MouseMotionListener{
	
	int GRIDX;  //地圖x軸格數
	int GRIDY;  //地圖y軸格數
	int MINES;  //地雷數
	int MMINES; //已標示地雷數
	int TIME_COUNT;//時間計數
	Container c;//容器
	int MAP[][];//地雷佈置情形;0：空白、1-8：週邊地雷數、9：地雷
	int FACE[][];//地雷表面情形;0：已探測、1：未探測、2：插棋子、3：疑問
	
	Image offI;
	Graphics offG;
	MDraw md;
	Timer timer;//計時器
	
	int MOUSE_POSITION;//滑鼠位置;0:笑臉,1:地圖,2:其他
	int MOUSE_PRESSED_START;//滑鼠按下位置;0:笑臉,1:地圖,2:其他
	int MOUSE_X;//滑鼠相對應地雷區座標
	int MOUSE_Y;
	
	boolean MOUSE_RIGHT_PRESSED;//滑鼠右鍵按下
	boolean MOUSE_LEFT_PRESSED;//滑鼠左鍵按下
	boolean GAME_START;//遊戲開始
	boolean GAME_OVER;//遊戲結束
	boolean GAME_FINAL;//遊戲完成
	boolean UNDO;//左右鍵不動作;
	
	int GAME_RUN;//遊戲是否執行 [true:進行中],[false:停止]
	int GAME_STATE;//遊戲狀態 [1:成功],[0:一般],[-1:失敗]
	
	
	public SingleMine(int gridx,   //地圖x軸格數
					  int gridy,   //地圖y軸格數
					  int mines,   //地雷數
					  Container c){//容器
		
		//資料初始
		this.GRIDX=gridx;
		this.GRIDY=gridy;
		this.MINES=mines;
		this.MMINES=mines;
		this.c=c;
		
		//產生繪圖物件
		md=new MDraw(gridx,gridy,mines,c);
		
		//產生地雷佈置陣列及表面情形並初始陣列值
		MAP=new int[gridx][gridy];
		FACE=new int[gridx][gridy];
		
		//遊戲重置
		reset();
  		md.update();			
	}
	
	private class EventListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			TIME_COUNT++;
			md.showCount(md.COUNT_X,md.COUNT_Y,TIME_COUNT);	
		}	
	}	
	
	//以亂數佈置地雷位置
	public void setMinePosition(){
		int m=this.MINES;
		
		//當地雷未佈置完時持續佈置
		while(m>0){
			
			//亂數產生座標
			int x=(int)(Math.random()*GRIDX);	
			int y=(int)(Math.random()*GRIDY);
			
			//判斷產生的座標是否成立			
			if(x>=0 && x<GRIDX &&//防止超出陣列
			   y>=0 && y<GRIDY &&
			   MAP[x][y]!=9 && //陣列值不表示地雷
			   !(x==MOUSE_X && y==MOUSE_Y)){
			   	
				m--;//佈置地雷剩餘數
				MAP[x][y]=9;//地圖座標標示為地雷
				
				//將地雷周圍數字加一
				for(int i=(x-1);i<=(x+1);i++){
					for(int j=(y-1);j<=(y+1);j++){
						if(i>=0 && i<GRIDX &&//範圍在陣列內
						   j>=0 && j<GRIDY &&
						   MAP[i][j]!=9 &&//陣列值不表示地雷
						   !(i==x && j==y)){//不為地雷座標
						 	MAP[i][j]++;  	
						}   
					}						
				}	
			}
		}
	}
	
	//取得視窗大小
	public static Dimension getDimension(int x,int y){
		return new Dimension((x+2)*16+10,(y+6)*16+50);
	}
	
	//滑鼠按鍵
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		
		setMouseMapPosition(e);//設定滑鼠在地圖上座標			
		switch(e.getButton()){
		case MouseEvent.BUTTON1://左鍵
			UNDO=false;
			//紀錄左鍵狀態
			MOUSE_LEFT_PRESSED=true;
			
			//設定滑鼠左鍵按下的起始區域
			MOUSE_PRESSED_START=MOUSE_POSITION;
			
			//遊戲狀態
			switch(GAME_RUN){
			
			case 1://進行中
				//滑鼠所在區域
				switch(MOUSE_POSITION){
				case 0://笑臉區
					md.showSmile(1);//笑臉陷下
					break;
				case 1://地雷區
					if(MOUSE_PRESSED_START!=0){
						if(MOUSE_RIGHT_PRESSED){//右鍵按下時按下左鍵
							showMouseDownMap(MOUSE_X,MOUSE_Y);
						}else{//右鍵未按下時按下左鍵
							//UNDO=false;
						}
						if(FACE[MOUSE_X][MOUSE_Y]==1)
							md.showMap(MOUSE_X,MOUSE_Y,0);//空白
						md.showSmile(2);//笑臉張嘴	
					}				
					break;
				case 2://一般區
					md.showSmile(2);//笑臉張嘴
					break;
				}
				break;
				
			case 0://初始完成
				//滑鼠所在區域
				switch(MOUSE_POSITION){
				case 0://笑臉區
					md.showSmile(1);//笑臉陷下
					break;
				case 1://地雷區
					if(MOUSE_PRESSED_START!=0){
						if(MOUSE_RIGHT_PRESSED){//右鍵按下時按下左鍵
							showMouseDownMap(MOUSE_X,MOUSE_Y);
						}else{//右鍵未按下時按下左鍵
							//UNDO=false;
						}
						if(FACE[MOUSE_X][MOUSE_Y]==1)
							md.showMap(MOUSE_X,MOUSE_Y,0);//空白
						md.showSmile(2);//笑臉張嘴	
					}
					break;
				case 2://一般區
					md.showSmile(2);//笑臉張嘴
					break;
				}
				break;
				
			case -1://停止	
				switch(MOUSE_POSITION){
				case 0://笑臉區
					md.showSmile(1);//笑臉陷下
					break;
				}
				break;
			}
			break;	
			
		case MouseEvent.BUTTON3://右鍵
			UNDO=false;
			//紀錄右鍵狀態
			MOUSE_RIGHT_PRESSED=true;
			if(GAME_RUN!=-1 && MOUSE_PRESSED_START!=0){
				
				if(MOUSE_LEFT_PRESSED){//連同左鍵按下時
					reloadMap();
					for(int i=(MOUSE_X-1);i<=(MOUSE_X+1);i++){
						for(int j=(MOUSE_Y-1);j<=(MOUSE_Y+1);j++){
							if(i>=0 && i<GRIDX &&
							   j>=0 && j<GRIDY &&
							   FACE[i][j]==1){
								md.showMap(i,j,0);   	
							}
						}						
					}					
				}else{//只有右鍵按下時
					if(MOUSE_POSITION==1){
						if(FACE[MOUSE_X][MOUSE_Y]!=0){
							FACE[MOUSE_X][MOUSE_Y]++;
							if(FACE[MOUSE_X][MOUSE_Y]>=4){
								FACE[MOUSE_X][MOUSE_Y]=1;
							}	
							if(FACE[MOUSE_X][MOUSE_Y]==2)
								MMINES--;
							else if(FACE[MOUSE_X][MOUSE_Y]==3)
								MMINES++;
							md.showCount(md.MCOUNT_X,md.MCOUNT_Y,MMINES);
							reloadMap();
						}
					}
				}	
			}
			break;
		}
		md.update();
	}
	
	public void mouseReleased(MouseEvent e){
		
		setMouseMapPosition(e);//設定滑鼠在地圖上座標		
		switch(e.getButton()){
		case MouseEvent.BUTTON1://左鍵
			
			//紀錄左鍵狀態
			MOUSE_LEFT_PRESSED=false;
			
			//遊戲狀態
			switch(GAME_RUN){
			
			case 1://進行中
				//滑鼠所在區域
				switch(MOUSE_POSITION){
				case 0://笑臉區
					if(MOUSE_PRESSED_START==0){//在笑臉區起始(重置遊戲遊戲)
						reset();//遊戲重置
					}
					md.showSmile(0);//笑臉
					break;
				case 1://地雷區
					if(MOUSE_PRESSED_START!=0){//不在笑臉區起始

						if(!MOUSE_RIGHT_PRESSED && 
						   !UNDO && //右鍵未按下鬆開左鍵
						   FACE[MOUSE_X][MOUSE_Y]==1){//表示未探測
							
							FACE[MOUSE_X][MOUSE_Y]=0;
							switch(MAP[MOUSE_X][MOUSE_Y]){
							case 0://空白
								doClear(MOUSE_X,MOUSE_Y);//清除空白
								checkFinal();//檢查是否完成
								break;	
							case 9://地雷(遊戲結束)
								onMine(true);
								break;
							default:
								checkFinal();//檢查是否完成
							}
							
						}else if(MOUSE_RIGHT_PRESSED){//右鍵按下鬆開左鍵
							UNDO=true;
							md.showSmile(0);//笑臉
							if(MOUSE_PRESSED_START!=0){
								checkAutoOpen();	
								checkFinal();
							}
						
						}else if(UNDO){
							md.showSmile(0);//笑臉		
						}else{
							md.showSmile(0);//笑臉
						}
						reloadMap();
					}					
					break;
				case 2://一般區
					md.showSmile(0);//笑臉
					break;
				}
				break;
				
			case 0://初始完成
				//滑鼠所在區域
				switch(MOUSE_POSITION){
				case 0://笑臉區
					if(MOUSE_PRESSED_START==0)
						reset();
					md.showSmile(0);//笑臉
					break;
				case 1://地雷區
					if(MOUSE_PRESSED_START!=0 ){//不是在笑臉區起始(開始遊戲)
						if(!MOUSE_RIGHT_PRESSED && //右鍵未按下鬆開左鍵
						   !UNDO && 
						   FACE[MOUSE_X][MOUSE_Y]==1){
							setMinePosition();//設定地雷位置
							GAME_RUN=1;//遊戲開始
							
							//啟動計時器
							timer=new Timer(1000,new EventListener());
							timer.start();
	
							FACE[MOUSE_X][MOUSE_Y]=0;//表示已探測
							switch(MAP[MOUSE_X][MOUSE_Y]){
							case 0://空白
								doClear(MOUSE_X,MOUSE_Y);
								break;						
							}
							md.showSmile(0);//笑臉
							checkFinal();//檢查是否完成
							reloadMap();//重新顯示地雷區狀況
						
						}else if(MOUSE_RIGHT_PRESSED){//右鍵按下鬆開左鍵
							UNDO=true;
							reloadMap();//重新顯示地雷區狀況
						}else if(UNDO){
							md.showSmile(0);//笑臉
						}else{
							md.showSmile(0);//笑臉	
						}

					}else{
						md.showSmile(0);//笑臉	
					}
					
					break;
				case 2://一般區
					md.showSmile(0);//笑臉
					break;
				}
				break;
				
			case -1://停止	
				switch(MOUSE_POSITION){
				case 0://笑臉區
					if(MOUSE_PRESSED_START==0){
						reset();//遊戲重置	
					}
					break;
				}
				break;
			}
			break;	
			
		case MouseEvent.BUTTON3://右鍵
			//紀錄右鍵狀態
			MOUSE_RIGHT_PRESSED=false;
			switch(GAME_RUN){
			case 1://進行中
				//break;	
			case 0://初始
				if(MOUSE_PRESSED_START!=0){
					if(!MOUSE_LEFT_PRESSED && !UNDO){//左鍵未按下鬆開右鍵
						md.showSmile(0);//笑臉
					}else if(MOUSE_LEFT_PRESSED){//左鍵按下鬆開右鍵
						UNDO=true;
						if(MOUSE_PRESSED_START!=0 && GAME_RUN==1){
							checkAutoOpen();	
							checkFinal();
						}						
					}else if(UNDO){
						md.showSmile(0);//笑臉	
					}			
					reloadMap();		
				}else{
					md.showSmile(0);//笑臉	
				}
				break;				
			case -1://停止
				break;								
			}
			break;
		}
		md.update();
		MOUSE_PRESSED_START=2;		
	}
	
	//滑鼠事件
	public void mouseMoved(MouseEvent e){
		setMousePosition(e);//設定滑鼠所在區域
	}
	
	public void mouseDragged(MouseEvent e){

		//設定滑鼠所在區域
		setMousePosition(e);
		setMouseMapPosition(e);
		
		//依遊戲狀態
		switch(GAME_RUN){
		case 1://進行中
		case 0://初始完成
			if(!UNDO){
				if(MOUSE_PRESSED_START!=0)
					showMouseDownMap(MOUSE_X,MOUSE_Y);
			}
			
			if(MOUSE_PRESSED_START==0){//起始在笑臉區
				switch(MOUSE_POSITION){
				case 0://笑臉區
					md.showSmile(1);//笑臉陷下
					break;
				case 1://地雷區
				case 2://一般區
					md.showSmile(0);//笑臉							
					break;					
				}
			}
			break;
		case -1://停止
			if(MOUSE_LEFT_PRESSED && MOUSE_PRESSED_START==0){
				switch(MOUSE_POSITION){
				case 0://笑臉區
					md.showSmile(1);//笑臉陷下
					break;

				case 1://地雷區
				case 2://一般區
					switch(GAME_STATE){
					case 1://成功
						md.showSmile(3);//戴眼鏡
						break;	
					case 0://一般
						md.showSmile(0);//笑臉
						break;
					case -1://失敗
						md.showSmile(4);//哭臉
						break;
					}
					break;	
				}				
			}
			break;
		}
		md.update();

	}
	
	//設定滑鼠所在區域
	public void setMousePosition(MouseEvent e){

		//笑臉區
		if((e.getX())>=md.SMILE_X && 
		   (e.getX())<(md.SMILE_X+md.SMILE_WIDTH) &&
		   (e.getY())>=md.SMILE_Y && 
		   (e.getY())<(md.SMILE_Y+md.SMILE_HEIGHT)){
		   	MOUSE_POSITION=0;

		//地雷區
		}else if((e.getX())>=md.MAP_X && 
		   		 (e.getX())<(md.MAP_X+GRIDX*md.MAP_WIDTH) &&
		   		 (e.getY())>=md.MAP_Y && 
		   		 (e.getY())<(md.MAP_Y+GRIDY*md.MAP_HEIGHT)){
		   	MOUSE_POSITION=1;
		
		//其他區
		}else{
			MOUSE_POSITION=2;
		}
	
	}
	
	//設定滑鼠在地圖上座標
	public void setMouseMapPosition(MouseEvent e){
		MOUSE_X=(e.getX()-md.MAP_X)/md.MAP_WIDTH;
		MOUSE_Y=(e.getY()-md.MAP_Y)/md.MAP_HEIGHT;
	}
	
	//顯示滑鼠按下的地雷區
	public void showMouseDownMap(int x,int y){
		
		reloadMap();//更新地圖圖示

		if(MOUSE_LEFT_PRESSED && MOUSE_RIGHT_PRESSED){

			
			for(int i=(x-1);i<=(x+1);i++){
				for(int j=(y-1);j<=(y+1);j++){
					if(i>=0 && i<GRIDX &&//防止超出陣列
					   j>=0 && j<GRIDY &&
					   FACE[i][j]==1){		
						md.showMap(i,j,0);
					}
				}
			}				
			
		}else if(MOUSE_LEFT_PRESSED){
			if(x>=0 && x<GRIDX &&//防止超出陣列
			   y>=0 && y<GRIDY &&
			   FACE[x][y]==1){		
				md.showMap(x,y,0);
			}		
		}		
	}
	
	//更新地圖圖示
	public void reloadMap(){
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				if(FACE[i][j]==0){//已探測
					md.showMap(i,j,MAP[i][j]);
				}else{//未探測
					switch(FACE[i][j]){
					case 1://未探測
						md.showMap(i,j,12);
						break;
					case 2://插棋子
						md.showMap(i,j,13);
						break;
					case 3://問號
						md.showMap(i,j,14);
						break;							
					}
				}
			}					
		}		
	}	
	
	//設定地圖表面狀態
	public void setFace(int mode){
		if(MOUSE_X>=0 && MOUSE_X<GRIDX &&//防止超出陣列
		   MOUSE_Y>=0 && MOUSE_Y<GRIDY){
			FACE[MOUSE_X][MOUSE_Y]=mode;
		}
	}
	
	//清除空白區
	public void doClear(int x,int y){
		for(int i=(x-1);i<=(x+1);i++){
			for(int j=(y-1);j<=(y+1);j++){
				if(i>=0 && i<GRIDX &&
				   j>=0 && j<GRIDY &&	
				   FACE[i][j]==1 && //表示未探測
				   !(i==x && j==y)){
					switch(MAP[i][j]){
					case 0:
						FACE[i][j]=0;
						doClear(i,j);
						break;	
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						FACE[i][j]=0;
						break;
					}		
				}
			}			
		}	
	}
	
	//滑鼠兩件按下時鬆開任一鍵檢查自動翻開
	public void checkAutoOpen(){
		int x=MOUSE_X;
		int y=MOUSE_Y;
		int n=MAP[x][y];
		for(int i=(x-1);i<=(x+1);i++){
			for(int j=(y-1);j<=(y+1);j++){
				if(i>=0 && i<GRIDX &&
				   j>=0 && j<GRIDY &&
				   FACE[i][j]==2)
					n--;
			}			
		}
		if(n==0){
			for(int i=(x-1);i<=(x+1);i++){
				for(int j=(y-1);j<=(y+1);j++){
					if(i>=0 && i<GRIDX &&
					   j>=0 && j<GRIDY &&
					   FACE[i][j]==1 && 
					   !GAME_OVER){
						FACE[i][j]=0;
						if(MAP[i][j]==0)
							doClear(i,j);
						else if(MAP[i][j]==9)
							onMine(false);
					}
				}			
			}			
		}
	}
	
	//踩到地雷
	public void onMine(boolean right){
		if(right)//是否在正上方
			MAP[MOUSE_X][MOUSE_Y]=11;//地雷加紅邊
			
		GAME_RUN=-1;//遊戲停止
		GAME_STATE=-1;//失敗
		
		if(timer!=null){
			timer.stop();							
			timer=null;								
		}
		
		for(int ii=0;ii<GRIDX;ii++){
			for(int jj=0;jj<GRIDY;jj++){
				if(FACE[ii][jj]==2 &&//插錯棋子
				   MAP[ii][jj]!=9){//非地雷
					MAP[ii][jj]=10;
					FACE[ii][jj]=0;		
				}
				if(FACE[ii][jj]==2 &&//插對棋子
				   MAP[ii][jj]==9)//地雷
				   	FACE[ii][jj]=0;
				if(FACE[ii][jj]==1 &&//未探索
				   MAP[ii][jj]==9)		
					FACE[ii][jj]=0;
			}								
		}	
		md.showSmile(4);//哭臉
	}
	
	//檢查是否完成
	public void checkFinal(){
		int m=MINES;
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				if(FACE[i][j]!=0)
					m--;
			}				
		}	
		
		if(m==0){
			for(int i=0;i<GRIDX;i++){
				for(int j=0;j<GRIDY;j++){
					if(MAP[i][j]==9)
						MAP[i][j]=13;
					FACE[i][j]=0;				
				}				
			}	

			GAME_RUN=-1;//遊戲停止
			GAME_STATE=1;//成功
			timer.stop();
			md.showSmile(3);//戴眼鏡
			md.showCount(md.MCOUNT_X,md.MCOUNT_Y,0);
		}else{
			if(GAME_RUN==1)
				md.showSmile(0);//笑臉
			else{
				if(GAME_STATE==-1)	
					md.showSmile(4);//哭臉
			}
		}
	}
	
	//重置遊戲
	public void reset(){
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				FACE[i][j]=1;//地雷表面設定(未探索)
				MAP[i][j]=0;//地雷設定(空白)
			}				
		}	
		
		GAME_RUN=0;  //遊戲初始完成
		GAME_STATE=0;//一般狀態	
		MOUSE_LEFT_PRESSED=false;
		MOUSE_RIGHT_PRESSED=false;
		UNDO=false;
		
		//計時器重置
		if(timer!=null)
			timer.stop();
		TIME_COUNT=0;//時間計數設定
		MMINES=MINES;//標記地雷數設定		
		reloadMap();
		md.showSmile(0);
		md.showCount(md.MCOUNT_X,md.MCOUNT_Y,MINES);
		md.showCount(md.COUNT_X,md.COUNT_Y,TIME_COUNT);
	}
}
