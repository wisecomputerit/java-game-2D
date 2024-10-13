/*程式：SingleTetirs.java
 *說明：單一俄羅斯方塊遊戲類別
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

//單一磚塊組遊戲類別
public class SingleTetirs implements Runnable{
	
	Graphics g;//繪圖區
	Component c;
	Thread DownThread;
	long GAME_SPEED;//紀錄遊戲速度
	long GAME_SCORE;//紀錄遊戲分數
	long GAME_LEVEL;//紀錄遊戲等級
	long GAME_LINE;//紀錄遊戲消除行數
	boolean GAME_RUNNING;//紀錄遊戲是否進行中
	Draw d;//畫出磚塊方式
	
	int MAPX;//地圖X座標
	int MAPY;//地圖Y座標
	
	int MAP_ARRAY[][];//地圖陣列
	int MAP_SHOW_ARRAY[][];//地圖顯示陣列(與移動磚塊組結合之陣列)
	Color MAP_COLOR_ARRAY[][];//地圖顏色陣列
	
	int GRIDX;//x軸格數
	int GRIDY;//y軸格數

	int BRICK_WIDTH;//單一磚塊_寬	
	int BRICK_SIDE;//單一磚塊_邊
	
	int NEXT_BRICK_NUMBER;//下一個磚塊組顯示數量
	
	boolean IS_SHOW_NEXT;//是否顯示下一個磚塊組
	
	int NOW_BRICK;//現在動作磚塊組編號
	int NOW_BRICK_DIRECT;//現在動作磚塊組的方向
	int NOW_BRICK_X;//現在動作磚塊組的x座標
	int NOW_BRICK_Y;//現在動作磚塊組的y座標
	SuperBrick NowBrick;//現在動作的磚塊組
	Color NOW_BRICK_COLOR;//現在動作磚塊組的顏色
	
	ArrayList BrickArray;//所有磚塊組類別陣列
	ArrayList NextBrickArray;//下一個磚塊組儲存陣列
	ArrayList NextBrickColorArray;//下一個磚塊組儲存陣列
		
	public SingleTetirs(Graphics g,//繪圖區
					    int mapx,int mapy,//地圖座標
					    int gridx,int gridy,//地圖方格數
					    int brick_width,//磚塊組寬
					    int brick_side,//磚塊組邊
					    int next_brick_number,//顯示下一個磚塊組數量
					    boolean is_show_next,//是否顯示下一個磚塊組
					    Component c){//元件
		//初始各參數
		this.c=c;
		this.g=g;
		this.MAPX=mapx;
		this.MAPY=mapy;
		this.GRIDX=gridx;
		this.GRIDY=gridy;
		this.BRICK_WIDTH=brick_width;
		this.BRICK_SIDE=brick_side;
		this.NEXT_BRICK_NUMBER=next_brick_number;
		this.IS_SHOW_NEXT=is_show_next;
		this.MAP_ARRAY=new int[GRIDX][GRIDY];
		this.MAP_SHOW_ARRAY=new int[GRIDX][GRIDY];
		this.MAP_COLOR_ARRAY=new Color[GRIDX][GRIDY];
		this.NOW_BRICK_X=0;
		this.NOW_BRICK_Y=0;
		this.GAME_SPEED=1000;
		this.GAME_SCORE=0;
		this.GAME_LEVEL=1;
		this.GAME_LINE=0;
		
		//將地圖陣列歸零
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				this.MAP_ARRAY[i][j]=0;
				this.MAP_SHOW_ARRAY[i][j]=0;
			}			
		}
		
		//加入所有磚塊組類型
		BrickArray=new ArrayList();//建立儲存磚塊組型態的陣列
		BrickArray.add(new Brick_BB());
		BrickArray.add(new Brick_I());
		BrickArray.add(new Brick_L());
		BrickArray.add(new Brick_UL());
		BrickArray.add(new Brick_UT());
		BrickArray.add(new Brick_UZ());
		BrickArray.add(new Brick_Z());
		
		//產生下一磚塊組資料
		NextBrickArray=new ArrayList();//建立儲存預備磚塊組編號的陣列
		NextBrickColorArray=new ArrayList();//建立儲存預備磚塊組編號的陣列
		for(int i=0;i<(NEXT_BRICK_NUMBER+1);i++){
			createNextBrick();
		}

		//建立繪出磚塊組物件
		d=new Draw(this);
		
		//畫出外框
		d.DrawMapFrame(0,0,GRIDX*BRICK_WIDTH+2*5,GRIDY*BRICK_WIDTH+2*5,5);
		d.DrawMapFrame(290,0,200,350,5);//預備方塊外框
		d.DrawMapFrame(290,350,200,140,5);//遊戲數據外框
		
		showNextBrickGroup();//顯示提示方塊組
		addNextBrick();//新增一個提示方塊
		showNextBrick();//顯示現在動作方塊
		
		updateGame(0);//更新遊戲資料
		
		//產生控制下降的執行緒
		DownThread=new Thread(this);
		DownThread.start();
	}
	
	//重繪視窗
	public void paint(){
		//畫出外框
		d.DrawMapFrame(0,0,GRIDX*BRICK_WIDTH+2*5,GRIDY*BRICK_WIDTH+2*5,5);
		d.DrawMapFrame(290,0,200,350,5);//預備方塊
		d.DrawMapFrame(290,350,200,140,5);//遊戲數據	
		showNextBrickGroup();
		updateGame(0);
		reLoadMap();
		showNextBrick();
					
	}
	
	//覆蓋run()方法
	public void run(){
		try{
			Thread.sleep(GAME_SPEED);  	
			keyPress(KeyEvent.VK_DOWN);
			DownThread=new Thread(this);
			DownThread.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//更新遊戲資料
	public void updateGame(int line){
		d.DrawMapFrame(290,350,200,140,5);
		GAME_SCORE+=50*line*line*line*(0.5)*GAME_LEVEL;
		GAME_LEVEL=GAME_SCORE/5000+1;
		GAME_SPEED=800/GAME_LEVEL+200;
		GAME_LINE+=line;
		
		d.DrawFont("級數:"+GAME_LEVEL,300,400,16,Color.cyan,"標楷體");
		d.DrawFont("分數:"+GAME_SCORE,300,430,16,Color.red,"標楷體");		
		d.DrawFont("行數:"+GAME_LINE,300,460,16,Color.green,"標楷體");
	}	
		
	//產生下一組磚塊
	public void createNextBrick(){
		//產生預備磚塊組編號
		NextBrickArray.add(new Integer((int)(Math.random()*BrickArray.size())));
		//產生預備磚塊組顏色
		NextBrickColorArray.add(new Color((int)(Math.random()*128+127),
										  (int)(Math.random()*128+127),
										  (int)(Math.random()*128+127)));		
	}
	
	//檢查磚塊組是否可以放置
	public boolean isPut(int x,int y,int type,int mode){
		SuperBrick sb=(SuperBrick)BrickArray.get(type);
		for(int i=0;i<=4;i++){
			for(int j=0;j<=4;j++){
				if(sb.BRICK_ARRAY[mode][i][j]==1){
					if((i+x)<0 || (i+x)>(GRIDX-1) || 
					   (j+y)<0 || (j+y)>(GRIDY-1) ||
					   MAP_ARRAY[i+x][j+y]==1){
						return false;				
					}	
				}
			}			
		}
		return true;				
	}
	
	//磚塊組陣列寫入地圖陣列
	public void addBrickToMap(){
		for(int i=0;i<=4;i++){
			for(int j=0;j<=4;j++){
				if((NOW_BRICK_X+i)>=0 && (NOW_BRICK_X+i)<GRIDX &&
				   (NOW_BRICK_Y+j)>=0 && (NOW_BRICK_Y+j)<GRIDY &&
				   NowBrick.BRICK_ARRAY[NOW_BRICK_DIRECT][i][j]==1){
					MAP_ARRAY[NOW_BRICK_X+i][NOW_BRICK_Y+j]=1;
					MAP_COLOR_ARRAY[NOW_BRICK_X+i][NOW_BRICK_Y+j]=NOW_BRICK_COLOR;
				}				
			}				
		}	
	}
	
	//產生下一個磚塊組
	public void addNextBrick(){
		NextBrickArray.remove(0);
		NextBrickColorArray.remove(0);
		NOW_BRICK=((Integer)NextBrickArray.get(0)).intValue();
		NOW_BRICK_COLOR=(Color)NextBrickColorArray.get(0);
		NOW_BRICK_DIRECT=0;
		NOW_BRICK_X=(GRIDX-5)/2;
		NOW_BRICK_Y=0;
		NowBrick=(SuperBrick)BrickArray.get(NOW_BRICK);
		createNextBrick();
	}
	
	//顯示下一個磚塊組
	public void showNextBrick(){
		if(this.isPut(NOW_BRICK_X,
		   			  NOW_BRICK_Y,
					  NOW_BRICK,
		              NOW_BRICK_DIRECT)){
			d.DrawBrickGroup(NOW_BRICK_X,
			   			     NOW_BRICK_Y,
						     NOW_BRICK,
			                 NOW_BRICK_DIRECT,
			                 NOW_BRICK_COLOR);
		}else{
			DownThread.stop();			
		}
	}
	
	//檢查滿行
	public void checkFull(){
		boolean blFull=true;//滿行旗標
		int delLine=0;//刪除行數計數
		
		for(int j=NOW_BRICK_Y;j<(NOW_BRICK_Y+5);j++){
			blFull=true;
			for(int i=0;i<GRIDX;i++){
				if(j>=0 && j<GRIDY){
					if(MAP_ARRAY[i][j]!=1)
						blFull=false;					
				}else{
					blFull=false;					
				}
			}
			if(blFull){
				removeFull(j);//移除滿行			
				delLine++;
			}
		}
		
		if(delLine>0)
			reLoadMap();
			
		updateGame(delLine);
		
	}		
	
	//移除滿行
	public void removeFull(int line){
		for(int j=line;j>0;j--){
			for(int i=0;i<GRIDX;i++){				
				MAP_ARRAY[i][j]=MAP_ARRAY[i][j-1];
				MAP_COLOR_ARRAY[i][j]=MAP_COLOR_ARRAY[i][j-1];
			}								
		}
		
		for(int i=0;i<GRIDX;i++){
			MAP_ARRAY[i][0]=0;
			MAP_COLOR_ARRAY[i][0]=Color.black;
		}
	}
	
	//重新載入地圖磚塊
	public void reLoadMap(){
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				if(MAP_ARRAY[i][j]==1)
					d.DrawBrick(i,j,MAP_COLOR_ARRAY[i][j]);
				else
					d.DrawBrick(i,j,Color.black);
					//d.DrawBrick(i,j,Color.white);
			}				
		}	
	}
	
	//顯示下一組磚塊
	public void showNextBrickGroup(){
		new MoveThread(this).start();
	}
	
	//按鍵事件
	public synchronized void keyPress(int KEYCODE){
		int x=NOW_BRICK_X;
		int y=NOW_BRICK_Y;
		int direct=NOW_BRICK_DIRECT;		
		switch(KEYCODE){
		case KeyEvent.VK_UP://上
			NOW_BRICK_DIRECT++;
			if(NOW_BRICK_DIRECT>=4)
				NOW_BRICK_DIRECT=0;
				
			if(isPut(NOW_BRICK_X,
					 NOW_BRICK_Y,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){                   					                      
				d.DrawBrickGroup(x,y,direct);
			}else{
				NOW_BRICK_DIRECT--;
				if(NOW_BRICK_DIRECT<0)
					NOW_BRICK_DIRECT=3;
			}
			/*if(isPut(NOW_BRICK_X,
					 NOW_BRICK_Y-1,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_Y--;
				d.DrawBrickGroup(x,y,direct);
		
			}else{//到底
				showNextBrickGroup();
				addBrickToMap();
				checkFull();
				
				addNextBrick();
				
				showNextBrick();
			}*/			
			break;				
		case KeyEvent.VK_DOWN://下
			if(isPut(NOW_BRICK_X,
					 NOW_BRICK_Y+1,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_Y++;
				d.DrawBrickGroup(x,y,direct);
		
			}else{//到底
				showNextBrickGroup();
				addBrickToMap();
				checkFull();
				
				addNextBrick();
				
				showNextBrick();
			}
			break;
			
		case KeyEvent.VK_LEFT://左
			if(isPut(NOW_BRICK_X-1,
					 NOW_BRICK_Y,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_X--;
				d.DrawBrickGroup(x,y,direct);
			}		
			break;
						
		case KeyEvent.VK_RIGHT://右
			if(isPut(NOW_BRICK_X+1,
					 NOW_BRICK_Y,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_X++;
				d.DrawBrickGroup(x,y,direct);
			}
			break;
								
		case KeyEvent.VK_SPACE://直落
			/*NOW_BRICK_DIRECT++;
			if(NOW_BRICK_DIRECT>=4)
				NOW_BRICK_DIRECT=0;
				
			if(isPut(NOW_BRICK_X,
					 NOW_BRICK_Y,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){                   					                      
				d.DrawBrickGroup(x,y,direct);
			}else{
				NOW_BRICK_DIRECT--;
				if(NOW_BRICK_DIRECT<0)
					NOW_BRICK_DIRECT=3;
			}*/

			while(isPut(NOW_BRICK_X,
				        NOW_BRICK_Y+1,
					    NOW_BRICK,
						NOW_BRICK_DIRECT)){
				NOW_BRICK_Y++;
				d.DrawBrickGroup(x,y,direct);
				x=NOW_BRICK_X;
				y=NOW_BRICK_Y;
			}
			
			addBrickToMap();
			checkFull();
			addNextBrick();
			showNextBrickGroup();
			showNextBrick();
			break;				
			
		case KeyEvent.VK_ESCAPE://跳離
			System.exit(0);
			break;	
										
		case KeyEvent.VK_PAUSE://暫停
			if(DownThread.isAlive())
				DownThread.stop();
			else{
				DownThread=new Thread(this);//產生控制下降的執行緒
				DownThread.start();
			}	
			break;	
		}				
	}
}


/*相關繪圖方法。
 *建立時需以SingleTetirs物件違建構子參數，
 *負責方塊繪製，方塊區外框繪製，字型繪製等
 */
class Draw{
	
	SingleTetirs st;//俄羅斯磚塊組單一類別
		
	//建構子
	public Draw(SingleTetirs st){
		this.st=st;//指定繪圖區
	}	
    
    //畫出磚塊組動作區地圖外框	
	public void DrawMapFrame(int x,int y,int wid,int hig,int side){
		
		//定義多邊形座標
		int xx[][]={{x,x+wid,x+wid-side,x+side},//上
			    	{x+wid-side,x+wid,x+wid,x+wid-side},//右
					{x+side,x+wid-side,x+wid,x},//下
					{x,x+side,x+side,x}};//左

		int yy[][]={{y,y,y+side,y+side},//上
			    	{y+side,y,y+hig,y+hig-side},//右
					{y+hig-side,y+hig-side,y+hig,y+hig},//下
					{y,y+side,y+hig-side,y+hig}};//左		
	
				
		//畫出多邊形
		for(int i=0;i<=3;i++){
			
			//不同邊設定不同顏色
			switch(i){
				case 0:
					st.g.setColor(new Color(150,150,150));
					break;
				case 1:
					st.g.setColor(new Color(150,150,150));
					break;
				case 2:
					st.g.setColor(new Color(180,180,180));
					break;
				case 3:
					st.g.setColor(new Color(180,180,180));
					break;
			}
			
			st.g.fillPolygon(xx[i],yy[i],4);				
			st.g.setColor(Color.black);
			//st.g.setColor(Color.white);
			st.g.fillRect(x+side-1,y+side-1,wid-side*2+2,hig-side*2+2);
		}
	}

	//畫出磚塊組
	public void DrawBrickGroup(int x,int y,int type,int direct,Color c){
		SuperBrick sb=(SuperBrick)st.BrickArray.get(type);
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(sb.BRICK_ARRAY[direct][i][j]==1){
					DrawBrick((i+x),(j+y),c);
				}
			}	
		}
	}
	
	//畫出磚塊組(清除前一個磚塊組)
	public void DrawBrickGroup(int fx,int fy,int fdirect){
		//清除前一個磚塊組
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if((i+st.NOW_BRICK_X)>=0 && (i+st.NOW_BRICK_X)<st.GRIDX && 
				   (j+st.NOW_BRICK_Y)>=0 && (j+st.NOW_BRICK_Y)<st.GRIDY){
					if(st.NowBrick.BRICK_ARRAY[fdirect][i][j]==1){
						DrawBrick(i+fx,j+fy,Color.black);
						//DrawBrick(i+fx,j+fy,Color.white);
					}
				}
			}	
		}

		//畫出現在磚塊組
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if((i+st.NOW_BRICK_X)>=0 && (i+st.NOW_BRICK_X)<st.GRIDX && 
				   (j+st.NOW_BRICK_Y)>=0 && (j+st.NOW_BRICK_Y)<st.GRIDY){
					if(st.NowBrick.BRICK_ARRAY[st.NOW_BRICK_DIRECT][i][j]==1){
						DrawBrick(i+st.NOW_BRICK_X,
							      j+st.NOW_BRICK_Y,
							      st.NOW_BRICK_COLOR);
					}
				}
			}	
		}
	}	

	//畫出單一磚塊
	public void DrawBrick(int x,int y,Color c){
		st.g.setColor(Color.black);
		//st.g.setColor(Color.white);
		st.g.drawRect(x*st.BRICK_WIDTH+st.MAPX,y*st.BRICK_WIDTH+st.MAPY,
				      st.BRICK_WIDTH-1,st.BRICK_WIDTH-1);
		st.g.setColor(c);
		st.g.fill3DRect(x*st.BRICK_WIDTH+st.MAPX+st.BRICK_SIDE,
						y*st.BRICK_WIDTH+st.MAPY+st.BRICK_SIDE,
				        st.BRICK_WIDTH-st.BRICK_SIDE*2,
				        st.BRICK_WIDTH-st.BRICK_SIDE*2,true);				      		
	}
	
	//畫出字型
	public void DrawFont(String str,int x,int y,int size,Color c,String fs){
		Font f=new Font(fs,Font.BOLD,size);
		st.g.setColor(c);
		st.g.setFont(f);
		st.g.drawString(str,x,y);	
	}
}

//移動預備方塊組
class MoveThread extends Thread{
	SingleTetirs s;
	Graphics g;
	Draw d;
	Component c;
	Image img;
	Graphics sg;
	
	public MoveThread(SingleTetirs s){
		this.s=s;
		this.g=s.g;
		this.d=s.d;
		this.c=s.c;
		
		img=c.createImage(190,340);
		sg=img.getGraphics();			
	}	
	
	public void run(){	
		for(int i=0;i<4;i++){
			try{
				sleep(50);
				showNextBrickGroup(i);
				g.drawImage(img,295,5,c);				
			}catch(Exception e){
				e.printStackTrace();	
			}
		}
	}
	
	//顯示下一組磚塊
	public void showNextBrickGroup(int index){

		sg.setColor(Color.black);
		//sg.setColor(Color.white);
		sg.fillRect(0,0,190,340);	

		for(int j=0;j<s.NextBrickArray.size();j++){
			int m=((Integer)s.NextBrickArray.get(j)).intValue();
			Color c=((Color)s.NextBrickColorArray.get(j));
			DrawBrickGroup((2),((j)*4)-index,m,0,c);
		}
	}	
	
	//畫出磚塊組
	public void DrawBrickGroup(int x,int y,int type,int direct,Color c){
		SuperBrick sb=(SuperBrick)s.BrickArray.get(type);
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(sb.BRICK_ARRAY[direct][i][j]==1){
					DrawBrick((i+x),(j+y),c);
				}
			}	
		}
	}
		
	//畫出單一磚塊
	public void DrawBrick(int x,int y,Color c){
		sg.setColor(Color.black);
		//sg.setColor(Color.white);
		sg.drawRect(x*s.BRICK_WIDTH-9,y*s.BRICK_WIDTH,
				      s.BRICK_WIDTH-1,s.BRICK_WIDTH-1);
		sg.setColor(c);
		sg.fill3DRect(x*s.BRICK_WIDTH-9,
						y*s.BRICK_WIDTH,
				        s.BRICK_WIDTH-s.BRICK_SIDE*2,
				        s.BRICK_WIDTH-s.BRICK_SIDE*2,true);				      
	}	
}