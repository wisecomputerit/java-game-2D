/*程式：MDraw.java
 *說明：踩地雷遊戲繪圖運算。
 *      圖片載入、圖片貼圖、次顯示頁轉換、畫面更新等
 */

import java.awt.*;
import javax.swing.*;

class MDraw{
	
	//遊戲數據
	int GRIDX;  //地圖x軸格數
	int GRIDY;  //地圖y軸格數
	int MINES;  //地雷數
	Container c;//容器
	
	
	//次顯示區
	Image offI;
	Graphics offG;
	
	//圖片陣列
	Image[] imgSmile;
	Image[] imgMap;
	Image[] imgCount;
	
	//所有圖片寬與長
	int SMILE_WIDTH;
	int SMILE_HEIGHT;
	int MAP_WIDTH;
	int MAP_HEIGHT;
	int COUNT_WIDTH;
	int COUNT_HEIGHT;
	
	//各區域座標
	int STATE_X;//遊戲狀態區
	int STATE_Y;
	int COUNT_X;//時間計數
	int COUNT_Y;
	int MCOUNT_X;//地雷計數
	int MCOUNT_Y;
	int MAP_X;//地雷區
	int MAP_Y;	
	int SMILE_X;//笑臉
	int SMILE_Y;
		
	public MDraw(int gridx,   //地圖x軸格數
				 int gridy,   //地圖y軸格數
				 int mines,   //地雷數
				 Container c){//容器

		//資料初始
		this.GRIDX=gridx;
		this.GRIDY=gridy;
		this.MINES=mines;
		this.c=c;
		
		
		//建立次畫面
		offI=c.createImage(c.getWidth(),c.getHeight());
		offG=offI.getGraphics();		
		
		//讀入所有圖片
		imgSmile=new Image[5]; //遊戲狀態圖(笑臉)
		for(int i=0;i<=4;i++)
			imgSmile[i]=(new ImageIcon("pic/smile/s"+i+".gif")).getImage();	
					
		imgCount=new Image[10];//計數(LED字型)
		for(int i=0;i<=9;i++)
			imgCount[i]=(new ImageIcon("pic/count/"+i+".gif")).getImage();	
			
		imgMap=new Image[15];  //地圖上(數字、地雷)
		for(int i=0;i<=14;i++)
			imgMap[i]=(new ImageIcon("pic/map/m"+i+".gif")).getImage();
		
		//紀錄圖片寬與長
		SMILE_WIDTH=imgSmile[0].getWidth(null);//遊戲狀態圖
		SMILE_HEIGHT=imgSmile[0].getHeight(null);
		COUNT_WIDTH=imgCount[0].getWidth(null);//計數圖
		COUNT_HEIGHT=imgCount[0].getHeight(null);		
		MAP_WIDTH=imgMap[0].getWidth(null);//地圖狀態圖
		MAP_HEIGHT=imgMap[0].getHeight(null);
		
		//所有區域座標
		MAP_X=MAP_WIDTH;//地雷區
		MAP_Y=MAP_HEIGHT*5;
		DrawMapFrame(MAP_X-3,MAP_Y-3,GRIDX*MAP_WIDTH+6,GRIDY*MAP_HEIGHT+6,3,false);
		
		STATE_X=MAP_WIDTH;//遊戲狀態區
		STATE_Y=MAP_HEIGHT;
		DrawMapFrame(STATE_X-3,STATE_Y-3,GRIDX*MAP_WIDTH+6,MAP_HEIGHT*3+6,3,false);		

		MCOUNT_X=STATE_X+(MAP_HEIGHT*3-COUNT_HEIGHT)/2;//地雷數
		MCOUNT_Y=STATE_Y+(MAP_HEIGHT*3-COUNT_HEIGHT)/2;
		DrawMapFrame(MCOUNT_X-2,MCOUNT_Y-2,COUNT_WIDTH*3+4,COUNT_HEIGHT+4,2,false);				

		COUNT_X=(STATE_X-3)+(GRIDX*MAP_WIDTH+6)-MAP_WIDTH-COUNT_WIDTH*3;//秒數
		COUNT_Y=MCOUNT_Y;
		DrawMapFrame(COUNT_X-2,COUNT_Y-2,COUNT_WIDTH*3+4,COUNT_HEIGHT+4,2,false);				

		SMILE_X=(STATE_X-3)+(GRIDX*MAP_WIDTH+6-SMILE_WIDTH)/2;//笑臉
		SMILE_Y=STATE_Y+(MAP_HEIGHT*3-SMILE_HEIGHT)/2;
				
		
		//畫出立體框
		DrawMapFrame(0,0,(GRIDX+2)*MAP_WIDTH,(GRIDY+6)*MAP_HEIGHT,4,true);			
		
		//mapInit();//地圖初始化
		showCount(MCOUNT_X,MCOUNT_Y,MINES);//顯示地雷數
		showCount(COUNT_X,COUNT_Y,0);//顯示秒數
		showSmile(0);//顯示笑臉
		
	}
	
	//顯示地圖圖示
	public void showMap(int x,int y,int mode){
		if(x>=0 && x<GRIDX &&//防止超出陣列
		   y>=0 && y<GRIDY){		
			offG.drawImage(imgMap[mode],
						   MAP_X+x*MAP_WIDTH,
						   MAP_Y+y*MAP_HEIGHT,
						   null);
		}
	}

	//顯示笑臉
	public void showSmile(int i){
		if(i>=0 && i<=4)
			offG.drawImage(imgSmile[i],SMILE_X,SMILE_Y,null);
		update();
	}
	
	//顯示計數數字
	public void showCount(int x,int y,int count){
		if(count>=0){
			offG.drawImage(imgCount[(count%1000)/100],x,y,null);
			offG.drawImage(imgCount[(count%100)/10],x+COUNT_WIDTH,y,null);
			offG.drawImage(imgCount[(count%10)],x+COUNT_WIDTH*2,y,null);
			update();//更新畫面
		}
	}
	
    //畫出磚塊組動作區地圖外框	
	public void DrawMapFrame(int x,int y,int wid,int hig,int side,boolean raised){
		
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
			if(raised){//設定框為凸起
				switch(i){//不同邊設定不同顏色
					case 0:
						offG.setColor(new Color(250,250,250));
						break;
					case 1:
						offG.setColor(new Color(120,120,120));
						break;
					case 2:
						offG.setColor(new Color(100,100,100));
						break;
					case 3:
						offG.setColor(new Color(240,240,240));
						break;
				}
			}else{//設定框為凹陷
				switch(i){//不同邊設定不同顏色
					case 0:
						offG.setColor(new Color(100,100,100));
						break;
					case 1:
						offG.setColor(new Color(240,240,240));
						break;
					case 2:
						offG.setColor(new Color(250,250,250));
						break;
					case 3:
						offG.setColor(new Color(120,120,120));						
						break;
				}
			}
			offG.fillPolygon(xx[i],yy[i],4);				
		}
		update();//更新畫面
	}	
	
	public void update(){
		c.getGraphics().drawImage(offI,0,0,null);
	}
}