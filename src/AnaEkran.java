import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.sql.*;
import java.util.*;

public class AnaEkran extends JFrame implements ActionListener {

	Connection conn= null;
	JFrame jFrame;
	JPanel jPanel_tablo;
	JPanel[] jPanel_hucre;
	JLabel[] jLabels;
	public AnaEkran()
	{
		jFrame=new JFrame("DERS PROGRAMI");
		jFrame.setSize(985, 775);
		jFrame.setLayout(new FlowLayout());
		jFrame.setDefaultCloseOperation(3);
		
		jPanel_tablo= new JPanel();
		jPanel_tablo.setPreferredSize(new Dimension(960, 675));
		jPanel_tablo.setLayout(new GridLayout(9, 8));
		jPanel_tablo.setBorder(BorderFactory.createLineBorder(Color.black));
		
		jLabels= new JLabel[3];
		jPanel_hucre=new JPanel[72];
		
		for(int i=0;i<72;i++)
		{
			jPanel_hucre[i]=new JPanel();
			jPanel_hucre[i].setPreferredSize(new Dimension(120, 75));
			jPanel_hucre[i].setLayout(new FlowLayout());
			jPanel_hucre[i].setBorder(BorderFactory.createLineBorder(Color.black));		
			jPanel_tablo.add(jPanel_hucre[i]);
		}
		
		jFrame.add(jPanel_tablo);
		gunSaatDoldur();
		String[] butonlar= {"HOCA EKLE", "DERS EKLE", "EÞLEÞTÝRME OLUÞTUR","EÞLEÞTÝRMELERÝ SÝL"};
		for(int i=0;i<4;i++)
		{
		JButton jButton=new JButton(butonlar[i]);
		jButton.setPreferredSize(new Dimension(236, 45));
		jButton.addActionListener(this);
		jButton.setActionCommand(i+"");
		jFrame.add(jButton);
		}
		
		tabloDoldur();
		jFrame.setVisible(true);
	}
	public static void main(String[] args) {
	new AnaEkran();
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("0"))
		{new Hoca();}
		else if(e.getActionCommand().equals("1"))
		{new Ders();}
		else if(e.getActionCommand().equals("2"))
		{new Eslestirme(this);}	
		else 
		{sil();}
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
	
	public void gunSaatDoldur() 
	{
		baglan();
		try 
		{
			String sql="select isim from gunler";
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int i=1;
			while (rs.next()&&i<=7) 
			{
				JLabel jLabel=new JLabel(rs.getString("isim"));
				jLabel.setFont(new Font("Serif", Font.BOLD,20));
				jLabel.setPreferredSize(new Dimension(110, 57));
				jLabel.setHorizontalAlignment(SwingConstants.CENTER);
				jPanel_hucre[i].add(jLabel);
				i++;
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(this,"GÜNLERÝ DOLDURMA BAÞARISIZ");
		}
		
		try 
		{
			String sql="select saat from saatler";
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int i=8;
			while (rs.next()&&i<=72) 
			{
				JLabel jLabel=new JLabel(rs.getString("saat"));
				jLabel.setFont(new Font("Serif", Font.BOLD,20));
				jLabel.setPreferredSize(new Dimension(110, 57));
				jLabel.setHorizontalAlignment(SwingConstants.CENTER);
				jPanel_hucre[i].add(jLabel);
				i+=8;
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(this,"SAATLERÝ DOLDURMA BAÞARISIZ");
		}
		baglantiKes();
	}
	
	public void tabloDoldur()
	{
		baglan();
		try 
		{
			String sql="select * from eslestirme";
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				String ders="select dersadi from dersler where id=?";
				ps=conn.prepareStatement(ders);
				ps.setInt(1, rs.getInt("dersid"));
				ResultSet rsDers=ps.executeQuery();
				jLabels[0]=new JLabel(rsDers.getString("dersadi"));
				jLabels[0].setFont(new Font("Serif", Font.PLAIN,12));
				jLabels[0].setPreferredSize(new Dimension(110, 17));
				jLabels[0].setHorizontalAlignment(SwingConstants.CENTER);

				String hoca="select hocaismi from hocalar where id=?";
				ps=conn.prepareStatement(hoca);
				ps.setInt(1, rs.getInt("hocaid"));
				ResultSet rsHoca=ps.executeQuery();
				jLabels[1]=new JLabel(rsHoca.getString("hocaismi"));
				jLabels[1].setFont(new Font("Serif", Font.PLAIN,12));
				jLabels[1].setPreferredSize(new Dimension(110, 17));
				jLabels[1].setHorizontalAlignment(SwingConstants.CENTER);
				
				jLabels[2]=new JLabel();
				if(rs.getInt("tip")==1)
					jLabels[2].setText("Teorik");
				else if(rs.getInt("tip")==2)
					jLabels[2].setText("Uygulama");
				jLabels[2].setFont(new Font("Serif", Font.PLAIN,12));
				jLabels[2].setPreferredSize(new Dimension(110, 15));
				jLabels[2].setHorizontalAlignment(SwingConstants.CENTER);

				int konum= (rs.getInt("saatid")*8)+rs.getInt("gunid");
				jPanel_hucre[konum].add(jLabels[0]);
				jPanel_hucre[konum].add(jLabels[1]);
				jPanel_hucre[konum].add(jLabels[2]);
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(this,"EÞLEÞTÝRMELERÝ DOLDURMA BAÞARISIZ");
		}
		baglantiKes();	
	}
	public void sil()
	{
		baglan();
		try 
		{
			String sql="delete from eslestirme";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeUpdate();
			for(int i=8;i<72;i++)
			{
				if(i%8!=0)
				{
					jPanel_hucre[i].removeAll();
				}
			}
			tabloDoldur();
			jPanel_tablo.updateUI();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(this,"SÝLME BAÞARISIZ");
		}
		baglantiKes();
	}
}
