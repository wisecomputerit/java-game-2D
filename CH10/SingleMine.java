/*�{���GSingleMine.java
 *�����G��a�p�C���D�n�������O
 */
 
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.swing.*;

class SingleMine implements MouseListener,
							MouseMotionListener{
	
	int GRIDX;  //�a��x�b���
	int GRIDY;  //�a��y�b���
	int MINES;  //�a�p��
	int MMINES; //�w�Хܦa�p��
	int TIME_COUNT;//�ɶ��p��
	Container c;//�e��
	int MAP[][];//�a�p�G�m����;0�G�ťաB1-8�G�g��a�p�ơB9�G�a�p
	int FACE[][];//�a�p������;0�G�w�����B1�G�������B2�G���Ѥl�B3�G�ð�
	
	Image offI;
	Graphics offG;
	MDraw md;
	Timer timer;//�p�ɾ�
	
	int MOUSE_POSITION;//�ƹ���m;0:���y,1:�a��,2:��L
	int MOUSE_PRESSED_START;//�ƹ����U��m;0:���y,1:�a��,2:��L
	int MOUSE_X;//�ƹ��۹����a�p�Ϯy��
	int MOUSE_Y;
	
	boolean MOUSE_RIGHT_PRESSED;//�ƹ��k����U
	boolean MOUSE_LEFT_PRESSED;//�ƹ�������U
	boolean GAME_START;//�C���}�l
	boolean GAME_OVER;//�C������
	boolean GAME_FINAL;//�C������
	boolean UNDO;//���k�䤣�ʧ@;
	
	int GAME_RUN;//�C���O�_���� [true:�i�椤],[false:����]
	int GAME_STATE;//�C�����A [1:���\],[0:�@��],[-1:����]
	
	
	public SingleMine(int gridx,   //�a��x�b���
					  int gridy,   //�a��y�b���
					  int mines,   //�a�p��
					  Container c){//�e��
		
		//��ƪ�l
		this.GRIDX=gridx;
		this.GRIDY=gridy;
		this.MINES=mines;
		this.MMINES=mines;
		this.c=c;
		
		//����ø�Ϫ���
		md=new MDraw(gridx,gridy,mines,c);
		
		//���ͦa�p�G�m�}�C�Ϊ����Ψê�l�}�C��
		MAP=new int[gridx][gridy];
		FACE=new int[gridx][gridy];
		
		//�C�����m
		reset();
  		md.update();			
	}
	
	private class EventListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			TIME_COUNT++;
			md.showCount(md.COUNT_X,md.COUNT_Y,TIME_COUNT);	
		}	
	}	
	
	//�H�üƧG�m�a�p��m
	public void setMinePosition(){
		int m=this.MINES;
		
		//��a�p���G�m���ɫ���G�m
		while(m>0){
			
			//�üƲ��ͮy��
			int x=(int)(Math.random()*GRIDX);	
			int y=(int)(Math.random()*GRIDY);
			
			//�P�_���ͪ��y�ЬO�_����			
			if(x>=0 && x<GRIDX &&//����W�X�}�C
			   y>=0 && y<GRIDY &&
			   MAP[x][y]!=9 && //�}�C�Ȥ���ܦa�p
			   !(x==MOUSE_X && y==MOUSE_Y)){
			   	
				m--;//�G�m�a�p�Ѿl��
				MAP[x][y]=9;//�a�Ϯy�мХܬ��a�p
				
				//�N�a�p�P��Ʀr�[�@
				for(int i=(x-1);i<=(x+1);i++){
					for(int j=(y-1);j<=(y+1);j++){
						if(i>=0 && i<GRIDX &&//�d��b�}�C��
						   j>=0 && j<GRIDY &&
						   MAP[i][j]!=9 &&//�}�C�Ȥ���ܦa�p
						   !(i==x && j==y)){//�����a�p�y��
						 	MAP[i][j]++;  	
						}   
					}						
				}	
			}
		}
	}
	
	//���o�����j�p
	public static Dimension getDimension(int x,int y){
		return new Dimension((x+2)*16+10,(y+6)*16+50);
	}
	
	//�ƹ�����
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		
		setMouseMapPosition(e);//�]�w�ƹ��b�a�ϤW�y��			
		switch(e.getButton()){
		case MouseEvent.BUTTON1://����
			UNDO=false;
			//�������䪬�A
			MOUSE_LEFT_PRESSED=true;
			
			//�]�w�ƹ�������U���_�l�ϰ�
			MOUSE_PRESSED_START=MOUSE_POSITION;
			
			//�C�����A
			switch(GAME_RUN){
			
			case 1://�i�椤
				//�ƹ��Ҧb�ϰ�
				switch(MOUSE_POSITION){
				case 0://���y��
					md.showSmile(1);//���y���U
					break;
				case 1://�a�p��
					if(MOUSE_PRESSED_START!=0){
						if(MOUSE_RIGHT_PRESSED){//�k����U�ɫ��U����
							showMouseDownMap(MOUSE_X,MOUSE_Y);
						}else{//�k�䥼���U�ɫ��U����
							//UNDO=false;
						}
						if(FACE[MOUSE_X][MOUSE_Y]==1)
							md.showMap(MOUSE_X,MOUSE_Y,0);//�ť�
						md.showSmile(2);//���y�i�L	
					}				
					break;
				case 2://�@���
					md.showSmile(2);//���y�i�L
					break;
				}
				break;
				
			case 0://��l����
				//�ƹ��Ҧb�ϰ�
				switch(MOUSE_POSITION){
				case 0://���y��
					md.showSmile(1);//���y���U
					break;
				case 1://�a�p��
					if(MOUSE_PRESSED_START!=0){
						if(MOUSE_RIGHT_PRESSED){//�k����U�ɫ��U����
							showMouseDownMap(MOUSE_X,MOUSE_Y);
						}else{//�k�䥼���U�ɫ��U����
							//UNDO=false;
						}
						if(FACE[MOUSE_X][MOUSE_Y]==1)
							md.showMap(MOUSE_X,MOUSE_Y,0);//�ť�
						md.showSmile(2);//���y�i�L	
					}
					break;
				case 2://�@���
					md.showSmile(2);//���y�i�L
					break;
				}
				break;
				
			case -1://����	
				switch(MOUSE_POSITION){
				case 0://���y��
					md.showSmile(1);//���y���U
					break;
				}
				break;
			}
			break;	
			
		case MouseEvent.BUTTON3://�k��
			UNDO=false;
			//�����k�䪬�A
			MOUSE_RIGHT_PRESSED=true;
			if(GAME_RUN!=-1 && MOUSE_PRESSED_START!=0){
				
				if(MOUSE_LEFT_PRESSED){//�s�P������U��
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
				}else{//�u���k����U��
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
		
		setMouseMapPosition(e);//�]�w�ƹ��b�a�ϤW�y��		
		switch(e.getButton()){
		case MouseEvent.BUTTON1://����
			
			//�������䪬�A
			MOUSE_LEFT_PRESSED=false;
			
			//�C�����A
			switch(GAME_RUN){
			
			case 1://�i�椤
				//�ƹ��Ҧb�ϰ�
				switch(MOUSE_POSITION){
				case 0://���y��
					if(MOUSE_PRESSED_START==0){//�b���y�ϰ_�l(���m�C���C��)
						reset();//�C�����m
					}
					md.showSmile(0);//���y
					break;
				case 1://�a�p��
					if(MOUSE_PRESSED_START!=0){//���b���y�ϰ_�l

						if(!MOUSE_RIGHT_PRESSED && 
						   !UNDO && //�k�䥼���U�P�}����
						   FACE[MOUSE_X][MOUSE_Y]==1){//��ܥ�����
							
							FACE[MOUSE_X][MOUSE_Y]=0;
							switch(MAP[MOUSE_X][MOUSE_Y]){
							case 0://�ť�
								doClear(MOUSE_X,MOUSE_Y);//�M���ť�
								checkFinal();//�ˬd�O�_����
								break;	
							case 9://�a�p(�C������)
								onMine(true);
								break;
							default:
								checkFinal();//�ˬd�O�_����
							}
							
						}else if(MOUSE_RIGHT_PRESSED){//�k����U�P�}����
							UNDO=true;
							md.showSmile(0);//���y
							if(MOUSE_PRESSED_START!=0){
								checkAutoOpen();	
								checkFinal();
							}
						
						}else if(UNDO){
							md.showSmile(0);//���y		
						}else{
							md.showSmile(0);//���y
						}
						reloadMap();
					}					
					break;
				case 2://�@���
					md.showSmile(0);//���y
					break;
				}
				break;
				
			case 0://��l����
				//�ƹ��Ҧb�ϰ�
				switch(MOUSE_POSITION){
				case 0://���y��
					if(MOUSE_PRESSED_START==0)
						reset();
					md.showSmile(0);//���y
					break;
				case 1://�a�p��
					if(MOUSE_PRESSED_START!=0 ){//���O�b���y�ϰ_�l(�}�l�C��)
						if(!MOUSE_RIGHT_PRESSED && //�k�䥼���U�P�}����
						   !UNDO && 
						   FACE[MOUSE_X][MOUSE_Y]==1){
							setMinePosition();//�]�w�a�p��m
							GAME_RUN=1;//�C���}�l
							
							//�Ұʭp�ɾ�
							timer=new Timer(1000,new EventListener());
							timer.start();
	
							FACE[MOUSE_X][MOUSE_Y]=0;//��ܤw����
							switch(MAP[MOUSE_X][MOUSE_Y]){
							case 0://�ť�
								doClear(MOUSE_X,MOUSE_Y);
								break;						
							}
							md.showSmile(0);//���y
							checkFinal();//�ˬd�O�_����
							reloadMap();//���s��ܦa�p�Ϫ��p
						
						}else if(MOUSE_RIGHT_PRESSED){//�k����U�P�}����
							UNDO=true;
							reloadMap();//���s��ܦa�p�Ϫ��p
						}else if(UNDO){
							md.showSmile(0);//���y
						}else{
							md.showSmile(0);//���y	
						}

					}else{
						md.showSmile(0);//���y	
					}
					
					break;
				case 2://�@���
					md.showSmile(0);//���y
					break;
				}
				break;
				
			case -1://����	
				switch(MOUSE_POSITION){
				case 0://���y��
					if(MOUSE_PRESSED_START==0){
						reset();//�C�����m	
					}
					break;
				}
				break;
			}
			break;	
			
		case MouseEvent.BUTTON3://�k��
			//�����k�䪬�A
			MOUSE_RIGHT_PRESSED=false;
			switch(GAME_RUN){
			case 1://�i�椤
				//break;	
			case 0://��l
				if(MOUSE_PRESSED_START!=0){
					if(!MOUSE_LEFT_PRESSED && !UNDO){//���䥼���U�P�}�k��
						md.showSmile(0);//���y
					}else if(MOUSE_LEFT_PRESSED){//������U�P�}�k��
						UNDO=true;
						if(MOUSE_PRESSED_START!=0 && GAME_RUN==1){
							checkAutoOpen();	
							checkFinal();
						}						
					}else if(UNDO){
						md.showSmile(0);//���y	
					}			
					reloadMap();		
				}else{
					md.showSmile(0);//���y	
				}
				break;				
			case -1://����
				break;								
			}
			break;
		}
		md.update();
		MOUSE_PRESSED_START=2;		
	}
	
	//�ƹ��ƥ�
	public void mouseMoved(MouseEvent e){
		setMousePosition(e);//�]�w�ƹ��Ҧb�ϰ�
	}
	
	public void mouseDragged(MouseEvent e){

		//�]�w�ƹ��Ҧb�ϰ�
		setMousePosition(e);
		setMouseMapPosition(e);
		
		//�̹C�����A
		switch(GAME_RUN){
		case 1://�i�椤
		case 0://��l����
			if(!UNDO){
				if(MOUSE_PRESSED_START!=0)
					showMouseDownMap(MOUSE_X,MOUSE_Y);
			}
			
			if(MOUSE_PRESSED_START==0){//�_�l�b���y��
				switch(MOUSE_POSITION){
				case 0://���y��
					md.showSmile(1);//���y���U
					break;
				case 1://�a�p��
				case 2://�@���
					md.showSmile(0);//���y							
					break;					
				}
			}
			break;
		case -1://����
			if(MOUSE_LEFT_PRESSED && MOUSE_PRESSED_START==0){
				switch(MOUSE_POSITION){
				case 0://���y��
					md.showSmile(1);//���y���U
					break;

				case 1://�a�p��
				case 2://�@���
					switch(GAME_STATE){
					case 1://���\
						md.showSmile(3);//������
						break;	
					case 0://�@��
						md.showSmile(0);//���y
						break;
					case -1://����
						md.showSmile(4);//���y
						break;
					}
					break;	
				}				
			}
			break;
		}
		md.update();

	}
	
	//�]�w�ƹ��Ҧb�ϰ�
	public void setMousePosition(MouseEvent e){

		//���y��
		if((e.getX())>=md.SMILE_X && 
		   (e.getX())<(md.SMILE_X+md.SMILE_WIDTH) &&
		   (e.getY())>=md.SMILE_Y && 
		   (e.getY())<(md.SMILE_Y+md.SMILE_HEIGHT)){
		   	MOUSE_POSITION=0;

		//�a�p��
		}else if((e.getX())>=md.MAP_X && 
		   		 (e.getX())<(md.MAP_X+GRIDX*md.MAP_WIDTH) &&
		   		 (e.getY())>=md.MAP_Y && 
		   		 (e.getY())<(md.MAP_Y+GRIDY*md.MAP_HEIGHT)){
		   	MOUSE_POSITION=1;
		
		//��L��
		}else{
			MOUSE_POSITION=2;
		}
	
	}
	
	//�]�w�ƹ��b�a�ϤW�y��
	public void setMouseMapPosition(MouseEvent e){
		MOUSE_X=(e.getX()-md.MAP_X)/md.MAP_WIDTH;
		MOUSE_Y=(e.getY()-md.MAP_Y)/md.MAP_HEIGHT;
	}
	
	//��ܷƹ����U���a�p��
	public void showMouseDownMap(int x,int y){
		
		reloadMap();//��s�a�Ϲϥ�

		if(MOUSE_LEFT_PRESSED && MOUSE_RIGHT_PRESSED){

			
			for(int i=(x-1);i<=(x+1);i++){
				for(int j=(y-1);j<=(y+1);j++){
					if(i>=0 && i<GRIDX &&//����W�X�}�C
					   j>=0 && j<GRIDY &&
					   FACE[i][j]==1){		
						md.showMap(i,j,0);
					}
				}
			}				
			
		}else if(MOUSE_LEFT_PRESSED){
			if(x>=0 && x<GRIDX &&//����W�X�}�C
			   y>=0 && y<GRIDY &&
			   FACE[x][y]==1){		
				md.showMap(x,y,0);
			}		
		}		
	}
	
	//��s�a�Ϲϥ�
	public void reloadMap(){
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				if(FACE[i][j]==0){//�w����
					md.showMap(i,j,MAP[i][j]);
				}else{//������
					switch(FACE[i][j]){
					case 1://������
						md.showMap(i,j,12);
						break;
					case 2://���Ѥl
						md.showMap(i,j,13);
						break;
					case 3://�ݸ�
						md.showMap(i,j,14);
						break;							
					}
				}
			}					
		}		
	}	
	
	//�]�w�a�Ϫ����A
	public void setFace(int mode){
		if(MOUSE_X>=0 && MOUSE_X<GRIDX &&//����W�X�}�C
		   MOUSE_Y>=0 && MOUSE_Y<GRIDY){
			FACE[MOUSE_X][MOUSE_Y]=mode;
		}
	}
	
	//�M���ťհ�
	public void doClear(int x,int y){
		for(int i=(x-1);i<=(x+1);i++){
			for(int j=(y-1);j<=(y+1);j++){
				if(i>=0 && i<GRIDX &&
				   j>=0 && j<GRIDY &&	
				   FACE[i][j]==1 && //��ܥ�����
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
	
	//�ƹ������U���P�}���@���ˬd�۰�½�}
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
	
	//���a�p
	public void onMine(boolean right){
		if(right)//�O�_�b���W��
			MAP[MOUSE_X][MOUSE_Y]=11;//�a�p�[����
			
		GAME_RUN=-1;//�C������
		GAME_STATE=-1;//����
		
		if(timer!=null){
			timer.stop();							
			timer=null;								
		}
		
		for(int ii=0;ii<GRIDX;ii++){
			for(int jj=0;jj<GRIDY;jj++){
				if(FACE[ii][jj]==2 &&//�����Ѥl
				   MAP[ii][jj]!=9){//�D�a�p
					MAP[ii][jj]=10;
					FACE[ii][jj]=0;		
				}
				if(FACE[ii][jj]==2 &&//����Ѥl
				   MAP[ii][jj]==9)//�a�p
				   	FACE[ii][jj]=0;
				if(FACE[ii][jj]==1 &&//������
				   MAP[ii][jj]==9)		
					FACE[ii][jj]=0;
			}								
		}	
		md.showSmile(4);//���y
	}
	
	//�ˬd�O�_����
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

			GAME_RUN=-1;//�C������
			GAME_STATE=1;//���\
			timer.stop();
			md.showSmile(3);//������
			md.showCount(md.MCOUNT_X,md.MCOUNT_Y,0);
		}else{
			if(GAME_RUN==1)
				md.showSmile(0);//���y
			else{
				if(GAME_STATE==-1)	
					md.showSmile(4);//���y
			}
		}
	}
	
	//���m�C��
	public void reset(){
		for(int i=0;i<GRIDX;i++){
			for(int j=0;j<GRIDY;j++){
				FACE[i][j]=1;//�a�p���]�w(������)
				MAP[i][j]=0;//�a�p�]�w(�ť�)
			}				
		}	
		
		GAME_RUN=0;  //�C����l����
		GAME_STATE=0;//�@�몬�A	
		MOUSE_LEFT_PRESSED=false;
		MOUSE_RIGHT_PRESSED=false;
		UNDO=false;
		
		//�p�ɾ����m
		if(timer!=null)
			timer.stop();
		TIME_COUNT=0;//�ɶ��p�Ƴ]�w
		MMINES=MINES;//�аO�a�p�Ƴ]�w		
		reloadMap();
		md.showSmile(0);
		md.showCount(md.MCOUNT_X,md.MCOUNT_Y,MINES);
		md.showCount(md.COUNT_X,md.COUNT_Y,TIME_COUNT);
	}
}
