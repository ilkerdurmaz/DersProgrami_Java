import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.sql.*;
import java.util.*;

public class Ders extends JFrame implements ActionListener{
	Connection conn= null;
	JFrame jFrame;
	JLabel[] jLabels;
	JTextArea jTextArea;
	JTextField[] jTextFields;
	JPanel jPanel;
	public Ders()
	{
		jFrame=new JFrame("EKLE");
		jFrame.setSize(175, 345);
		jFrame.setLayout(new FlowLayout());
		jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jPanel=new JPanel();
		jPanel.setPreferredSize(new Dimension(150, 105));
		jPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		jFrame.add(jPanel);
		
		jLabels=new JLabel[6];
		jTextFields=new JTextField[5];
		
		JLabel adi=new JLabel("Ders Adý: ");
		adi.setPreferredSize(new Dimension(140, 25));
		jPanel.add(adi);
		
		jTextArea=new JTextArea();
		jTextArea.setPreferredSize(new Dimension(140, 65));
		jTextArea.setLineWrap(true);
		jPanel.add(jTextArea);
		
		jPanel=new JPanel();
		jPanel.setPreferredSize(new Dimension(150, 155));
		jPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		jFrame.add(jPanel);
		
		JLabel kodu=new JLabel("Kodu: ");
		kodu.setPreferredSize(new Dimension(35, 25));
		jPanel.add(kodu);
		
		jTextFields[0]=new JTextField();
		jTextFields[0].setPreferredSize(new Dimension(100, 25));
		jPanel.add(jTextFields[0]);
		
		String[] etiketler= {"Teorik Saati: ","Uygulama Saati: ","Sýnýf: ","Örgün: "};
		for (int i = 1;i<5;i++) 
		{
			jLabels[i]=new JLabel(etiketler[i-1]);
			jLabels[i].setPreferredSize(new Dimension(95, 25));

			jTextFields[i]=new JTextField();
			jTextFields[i].setPreferredSize(new Dimension(40, 25));
			
			jPanel.add(jLabels[i]);
			jPanel.add(jTextFields[i]);
		}
		
		JButton jButton=new JButton("DERS EKLE");
		jButton.addActionListener(this);
		jButton.setPreferredSize(new Dimension(150, 25));
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
		baglan();
		String sql="insert into dersler(dersadi,kodu,teoriksaati,uygulamasaati,sinif,orgun) values(?,?,?,?,?,?)";
		try 
		{
			PreparedStatement ps= conn.prepareStatement(sql);
			ps.setString(1, jTextArea.getText());
			ps.setString(2, jTextFields[0].getText());
			ps.setInt(3, Integer.parseInt(jTextFields[1].getText()));
			ps.setInt(4, Integer.parseInt(jTextFields[2].getText()));
			ps.setInt(5, Integer.parseInt(jTextFields[3].getText()));
			ps.setInt(6, Integer.parseInt(jTextFields[4].getText()));
			ps.executeUpdate();
			JOptionPane.showMessageDialog(this,"EKLEME BAÞARILI");
			
			jTextArea.setText("");
			for(int i=0;i<5;i++)
			{
				jTextFields[i].setText("");
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(this,"EKLEME BAÞARISIZ");
		}
		baglantiKes();
	}
}
