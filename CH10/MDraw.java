/*�{���GMDraw.java
 *�����G��a�p�C��ø�ϹB��C
 *      �Ϥ����J�B�Ϥ��K�ϡB����ܭ��ഫ�B�e����s��
 */

import java.awt.*;
import javax.swing.*;

class MDraw{
	
	//�C���ƾ�
	int GRIDX;  //�a��x�b���
	int GRIDY;  //�a��y�b���
	int MINES;  //�a�p��
	Container c;//�e��
	
	
	//����ܰ�
	Image offI;
	Graphics offG;
	
	//�Ϥ��}�C
	Image[] imgSmile;
	Image[] imgMap;
	Image[] imgCount;
	
	//�Ҧ��Ϥ��e�P��
	int SMILE_WIDTH;
	int SMILE_HEIGHT;
	int MAP_WIDTH;
	int MAP_HEIGHT;
	int COUNT_WIDTH;
	int COUNT_HEIGHT;
	
	//�U�ϰ�y��
	int STATE_X;//�C�����A��
	int STATE_Y;
	int COUNT_X;//�ɶ��p��
	int COUNT_Y;
	int MCOUNT_X;//�a�p�p��
	int MCOUNT_Y;
	int MAP_X;//�a�p��
	int MAP_Y;	
	int SMILE_X;//���y
	int SMILE_Y;
		
	public MDraw(int gridx,   //�a��x�b���
				 int gridy,   //�a��y�b���
				 int mines,   //�a�p��
				 Container c){//�e��

		//��ƪ�l
		this.GRIDX=gridx;
		this.GRIDY=gridy;
		this.MINES=mines;
		this.c=c;
		
		
		//�إߦ��e��
		offI=c.createImage(c.getWidth(),c.getHeight());
		offG=offI.getGraphics();		
		
		//Ū�J�Ҧ��Ϥ�
		imgSmile=new Image[5]; //�C�����A��(���y)
		for(int i=0;i<=4;i++)
			imgSmile[i]=(new ImageIcon("pic/smile/s"+i+".gif")).getImage();	
					
		imgCount=new Image[10];//�p��(LED�r��)
		for(int i=0;i<=9;i++)
			imgCount[i]=(new ImageIcon("pic/count/"+i+".gif")).getImage();	
			
		imgMap=new Image[15];  //�a�ϤW(�Ʀr�B�a�p)
		for(int i=0;i<=14;i++)
			imgMap[i]=(new ImageIcon("pic/map/m"+i+".gif")).getImage();
		
		//�����Ϥ��e�P��
		SMILE_WIDTH=imgSmile[0].getWidth(null);//�C�����A��
		SMILE_HEIGHT=imgSmile[0].getHeight(null);
		COUNT_WIDTH=imgCount[0].getWidth(null);//�p�ƹ�
		COUNT_HEIGHT=imgCount[0].getHeight(null);		
		MAP_WIDTH=imgMap[0].getWidth(null);//�a�Ϫ��A��
		MAP_HEIGHT=imgMap[0].getHeight(null);
		
		//�Ҧ��ϰ�y��
		MAP_X=MAP_WIDTH;//�a�p��
		MAP_Y=MAP_HEIGHT*5;
		DrawMapFrame(MAP_X-3,MAP_Y-3,GRIDX*MAP_WIDTH+6,GRIDY*MAP_HEIGHT+6,3,false);
		
		STATE_X=MAP_WIDTH;//�C�����A��
		STATE_Y=MAP_HEIGHT;
		DrawMapFrame(STATE_X-3,STATE_Y-3,GRIDX*MAP_WIDTH+6,MAP_HEIGHT*3+6,3,false);		

		MCOUNT_X=STATE_X+(MAP_HEIGHT*3-COUNT_HEIGHT)/2;//�a�p��
		MCOUNT_Y=STATE_Y+(MAP_HEIGHT*3-COUNT_HEIGHT)/2;
		DrawMapFrame(MCOUNT_X-2,MCOUNT_Y-2,COUNT_WIDTH*3+4,COUNT_HEIGHT+4,2,false);				

		COUNT_X=(STATE_X-3)+(GRIDX*MAP_WIDTH+6)-MAP_WIDTH-COUNT_WIDTH*3;//���
		COUNT_Y=MCOUNT_Y;
		DrawMapFrame(COUNT_X-2,COUNT_Y-2,COUNT_WIDTH*3+4,COUNT_HEIGHT+4,2,false);				

		SMILE_X=(STATE_X-3)+(GRIDX*MAP_WIDTH+6-SMILE_WIDTH)/2;//���y
		SMILE_Y=STATE_Y+(MAP_HEIGHT*3-SMILE_HEIGHT)/2;
				
		
		//�e�X�����
		DrawMapFrame(0,0,(GRIDX+2)*MAP_WIDTH,(GRIDY+6)*MAP_HEIGHT,4,true);			
		
		//mapInit();//�a�Ϫ�l��
		showCount(MCOUNT_X,MCOUNT_Y,MINES);//��ܦa�p��
		showCount(COUNT_X,COUNT_Y,0);//��ܬ��
		showSmile(0);//��ܯ��y
		
	}
	
	//��ܦa�Ϲϥ�
	public void showMap(int x,int y,int mode){
		if(x>=0 && x<GRIDX &&//����W�X�}�C
		   y>=0 && y<GRIDY){		
			offG.drawImage(imgMap[mode],
						   MAP_X+x*MAP_WIDTH,
						   MAP_Y+y*MAP_HEIGHT,
						   null);
		}
	}

	//��ܯ��y
	public void showSmile(int i){
		if(i>=0 && i<=4)
			offG.drawImage(imgSmile[i],SMILE_X,SMILE_Y,null);
		update();
	}
	
	//��ܭp�ƼƦr
	public void showCount(int x,int y,int count){
		if(count>=0){
			offG.drawImage(imgCount[(count%1000)/100],x,y,null);
			offG.drawImage(imgCount[(count%100)/10],x+COUNT_WIDTH,y,null);
			offG.drawImage(imgCount[(count%10)],x+COUNT_WIDTH*2,y,null);
			update();//��s�e��
		}
	}
	
    //�e�X�j���հʧ@�Ϧa�ϥ~��	
	public void DrawMapFrame(int x,int y,int wid,int hig,int side,boolean raised){
		
		//�w�q�h��ήy��
		int xx[][]={{x,x+wid,x+wid-side,x+side},//�W
			    	{x+wid-side,x+wid,x+wid,x+wid-side},//�k
					{x+side,x+wid-side,x+wid,x},//�U
					{x,x+side,x+side,x}};//��

		int yy[][]={{y,y,y+side,y+side},//�W
			    	{y+side,y,y+hig,y+hig-side},//�k
					{y+hig-side,y+hig-side,y+hig,y+hig},//�U
					{y,y+side,y+hig-side,y+hig}};//��		
	
				
		//�e�X�h���
		for(int i=0;i<=3;i++){
			if(raised){//�]�w�ج��Y�_
				switch(i){//���P��]�w���P�C��
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
			}else{//�]�w�ج��W��
				switch(i){//���P��]�w���P�C��
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
		update();//��s�e��
	}	
	
	public void update(){
		c.getGraphics().drawImage(offI,0,0,null);
	}
}