package com.onlypxz.plugins.autofindviews.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.intellij.psi.xml.XmlFile;
import com.onlypxz.plugins.autofindviews.entity.XmlElement;

public class ConfigFrame{

	public enum AutoType{
		Field,Fragment,ViewHolder
	}
    public static AutoType autoType;
	private static JFrame jf;
	private static List<XmlElement> xmlElements;
	private static JTextArea area;
	private static  JRadioButton[] autoTypeRadioBtns;

	private static JButton jbButton;

	public static void createFrame(XmlFile selectedFile)
	{
		xmlElements=Utils.getXmlElementsForIds(selectedFile);
		if(jf==null)
		{
			jf = new JFrame();
			jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jf.setLocation(getWindowScreenSize().width / 2 - 200, getWindowScreenSize().height / 2 - 150);
			jf.setSize(500, 420);
			
			jf.setLayout(new BorderLayout());
			
			//North 
			JPanel jp=new JPanel(new FlowLayout(FlowLayout.CENTER));
			jp.add(new Label("代码位置："));

			ButtonGroup radioBtnGroup=new ButtonGroup();
			JRadioButton buttonField=new JRadioButton("Feild",true);
			JRadioButton buttonFragment=new JRadioButton("Fragment");
			JRadioButton buttonViewHolder=new JRadioButton("ViewHolder");
			autoTypeRadioBtns=new JRadioButton[]{buttonField,buttonFragment,buttonViewHolder};
			buttonField.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(buttonField.isSelected())
					{
						ConfigFrame.autoType=AutoType.Field;
					}
				}
			});

			buttonFragment.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(buttonFragment.isSelected())
					{
						ConfigFrame.autoType=AutoType.Fragment;
					}
				}
			});
			buttonViewHolder.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(buttonViewHolder.isSelected())
					{
						ConfigFrame.autoType=AutoType.ViewHolder;
					}
				}
			});
			//将三个加入一组
			radioBtnGroup.add(buttonField);
			radioBtnGroup.add(buttonFragment);
			radioBtnGroup.add(buttonViewHolder);

			jp.add(buttonField);
			jp.add(buttonFragment);
			jp.add(buttonViewHolder);
			//将北边的panel加入到窗体中
			jf.add(jp, BorderLayout.NORTH);

			
			//中间显示内容区域
			area=new JTextArea();
			JScrollPane jsp=new JScrollPane(area);
			area.setMargin(new Insets(5, 5, 5, 5));
			jf.add(jsp, BorderLayout.CENTER);
			
			//将南边按钮加入到窗体中
			jbButton=new JButton("开始生成");
			jf.add(jbButton, BorderLayout.SOUTH);
			jbButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					area.setText(Utils.createFoundCode(xmlElements,ConfigFrame.autoType));
				}
			});
		}
		jf.setTitle("自动生成findViewById代码--"+selectedFile.getName());
		area.setText("");
		autoTypeRadioBtns[AutoType.valueOf(ConfigFrame.autoType.name()).ordinal()].setSelected(true);
		jf.setVisible(true);
		
		
	}
	private static Dimension getWindowScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
}