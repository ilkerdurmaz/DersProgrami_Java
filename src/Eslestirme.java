import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.sql.*;
import java.util.*;

public class Eslestirme extends JFrame implements ActionListener{
	Connection conn= null;
	JFrame jFrame;
	JComboBox[] jComboBoxs;
	JRadioButton[] jRadioButtons;
	ArrayList[] arrayLists;
	int tip;
	AnaEkran ana;
	public Eslestirme(AnaEkran x)
	{
		ana=x;
		jFrame=new JFrame("EÞLEÞTÝRME");
		jFrame.setSize(325, 235);
		jFrame.setLayout(new FlowLayout());
		jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                ana.tabloDoldur();
                ana.jPanel_tablo.updateUI();
                e.getWindow().dispose();
            }
        });
		
		
		
		
		jComboBoxs=new JComboBox[4];
		jRadioButtons= new JRadioButton[2];
		arrayLists=new ArrayList[4];
		
		
		String[] etiketler= {"Ders: ","Hoca: ","Gün: ","Saat: ","Tip: "};
		for(int i=0;i<4;i++)
		{
			JLabel jLabel=new JLabel(etiketler[i]);
			jLabel.setPreferredSize(new Dimension(40, 25));
			jFrame.add(jLabel);
			
			jComboBoxs[i]=new JComboBox<String>();
			jComboBoxs[i].setPreferredSize(new Dimension(250, 25));
			jFrame.add(jComboBoxs[i]);
		}
		doldur();
		JLabel jLabel=new JLabel(etiketler[4]);
		jLabel.setPreferredSize(new Dimension(40, 25));
		jFrame.add(jLabel);
		
		String[] tipAdi= {"Uygulama","Teorik"};
		ButtonGroup bGroup= new ButtonGroup();
		for(int i=0;i<2;i++)
		{
			jRadioButtons[i]=new JRadioButton(tipAdi[i]);
			jRadioButtons[i].setPreferredSize(new Dimension(100, 25));
			bGroup.add(jRadioButtons[i]);
			jRadioButtons[i].addActionListener(this);
			jRadioButtons[i].setActionCommand(tipAdi[i]);
			jFrame.add(jRadioButtons[i]);
		}
		jRadioButtons[1].setSelected(true);
		tip=1;
		
		JButton jButton=new JButton("EÞLEÞTÝRME EKLE");
		jButton.setPreferredSize(new Dimension(300,35));
		jButton.addActionListener(this);
		jButton.setActionCommand("ekle");
		jFrame.add(jButton);
		
		
		jFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Uygulama"))
		{tip=2;}
		else if(e.getActionCommand().equals("Teorik"))
		{tip=1;}
		else {ekle();}
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
	
	public void doldur()
	{
		baglan();
		try 
		{
		String sql="select id,dersadi from dersler";
		PreparedStatement ps=conn.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		arrayLists[0]=new ArrayList<String>();
		while(rs.next())
		{
			jComboBoxs[0].addItem(rs.getString("dersadi"));
			arrayLists[0].add(rs.getInt("id"));
		}
		
		sql="select id,hocaismi from hocalar";
		ps=conn.prepareStatement(sql);
		rs=ps.executeQuery();
		arrayLists[1]=new ArrayList<String>();
		while(rs.next())
		{
			jComboBoxs[1].addItem(rs.getString("hocaismi"));
			arrayLists[1].add(rs.getInt("id"));
		}
		
		sql="select id,isim from gunler";
		ps=conn.prepareStatement(sql);
		rs=ps.executeQuery();
		arrayLists[2]=new ArrayList<String>();
		while(rs.next())
		{
			jComboBoxs[2].addItem(rs.getString("isim"));
			arrayLists[2].add(rs.getInt("id"));
		}
		
		sql="select id,saat from saatler";
		ps=conn.prepareStatement(sql);
		rs=ps.executeQuery();
		arrayLists[3]=new ArrayList<String>();
		while(rs.next())
		{
			jComboBoxs[3].addItem(rs.getString("saat"));
			arrayLists[3].add(rs.getInt("id"));
		}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,"DOLDURMA BAÞARISIZ");
		}
		baglantiKes();
	}
	
	public void ekle()
	{
		baglan();
		try 
		{
			try 
			{
			String kontrol="select id from eslestirme where gunid=? and saatid=?";
			PreparedStatement ps=conn.prepareStatement(kontrol);
			ps.setInt(1, (int) arrayLists[2].get(jComboBoxs[2].getSelectedIndex()));
			ps.setInt(2, (int) arrayLists[3].get(jComboBoxs[3].getSelectedIndex()));
			ResultSet rs=ps.executeQuery();
			if(rs.getInt("id")>-1)
			JOptionPane.showMessageDialog(this, "BELÝRTÝLEN GÜN VE SAATTE BAÞKA BÝR DERS BULUNMAKTADIR!");
			} 
			catch (Exception e) {
				String sql="insert into eslestirme(dersid,hocaid,gunid,saatid,tip) values (?,?,?,?,?)";
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setInt(1, (int) arrayLists[0].get(jComboBoxs[0].getSelectedIndex()));
				ps.setInt(2, (int) arrayLists[1].get(jComboBoxs[1].getSelectedIndex()));
				ps.setInt(3, (int) arrayLists[2].get(jComboBoxs[2].getSelectedIndex()));
				ps.setInt(4, (int) arrayLists[3].get(jComboBoxs[3].getSelectedIndex()));
				ps.setInt(5, tip);
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "EÞLEÞTÝRME BAÞARILI!");
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "EÞLEÞTÝRME BAÞARISIZ!");
		}
		baglantiKes();
	}
}
