/*�{���GTetirs.java
 *�����G�Xù������������	    
 */
 
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class Tetirs extends JFrame implements KeyListener,
											  MouseMotionListener,
											  MouseWheelListener,
											  MouseListener{
	
	static SingleTetirs ST;
	static int GRIDX;
	static int GRIDY;
	public static void main(String[] args){
		
		JFrame.setDefaultLookAndFeelDecorated(true);//�]�w�зǤ���
		Tetirs myTetirs=new Tetirs();//�إ߹������
		myTetirs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myTetirs.setTitle("�Xù�����");
		myTetirs.setBounds(0,0,500,525);//�]�w������m�j�p
		myTetirs.setVisible(true);//��ܵ���
		myTetirs.setResizable(false);		
		//ST=new SingleTetirs(myTetirs.getContentPane().getGraphics(),
		//					5,5,14,24,20,1,6,true,myTetirs.getComponent(0));
		ST=new SingleTetirs(myTetirs.getContentPane().getGraphics(),
							5,5,14,24,20,1,6,true,myTetirs.getContentPane());		
		myTetirs.getContentPane().addMouseMotionListener(myTetirs);//�[�J�ƹ��ƥ�
		myTetirs.getContentPane().addMouseListener(myTetirs);//�[�J�ƹ��ƥ�
		myTetirs.getContentPane().addMouseWheelListener(myTetirs);//�[�J�ƹ��ƥ�
		myTetirs.addKeyListener(myTetirs);//�[�J��L�ƥ�
				
	}
	
	public void paint(Graphics g){
		super.paint(g);
		if(ST!=null)
			ST.paint();
	}
	

	
	//�ƹ�����
	public void mouseClicked(MouseEvent e){
		switch(e.getButton()){
		case MouseEvent.BUTTON1:
			ST.keyPress(KeyEvent.VK_UP);
			break;	
		case MouseEvent.BUTTON2:
			break;
		case MouseEvent.BUTTON3:
			ST.keyPress(KeyEvent.VK_SPACE);
			break;
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	
	//�ƹ��u���ƥ�
	public void mouseWheelMoved(MouseWheelEvent e){
		switch(e.getWheelRotation()){
		case -1:		
			ST.keyPress(KeyEvent.VK_UP);
			break;
		case 1:		
			ST.keyPress(KeyEvent.VK_DOWN);
			break;		
		}
	}

	//�ƹ��ƥ�
	public void mouseMoved(MouseEvent e){
		int x=e.getX();
		int y=e.getY();
		if(x>5 && x<285 &&//�]�wX�b��� 
		   y>5 && y<485 &&//�]�wY�b��� 
		   ((x-5)/20)!=(ST.NOW_BRICK_X+2)){
			
			if(((x-5)/20)>(ST.NOW_BRICK_X+2)){
				ST.keyPress(KeyEvent.VK_RIGHT);
				System.out.println("big");
			}
			else if((((x-5)/20)<(ST.NOW_BRICK_X+2))){
				ST.keyPress(KeyEvent.VK_LEFT);					
				System.out.println("small");
			}
			else{
				System.out.println("equal");
			}
		}
	}
	public void mouseDragged(MouseEvent e){}


	//��L���U�ƥ�
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			ST.DownThread.stop();
		ST.keyPress(e.getKeyCode());
		if(e.getKeyCode()==KeyEvent.VK_DOWN){
			ST.DownThread=new Thread(ST);
			ST.DownThread.start();
		}
			
	}
	public void keyReleased(KeyEvent e){}	
	public void keyTyped(KeyEvent e){}	
}