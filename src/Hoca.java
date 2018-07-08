import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.crypto.spec.PSource;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.sql.*;
import java.util.*;

public class Hoca extends JFrame implements ActionListener{
	Connection conn= null;
	JFrame jFrame;
	JLabel[] jLabels;
	JTextField[] jTextFields;
	public Hoca()
	{
		jFrame=new JFrame("HOCA EKLE");
		jFrame.setSize(260, 135);
		jFrame.setLayout(new FlowLayout());
		jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jLabels=new JLabel[2];
		jTextFields= new JTextField[2];
		String[] etiketler= {"Hoca Ýsmi: ", "Hoca Ünvaný: "};
		
		for(int i=0;i<2;i++)
		{
		jLabels[i]=new JLabel(etiketler[i]);
		jLabels[i].setPreferredSize(new Dimension(80, 25));
		jFrame.add(jLabels[i]);
		
		jTextFields[i]= new JTextField();
		jTextFields[i].setPreferredSize(new Dimension(150, 25));
		jFrame.add(jTextFields[i]);
		}
		
		JButton jButton=new JButton("EKLE");
		jButton.setPreferredSize(new Dimension(235, 25));
		jButton.addActionListener(this);
		jFrame.add(jButton);
		
		jFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
			ekle();	
	}
	
	public void baglan()
	{
		try {
			Class.forName("org.sqlite.JDBC");
			conn =DriverManager.getConnection("jdbc:sqlite:DersProgrami.db");
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this,"BAÐLANTI BAÞARISIZ");
		}
	}
	public void baglantiKes()
	{
		try {
			conn.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,"BAÐLANTI KESÝLEMEDÝ");
		}
	}
	
	public void ekle()
	{
		if(jTextFields[0].getText().length()>0&&jTextFields[1].getText().length()>0)
		{
		baglan();
		String sql= "insert into hocalar(hocaismi) values(?)";
		try 
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, jTextFields[1].getText()+" "+jTextFields[0].getText());
			ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "EKLEME BAÞARILI");
			jTextFields[0].setText("");
			jTextFields[1].setText("");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "EKLEME BAÞARISIZ");
		}
		baglantiKes();
		}
		else
			JOptionPane.showMessageDialog(this, "ALANLARI BOÞ BIRAKMAYINIZ!");
	}

}
