package com.onlypxz.plugins.autofindviews.views;

import com.onlypxz.plugins.autofindviews.utils.AutoEntityFile;
import com.onlypxz.plugins.autofindviews.utils.StringUtils;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class JsonToEntityV2 extends BaseFrame {

	private JPanel contentPane;
	private JTextField clsNameTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JsonToEntityV2 frame = new JsonToEntityV2("Json To Entity Tools");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JsonToEntityV2(String title) {
		setTitle(title);
		setResizable(false);
		//若想直接运行，需要把images 放到src 目录下
		setIconImage(getImageIcon("/images/tools_icon.jpg").getImage());
		//不能用EXIT_ON_CLOSE as 会退出
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(534, 416);
//		contentPane = new JPanel();
		contentPane = createImgBgPanel(getImageIcon("/images/main_bg.jpg"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel label = new JLabel("版本");
		label.setBounds(56, 10, 54, 15);
		contentPane.add(label);
		
		final JRadioButton rdbtnV_1 = new JRadioButton("Java");
		rdbtnV_1.setBounds(116, 6, 80, 23);
//		
		
		JRadioButton rdbtnV_2 = new JRadioButton("Kotlin");
		rdbtnV_2.setSelected(true);
		rdbtnV_2.setBounds(210, 6, 80, 23);
		
		ButtonGroup levelBtnGroup=new ButtonGroup();
		levelBtnGroup.add(rdbtnV_1);
		levelBtnGroup.add(rdbtnV_2);
		contentPane.add(rdbtnV_1);
		contentPane.add(rdbtnV_2);
		//设置radiobutton 背景透明
		rdbtnV_1.setContentAreaFilled(false);
		rdbtnV_1.setFocusPainted(false);
		rdbtnV_2.setFocusPainted(false);
		rdbtnV_2.setContentAreaFilled(false);
		
		JLabel label_1 = new JLabel("类名");
		label_1.setBounds(56, 47, 54, 15);
		contentPane.add(label_1);
		
		clsNameTextField = new JTextField();
		clsNameTextField.setBounds(116, 44, 121, 27);
		//textField.setBorder(new MatteBorder(1, 1, 1, 1,Color.BLUE));
		contentPane.add(clsNameTextField);
		clsNameTextField.setColumns(10);
		
		JLabel lblJson = new JLabel("json数据");
		lblJson.setBounds(56, 102, 54, 15);
		contentPane.add(lblJson);
		
		final JEditorPane jsonEditorPane = new JEditorPane();
		JScrollPane jsonScrollPane = new JScrollPane(jsonEditorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsonScrollPane.setBounds(116, 82, 363, 54);
		contentPane.add(jsonScrollPane);
		
		
		
		JLabel label_2 = new JLabel("类内容");
		label_2.setBounds(56, 229, 54, 15);
		contentPane.add(label_2);
		
		final JEditorPane contenteditorPane = new JEditorPane();
		JScrollPane scrollPane_1 = new JScrollPane(contenteditorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(116, 162, 363, 148);
		contentPane.add(scrollPane_1);
		
		JButton button = new JButton("立即生成");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String className=clsNameTextField.getText();
				String json=jsonEditorPane.getText();
				String content="";
				if(rdbtnV_1.isSelected())
				{
					content= AutoEntityFile.getEntityContent(json, className);
					
				}else {
					AutoEntityFile.isInnerClass=null;
					content=AutoEntityFile.getEntityContentKotlin(json, className);
				}
				StringUtils.setSystemClipboard(content);
				contenteditorPane.setText(content);
			}
		});
		button.setBounds(210, 332, 161, 35);
		button.setFocusPainted(false);
		contentPane.add(button);
		contentPane.add(jsonScrollPane);
	}
}
