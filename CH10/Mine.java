import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mine extends JFrame{
	
	static SingleMine sm;
	
	//建構子
	public Mine(){
		super();		
	}	
	
	//程式進入點
	public static void main(String[] args){
		int gridx=20;
		int gridy=16;
		int mines=40;
		JFrame.setDefaultLookAndFeelDecorated(true);//設定標準介面
		Mine m=new Mine();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//預設視窗關閉方式
		m.setTitle("踩地雷Java版");
		m.setSize(SingleMine.getDimension(gridx,gridy));//設定視窗大小
		m.setLocation(300,200);		
		
		m.setVisible(true);
		m.setResizable(false);
		
		sm=new SingleMine(gridx,gridy,mines,m.getContentPane());//建立SingleMine物件

		m.getContentPane().addMouseListener(sm);//加入滑鼠監聽事件
		m.getContentPane().addMouseMotionListener(sm);//加入滑鼠監聽事件
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		if(sm!=null)
			sm.md.update();	
	}
}