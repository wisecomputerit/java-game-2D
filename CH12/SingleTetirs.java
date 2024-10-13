/*�{���GSingleTetirs.java
 *�����G��@�Xù������C�����O
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

//��@�j���չC�����O
public class SingleTetirs implements Runnable{
	
	Graphics g;//ø�ϰ�
	Component c;
	Thread DownThread;
	long GAME_SPEED;//�����C���t��
	long GAME_SCORE;//�����C������
	long GAME_LEVEL;//�����C������
	long GAME_LINE;//�����C���������
	boolean GAME_RUNNING;//�����C���O�_�i�椤
	Draw d;//�e�X�j���覡
	
	int MAPX;//�a��X�y��
	int MAPY;//�a��Y�y��
	
	int MAP_ARRAY[][];//�a�ϰ}�C
	int MAP_SHOW_ARRAY[][];//�a����ܰ}�C(�P���ʿj���յ��X���}�C)
	Color MAP_COLOR_ARRAY[][];//�a���C��}�C
	
	int GRIDX;//x�b���
	int GRIDY;//y�b���

	int BRICK_WIDTH;//��@�j��_�e	
	int BRICK_SIDE;//��@�j��_��
	
	int NEXT_BRICK_NUMBER;//�U�@�ӿj������ܼƶq
	
	boolean IS_SHOW_NEXT;//�O�_��ܤU�@�ӿj����
	
	int NOW_BRICK;//�{�b�ʧ@�j���սs��
	int NOW_BRICK_DIRECT;//�{�b�ʧ@�j���ժ���V
	int NOW_BRICK_X;//�{�b�ʧ@�j���ժ�x�y��
	int NOW_BRICK_Y;//�{�b�ʧ@�j���ժ�y�y��
	SuperBrick NowBrick;//�{�b�ʧ@���j����
	Color NOW_BRICK_COLOR;//�{�b�ʧ@�j���ժ��C��
	
	ArrayList BrickArray;//�Ҧ��j�������O�}�C
	ArrayList NextBrickArray;//�U�@�ӿj�����x�s�}�C
	ArrayList NextBrickColorArray;//�U�@�ӿj�����x�s�}�C
		
	public SingleTetirs(Graphics g,//ø�ϰ�
					    int mapx,int mapy,//�a�Ϯy��
					    int gridx,int gridy,//�a�Ϥ���
					    int brick_width,//�j���ռe
					    int brick_side,//�j������
					    int next_brick_number,//��ܤU�@�ӿj���ռƶq
					    boolean is_show_next,//�O�_��ܤU�@�ӿj����
					    Component c){//����
		//��l�U�Ѽ�
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
		
		//�N�a�ϰ}�C�k�s
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				this.MAP_ARRAY[i][j]=0;
				this.MAP_SHOW_ARRAY[i][j]=0;
			}			
		}
		
		//�[�J�Ҧ��j��������
		BrickArray=new ArrayList();//�إ��x�s�j���ի��A���}�C
		BrickArray.add(new Brick_BB());
		BrickArray.add(new Brick_I());
		BrickArray.add(new Brick_L());
		BrickArray.add(new Brick_UL());
		BrickArray.add(new Brick_UT());
		BrickArray.add(new Brick_UZ());
		BrickArray.add(new Brick_Z());
		
		//���ͤU�@�j���ո��
		NextBrickArray=new ArrayList();//�إ��x�s�w�ƿj���սs�����}�C
		NextBrickColorArray=new ArrayList();//�إ��x�s�w�ƿj���սs�����}�C
		for(int i=0;i<(NEXT_BRICK_NUMBER+1);i++){
			createNextBrick();
		}

		//�إ�ø�X�j���ժ���
		d=new Draw(this);
		
		//�e�X�~��
		d.DrawMapFrame(0,0,GRIDX*BRICK_WIDTH+2*5,GRIDY*BRICK_WIDTH+2*5,5);
		d.DrawMapFrame(290,0,200,350,5);//�w�Ƥ���~��
		d.DrawMapFrame(290,350,200,140,5);//�C���ƾڥ~��
		
		showNextBrickGroup();//��ܴ��ܤ����
		addNextBrick();//�s�W�@�Ӵ��ܤ��
		showNextBrick();//��ܲ{�b�ʧ@���
		
		updateGame(0);//��s�C�����
		
		//���ͱ���U���������
		DownThread=new Thread(this);
		DownThread.start();
	}
	
	//��ø����
	public void paint(){
		//�e�X�~��
		d.DrawMapFrame(0,0,GRIDX*BRICK_WIDTH+2*5,GRIDY*BRICK_WIDTH+2*5,5);
		d.DrawMapFrame(290,0,200,350,5);//�w�Ƥ��
		d.DrawMapFrame(290,350,200,140,5);//�C���ƾ�	
		showNextBrickGroup();
		updateGame(0);
		reLoadMap();
		showNextBrick();
					
	}
	
	//�л\run()��k
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
	
	//��s�C�����
	public void updateGame(int line){
		d.DrawMapFrame(290,350,200,140,5);
		GAME_SCORE+=50*line*line*line*(0.5)*GAME_LEVEL;
		GAME_LEVEL=GAME_SCORE/5000+1;
		GAME_SPEED=800/GAME_LEVEL+200;
		GAME_LINE+=line;
		
		d.DrawFont("�ż�:"+GAME_LEVEL,300,400,16,Color.cyan,"�з���");
		d.DrawFont("����:"+GAME_SCORE,300,430,16,Color.red,"�з���");		
		d.DrawFont("���:"+GAME_LINE,300,460,16,Color.green,"�з���");
	}	
		
	//���ͤU�@�տj��
	public void createNextBrick(){
		//���͹w�ƿj���սs��
		NextBrickArray.add(new Integer((int)(Math.random()*BrickArray.size())));
		//���͹w�ƿj�����C��
		NextBrickColorArray.add(new Color((int)(Math.random()*128+127),
										  (int)(Math.random()*128+127),
										  (int)(Math.random()*128+127)));		
	}
	
	//�ˬd�j���լO�_�i�H��m
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
	
	//�j���հ}�C�g�J�a�ϰ}�C
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
	
	//���ͤU�@�ӿj����
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
	
	//��ܤU�@�ӿj����
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
	
	//�ˬd����
	public void checkFull(){
		boolean blFull=true;//����X��
		int delLine=0;//�R����ƭp��
		
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
				removeFull(j);//��������			
				delLine++;
			}
		}
		
		if(delLine>0)
			reLoadMap();
			
		updateGame(delLine);
		
	}		
	
	//��������
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
	
	//���s���J�a�Ͽj��
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
	
	//��ܤU�@�տj��
	public void showNextBrickGroup(){
		new MoveThread(this).start();
	}
	
	//����ƥ�
	public synchronized void keyPress(int KEYCODE){
		int x=NOW_BRICK_X;
		int y=NOW_BRICK_Y;
		int direct=NOW_BRICK_DIRECT;		
		switch(KEYCODE){
		case KeyEvent.VK_UP://�W
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
		
			}else{//�쩳
				showNextBrickGroup();
				addBrickToMap();
				checkFull();
				
				addNextBrick();
				
				showNextBrick();
			}*/			
			break;				
		case KeyEvent.VK_DOWN://�U
			if(isPut(NOW_BRICK_X,
					 NOW_BRICK_Y+1,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_Y++;
				d.DrawBrickGroup(x,y,direct);
		
			}else{//�쩳
				showNextBrickGroup();
				addBrickToMap();
				checkFull();
				
				addNextBrick();
				
				showNextBrick();
			}
			break;
			
		case KeyEvent.VK_LEFT://��
			if(isPut(NOW_BRICK_X-1,
					 NOW_BRICK_Y,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_X--;
				d.DrawBrickGroup(x,y,direct);
			}		
			break;
						
		case KeyEvent.VK_RIGHT://�k
			if(isPut(NOW_BRICK_X+1,
					 NOW_BRICK_Y,
					 NOW_BRICK,
					 NOW_BRICK_DIRECT)){
				NOW_BRICK_X++;
				d.DrawBrickGroup(x,y,direct);
			}
			break;
								
		case KeyEvent.VK_SPACE://����
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
			
		case KeyEvent.VK_ESCAPE://����
			System.exit(0);
			break;	
										
		case KeyEvent.VK_PAUSE://�Ȱ�
			if(DownThread.isAlive())
				DownThread.stop();
			else{
				DownThread=new Thread(this);//���ͱ���U���������
				DownThread.start();
			}	
			break;	
		}				
	}
}


/*����ø�Ϥ�k�C
 *�إ߮ɻݥHSingleTetirs����H�غc�l�ѼơA
 *�t�d���ø�s�A����ϥ~��ø�s�A�r��ø�s��
 */
class Draw{
	
	SingleTetirs st;//�Xù���j���ճ�@���O
		
	//�غc�l
	public Draw(SingleTetirs st){
		this.st=st;//���wø�ϰ�
	}	
    
    //�e�X�j���հʧ@�Ϧa�ϥ~��	
	public void DrawMapFrame(int x,int y,int wid,int hig,int side){
		
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
			
			//���P��]�w���P�C��
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

	//�e�X�j����
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
	
	//�e�X�j����(�M���e�@�ӿj����)
	public void DrawBrickGroup(int fx,int fy,int fdirect){
		//�M���e�@�ӿj����
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

		//�e�X�{�b�j����
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

	//�e�X��@�j��
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
	
	//�e�X�r��
	public void DrawFont(String str,int x,int y,int size,Color c,String fs){
		Font f=new Font(fs,Font.BOLD,size);
		st.g.setColor(c);
		st.g.setFont(f);
		st.g.drawString(str,x,y);	
	}
}

//���ʹw�Ƥ����
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
	
	//��ܤU�@�տj��
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
	
	//�e�X�j����
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
		
	//�e�X��@�j��
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