import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.proteanit.sql.DbUtils;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainProg extends JFrame{

	private JTable table_bony;
	private JTable table_bramka;
	private String ImieNazwisko;

	private JFrame frame;
	Connection connection=null;
	Connection connection2=null;
	private JTextField Imie_nazwisko;
	private JTextField HacoSoftnumber;
	private JTextField txtImieINazwisko;
	private JTextField txtNrKarty;
	private JTextField txtData;		
	private JTable table_1;
	private JTextField Dataa;
	private String Id_karty = "";



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainProg window = new MainProg();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainProg() {
		initialize();
		connection = RCPdatabaseConnection.dbConnector("tosia", "1234","fatdb"); // test , fatdb
		connection2 =RCPdatabaseConnection.dbConnector("tosia", "1234","fat"); //proba_fat
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 718, 328);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Imie_nazwisko = new JTextField();
		Imie_nazwisko.setEditable(false);
		Imie_nazwisko.setBounds(10, 31, 160, 20);
		frame.getContentPane().add(Imie_nazwisko);
		Imie_nazwisko.setColumns(10);
		
		HacoSoftnumber = new JTextField();
		HacoSoftnumber.setColumns(10);
		HacoSoftnumber.setBounds(10, 76, 120, 20);
		frame.getContentPane().add(HacoSoftnumber);
		
		//table.getModel()
		
		
		JButton OK = new JButton("OK");
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(CheckDataa(Dataa.getText()) == true )
				{
					//if((!Nr_karty.getText().equals("") || !Imie_nazwisko.getText().equals("")) && !Dataa.getText().equals(""))
					if(!HacoSoftnumber.getText().equals("")  && !Dataa.getText().equals(""))
					{
						try {
							ShowTable(connection,connection2);
							ShowName(connection,connection2);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else
				JOptionPane.showMessageDialog(null, "zly format daty, poprawny : 2018-05-13 ( yyyy-mm-dd)");

			}
		});
		OK.setBounds(10, 168, 120, 23);
		frame.getContentPane().add(OK);
		
		JScrollPane Podglad_bony = new JScrollPane();
		Podglad_bony.setBounds(180, 11, 522, 91);
		frame.getContentPane().add(Podglad_bony);
		
		
		JScrollPane Podglad_bramka = new JScrollPane();
		Podglad_bramka = new JScrollPane();
		frame.getContentPane().add(Podglad_bramka);
		Podglad_bramka.setBounds(180, 107, 522, 86);
		
		JButton Odswiez_bramka = new JButton("odswiez");
		Odswiez_bramka.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ShowTable(connection,connection2);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		Odswiez_bramka.setBounds(619, 204, 83, 23);
		frame.getContentPane().add(Odswiez_bramka);
		
		JButton Dodaj_wejscie = new JButton("Dodaj wejscie");
		Dodaj_wejscie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String data1 = JOptionPane.showInputDialog("wpisz godzine WEJSCIA (np: 09:27:00");
				
				String data2 = Dataa.getText() +" "+ data1;
				System.out.println(data2);
				
				if(!data1.equals("")) 
				{
					if(data1.length() < 8) {
						 JOptionPane.showMessageDialog(null, "Dlugosc daty jest za krotka- niepowodzenie dodania");
					}
					else {
					
						try {
							AddWejscie( connection2,data1);
						 JOptionPane.showMessageDialog(null, "Dodano nowy rekord");
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}

			}
		});
		Dodaj_wejscie.setBounds(168, 204, 125, 23);
		frame.getContentPane().add(Dodaj_wejscie);
		
		JButton Zmien_wejscie = new JButton("Zmie\u0144 wej/wyj");
		Zmien_wejscie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
						try {
							
						    int confirmed = JOptionPane.showConfirmDialog(null, 
						            "Czy chcesz zmienic rekord z bazy danych?", "Wyjdz",
						            JOptionPane.YES_NO_OPTION);

						        if (confirmed == JOptionPane.YES_OPTION) 
						        {
								String	id = JOptionPane.showInputDialog("wpisz id");
								String godzina = JOptionPane.showInputDialog("wpisz godzine (np 09:12:30)");
								
									if(godzina.length() == 8 && CheckForColons(godzina)== true) {
										changeWej(connection2, id, godzina);
										 JOptionPane.showMessageDialog(null, "zmieniono REKORD " + id);
									}
									
									else 
										JOptionPane.showMessageDialog(null, "zly format godziny, poprawny: hh:mm:ss");
	
									 }
				
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				
			}
		});
		Zmien_wejscie.setBounds(301, 204, 130, 23);
		frame.getContentPane().add(Zmien_wejscie);
		
		JButton Usun_wejscie = new JButton("Usun wej/wyj");
		Usun_wejscie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
				    int confirmed = JOptionPane.showConfirmDialog(null, 
				            "Czy chcesz usunaæ rekord z bazy danych?", "Wyjdz",
				            JOptionPane.YES_NO_OPTION);

				        if (confirmed == JOptionPane.YES_OPTION)
				        {
						String	id = JOptionPane.showInputDialog("wpisz id");				
							deleteWej( connection2,  id);
							
					    JOptionPane.showMessageDialog(null, "USUNIETO REKORD " + id);
				        }
		
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		Usun_wejscie.setBounds(441, 204, 113, 23);
		frame.getContentPane().add(Usun_wejscie);
		
		JButton Dodaj_wyjscie = new JButton("Dodaj wyjscie");
		
		Dodaj_wyjscie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String data1 = JOptionPane.showInputDialog("wpisz godzine WYJSCIA (np: 09:27:00");	
				String data2 = Dataa.getText() +" "+ data1;
				
				if(data1.length() < 8)
				{
					 JOptionPane.showMessageDialog(null, "Dlugosc daty jest za krotka- niepowodzenie dodania");
				}
				else {
				
					try {
						AddWyjscie( connection2,data1);
					 JOptionPane.showMessageDialog(null, "Dodano nowy rekord");
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}

				
			}
		});
		Dodaj_wyjscie.setBounds(168, 235, 125, 23);
		frame.getContentPane().add(Dodaj_wyjscie);
		
		txtImieINazwisko = new JTextField();
		txtImieINazwisko.setText("Imie i nazwisko");
		txtImieINazwisko.setEditable(false);
		txtImieINazwisko.setBounds(10, 13, 102, 14);
		frame.getContentPane().add(txtImieINazwisko);
		txtImieINazwisko.setColumns(10);
		
		txtNrKarty = new JTextField();
		txtNrKarty.setText("Numer Haco");
		txtNrKarty.setEditable(false);
		txtNrKarty.setColumns(10);
		txtNrKarty.setBounds(10, 62, 86, 14);
		frame.getContentPane().add(txtNrKarty);
		
		txtData = new JTextField();
		txtData.setText("DATA");
		txtData.setEditable(false);
		txtData.setColumns(10);
		txtData.setBounds(10, 117, 86, 14);
		frame.getContentPane().add(txtData);
		
		table_1 = new JTable();
		table_1.setBounds(199, 136, 108, -124);
		frame.getContentPane().add(table_1);
		
		table_bony = new JTable();
		Podglad_bony.setViewportView(table_bony);
		
		table_bramka = new JTable();
		Podglad_bramka.setViewportView(table_bramka);
		
		frame.getContentPane().add(Podglad_bramka);
		
		Dataa = new JTextField();
		Dataa.setColumns(10);
		Dataa.setBounds(10, 132, 120, 20);
		frame.getContentPane().add(Dataa);
		
	}
	
	
	public void ShowTable(Connection connection, Connection connection2) throws SQLException
	{
		System.out.println("poszlo");
		
		try {
			String query0 = "select * from cards_name_surname_nrhacosoft where HacoSoftnumber = '"+HacoSoftnumber.getText()+"'"; // HACOSOCT NUMBER

			//String query0 = "select * from cards_name_surname_nrhacosoft where id_karty = '"+Nr_karty.getText()+"'";   // ID_KARTY
			PreparedStatement pst0=connection2.prepareStatement(query0);
			ResultSet rs0=pst0.executeQuery();

			if(rs0.next())
				Id_karty = rs0.getString(1);

			System.out.println("id_karty: " + Id_karty);
			System.out.println("data: " + Dataa.getText());



			pst0.close();
			rs0.close();
			
			// to query Dziala dla WSZYSTKICH mozliwych dat, zakomentowane w razie jakby ponizsze query sie nie sprawdzalo
			String query = "SELECT w.datum ,w.WERKBONNUMMER, BEGINTIJDH  , EINDTIJDH , w.STATUS \r\n" + 
					"FROM werkuren w  \r\n" + 
					"left Join werkbon r  on w.WERKBONNUMMER=r.WERKBONNUMMER \r\n" + 
					"where (w.werknemer= '"+HacoSoftnumber.getText()+"' AND w.datum = '"+Dataa.getText()+"') ORDER BY datum";
			
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table_bony.setModel(DbUtils.resultSetToTableModel(rs));


			pst.close();
			rs.close();
			
			
			String query2 = "select * from access where id_karty = "+Id_karty+" and `data` like '"+Dataa.getText()+"%' order by `data` desc"; 
			PreparedStatement pst2=connection2.prepareStatement(query2);
			ResultSet rs2=pst2.executeQuery();
			table_bramka.setModel(DbUtils.resultSetToTableModel(rs2));


			pst2.close();
			rs2.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public void ShowName(Connection connection, Connection connection2) throws SQLException
	{
		String query = null;
		
		try {

			query = "select cards_name_surname_nrhacosoft.nazwisko_imie from cards_name_surname_nrhacosoft where HacoSoftnumber='"+HacoSoftnumber.getText()+"'";
			//query = "select cards_name_surname_nrhacosoft.nazwisko_imie from cards_name_surname_nrhacosoft where id_karty='"+HacoSoftnumber.getText()+"'";
			
			PreparedStatement pst=connection2.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			
				while(rs.next())
				{
					ImieNazwisko = rs.getString("nazwisko_imie");
				}	
				Imie_nazwisko.setText(ImieNazwisko);
			pst.close();			
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	
	public void AddWejscie(Connection connection,String data)
	{
		String query = null;	
		String data2 = Dataa.getText() +" "+ data;
		System.out.println(data2);
		
		try {
			// aby dobrze dodac MUSI byc pelna data razem z godzina np "2018-12-12 07:19:59"
			query = "insert into access (`id_karty`,`akcja`,`data`) values ('"+Id_karty+"','wejscie','"+data2+"')";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.execute();

			pst.close();
	
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void AddWyjscie(Connection connection,String data)
	{
		String query = null;	
		String data2 = Dataa.getText() +" "+ data;
		System.out.println(data2);
		
		try {
			// aby dobrze dodac MUSI byc pelna data razem z godzina np "2018-12-12 07:19:59"
			query = "insert into access (`id_karty`,`akcja`,`data`) values ('"+Id_karty+"','wyjscie','"+data2+"')";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.execute();

			pst.close();
	
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void deleteWej(Connection connection, String delete)
	{
		String query = null;
		
		try {

			query = "delete from access where id = '"+delete+"'";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.execute();
			pst.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void changeWej(Connection connection, String id,String data) throws SQLException
	{
		String query = null;
		String data2 = Dataa.getText() +" "+ data;
		System.out.println(data2);

		
		query = "update access set `data` = '"+data2+"'" + 
				"where id = '"+id+"'";
		PreparedStatement pst=connection.prepareStatement(query);
		pst.execute();
		pst.close();
		
	}

	
	public boolean CheckForColons(String value) 
	{
		//HH:MM:SS
		boolean czy = false;
		
		char colon1 = value.charAt(2);
		char colon2 = value.charAt(5);

		if(colon1 == ':' && colon2 == ':')
			czy = true;
			
		
		return czy;
		
	}
	
	public boolean CheckDataa(String data)
	{
		
		System.out.println("dlugosc daty:" + data.length());
		
		boolean okej = false;
		
		// musza byc!
		char minus1 = data.charAt(4); // -
		char minus2 = data.charAt(7); // -
		char char0 = data.charAt(0); // 2
		char char1 = data.charAt(1); // 0        -> 2018-12-20  -> yyyy-mm-dd	
		
		char char5 = data.charAt(5); // miesiac pierwsza cyfra
		char char8 = data.charAt(8); // dzien pierwsza cyfra
		

		if(minus1 == '-' && minus2 == '-' && char0 == '2' && char1 == '0' )
		{
			if((char5 == '1' || char5 == '0' ) && ( char8 >=0  || char8 < 4))	
			{
				if(data.length() == 10)
				okej  = true;		
			}
		}

		return okej;

	}

	
}


