package com.boz.awt;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Windows {
	public static void main(String[] args) {
		JFrame frm = new JFrame();
		frm.setBounds(300, 300, 500, 500);
		frm.setTitle("我的窗口");
		
		Container c = frm.getContentPane();  // frm中包含一个内容窗格， 需要获取内容窗格，再设置背景颜色，直接设置frm的背景颜色会被内容窗格挡住
		c.setBackground(Color.red);
		
		frm.setLayout(null); // 如过不设置为null默认，按钮会充满整个内容框，挡住背景颜色
		
		//button
		JButton button = new JButton();
		button.setBounds(350, 400, 100, 40);
		button.setText("退出");
		
		frm.add(button); // 添加了按钮会把背景颜色挡住，可以通过面板来调节
		
		
		//text
		final JTextField jtf = new JTextField("该文本不可编辑", 30); 		// 创建文本行组件, 30 列
		JPasswordField jpf = new JPasswordField("密码文本", 30);   // 创建密码文本行组件, 30 列
		JTextArea jta = new JTextArea("您好", 10, 30);               // 创建文本区组件,10行，30列
		JScrollPane jsp = new JScrollPane(jta);                    // 创建滚动窗格，其显示内容是文本区对象
		
        jta.setLineWrap(true); // 设置自动换行
        jpf.setBounds(20, 10, 140, 20);
        jtf.setBounds(20, 40, 140, 20);
        jsp.setBounds(20, 70, 160, 100);

        // 把组件添加进窗口f中
        frm.add(jpf);
        frm.add(jtf);
        frm.add(jsp);
        
        frm.addWindowListener(new MyWin());  
        
		 //添加一个活动监听  
		button.addActionListener(new ActionListener() {  
             
           @Override  
           public void actionPerformed(ActionEvent e) {  
               // TODO Auto-generated method stub  
               System.out.println("退出, 按钮干的");
               System.out.println(jtf.getText());
//               System.exit(0);  
           }  
       });          
        
        
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
		
	}
}

class MyWin extends WindowAdapter{
    @Override  
    public void windowClosing(WindowEvent e) {  
//        System.out.println("Window closing"+e.toString());  
        System.out.println("我关了");  
        System.exit(0);  
    }  
    @Override  
    public void windowActivated(WindowEvent e) {  
        //每次获得焦点 就会触发  
        System.out.println("我活了");    
    }  
    @Override  
    public void windowOpened(WindowEvent e) {  
        System.out.println("我开了");  
    }  
}
