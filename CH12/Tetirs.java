/*程式：Tetirs.java
 *說明：俄羅斯方塊控制視窗	    
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
		
		JFrame.setDefaultLookAndFeelDecorated(true);//設定標準介面
		Tetirs myTetirs=new Tetirs();//建立實體視窗
		myTetirs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myTetirs.setTitle("俄羅斯方塊");
		myTetirs.setBounds(0,0,500,525);//設定視窗位置大小
		myTetirs.setVisible(true);//顯示視窗
		myTetirs.setResizable(false);		
		//ST=new SingleTetirs(myTetirs.getContentPane().getGraphics(),
		//					5,5,14,24,20,1,6,true,myTetirs.getComponent(0));
		ST=new SingleTetirs(myTetirs.getContentPane().getGraphics(),
							5,5,14,24,20,1,6,true,myTetirs.getContentPane());		
		myTetirs.getContentPane().addMouseMotionListener(myTetirs);//加入滑鼠事件
		myTetirs.getContentPane().addMouseListener(myTetirs);//加入滑鼠事件
		myTetirs.getContentPane().addMouseWheelListener(myTetirs);//加入滑鼠事件
		myTetirs.addKeyListener(myTetirs);//加入鍵盤事件
				
	}
	
	public void paint(Graphics g){
		super.paint(g);
		if(ST!=null)
			ST.paint();
	}
	

	
	//滑鼠按鍵
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
	
	//滑鼠滾輪事件
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

	//滑鼠事件
	public void mouseMoved(MouseEvent e){
		int x=e.getX();
		int y=e.getY();
		if(x>5 && x<285 &&//設定X軸邊界 
		   y>5 && y<485 &&//設定Y軸邊界 
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


	//鍵盤按下事件
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