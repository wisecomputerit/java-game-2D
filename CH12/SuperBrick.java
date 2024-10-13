/*�{���GSuperBrick.java
 *�����G�H�U���j���ո�����O�A�]�t�j���հ}�C��ơB�j���ձ���覡
 *      �Ҧ��j���եHSuperBrick����¦���O�l�ͦU���O�A�䤤�U�j����
 *		���������٤��G�V�Υ|�V�C
 */
 
//�Ҧ��j������������¦���O
public class SuperBrick{
	//�j���ո��
	public int BRICK_ARRAY[][][];//�����j���ո�ư}�C
	public SuperBrick(){
		//�إ߬����j���ժ��}�C�ñN��ƲM��
		BRICK_ARRAY=new int[4][5][5];
		for(int i=0;i<4;i++){
			for(int j=0;j<5;j++){
				for(int k=0;k<5;k++){
					BRICK_ARRAY[i][j][k]=0;
				}			
			}			
		}		
	}
	
	//����j����(���ɰw����_�u�A�Υ|�Ӥ�V�Ҥ��P���j���ղ�)
	protected void turnBrick_Four(){
		
		for(int i=1;i<=3;i++){
			for(int x=0;x<=4;x++){
				for(int y=0;y<=4;y++){
					BRICK_ARRAY[i][x][y]=BRICK_ARRAY[i-1][4-y][x];
				}				
			}
		}
	}

	//����j����(���ɰw����_�u�A�Ψ�Ӥ�V���j���ղ�)
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
	
	//�]�w�j���ո��
	private void setBrickArray(){}
}

//�j�������O_�Цr��
class Brick_BB extends SuperBrick{
	//�غc�l
	public Brick_BB(){
		super();
		setBrickArray();//�]�w�j���ո��		
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		for(int i=0;i<=3;i++){
			BRICK_ARRAY[i][3][1]=1;//�Цr��
			BRICK_ARRAY[i][3][2]=1;
			BRICK_ARRAY[i][2][1]=1;
			BRICK_ARRAY[i][2][2]=1;		
		}
	}	
}
	
//�j�������O_�˱��
class Brick_UT extends SuperBrick{
	
	//�غc�l
	public Brick_UT(){
		super();
		setBrickArray();//�]�w�j���ո��		
		turnBrick_Four();//����j����
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		BRICK_ARRAY[0][2][1]=1;//�˱��
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//�j�������O_�ڧ�
class Brick_L extends SuperBrick{
	//�غc�l
	public Brick_L(){
		super();
		setBrickArray();//�]�w�j���ո��		
		turnBrick_Four();//����j����
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		BRICK_ARRAY[0][1][1]=1;//�ڧ�
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//�j�������O_�ˢڧ�
class Brick_UL extends SuperBrick{
	//�غc�l
	public Brick_UL(){
		super();
		setBrickArray();//�]�w�j���ո��		
		turnBrick_Four();//����j����
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		BRICK_ARRAY[0][3][1]=1;//�ˢڧ�
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//�j�������O_�@�r��
class Brick_I extends SuperBrick{
	//�غc�l
	public Brick_I(){
		super();
		setBrickArray();//�]�w�j���ո��		
		turnBrick_Two();//����j����
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		BRICK_ARRAY[0][1][2]=1;//�@�r��
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;
		BRICK_ARRAY[0][4][2]=1;		
	}
}

//�j�������O_���
class Brick_Z extends SuperBrick{
	//�غc�l
	public Brick_Z(){
		super();
		setBrickArray();//�]�w�j���ո��		
		turnBrick_Two();//����j����
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		BRICK_ARRAY[0][1][1]=1;//���
		BRICK_ARRAY[0][2][1]=1;
		BRICK_ARRAY[0][2][2]=1;
		BRICK_ARRAY[0][3][2]=1;		
	}
}

//�j�������O_�ˢ��
class Brick_UZ extends SuperBrick{
	//�غc�l
	public Brick_UZ(){
		super();
		setBrickArray();//�]�w�j���ո��		
		turnBrick_Two();//����j����
	}
	
	//�]�w�j���ո��
	private void setBrickArray(){
		BRICK_ARRAY[0][2][1]=1;//�ˢ��
		BRICK_ARRAY[0][3][1]=1;
		BRICK_ARRAY[0][1][2]=1;
		BRICK_ARRAY[0][2][2]=1;		
	}
}