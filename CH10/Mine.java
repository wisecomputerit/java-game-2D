import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mine extends JFrame{
	
	static SingleMine sm;
	
	//�غc�l
	public Mine(){
		super();		
	}	
	
	//�{���i�J�I
	public static void main(String[] args){
		int gridx=20;
		int gridy=16;
		int mines=40;
		JFrame.setDefaultLookAndFeelDecorated(true);//�]�w�зǤ���
		Mine m=new Mine();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�w�]���������覡
		m.setTitle("��a�pJava��");
		m.setSize(SingleMine.getDimension(gridx,gridy));//�]�w�����j�p
		m.setLocation(300,200);		
		
		m.setVisible(true);
		m.setResizable(false);
		
		sm=new SingleMine(gridx,gridy,mines,m.getContentPane());//�إ�SingleMine����

		m.getContentPane().addMouseListener(sm);//�[�J�ƹ���ť�ƥ�
		m.getContentPane().addMouseMotionListener(sm);//�[�J�ƹ���ť�ƥ�
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		if(sm!=null)
			sm.md.update();	
	}
}