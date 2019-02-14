package guiOSM;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.*;

import dataOSM.OurDataBase;
import dataOSM.PatientContener;

/**
 * Klasa implementujaca glowne okno aplikacji.
 */
public class MainView extends JFrame implements ActionListener {

	/**
	 * element listy rozwijanej, utworzony jako element globalny klasy, ktory
	 * umooliwia zakmniecie okna po nacisnieciu na to pole listy
	 */
	MenuItem close;
	static OurDataBase ourDB = new OurDataBase();

	/**
	 * Funkcja tworzaca glowne okno aplikacji: ustala wymiary, tytul i
	 * implementuje liste rozwijana na gorze okna
	 */
	public MainView() {

		this.setTitle("Rejestracja wyników badan");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 700);

		/**
		 * utworzenie listy rozwijanej
		 */
		MenuBar menuBar = new MenuBar();
		Menu menuApp = new Menu("Aplikacja");
		close = new MenuItem("Zamknij Alt+F4");

		close.addActionListener(this);
		menuApp.add(close);
		menuBar.add(menuApp);
		setMenuBar(menuBar);

		/**
		 * Funkcja umooliwiajaca zamkniecie okna poprzez wcisniecie przyciskow
		 * Alt+F4
		 */
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (((KeyStroke.getKeyStroke(KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK)) != null)
						&& evt.getKeyCode() == KeyEvent.VK_F4)
					processWindowEvent(new WindowEvent(getWindows()[0], WindowEvent.WINDOW_CLOSING));
			}
		});
	}

	public static void main(String[] args) {

		Runnable thread = new Runnable() {
			@Override
			public void run() {
				MainView window = new MainView();
				//OurDataBase ourDB = new OurDataBase();
				try {
					ourDB.createDB();
					//ourDB.drop_table();
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JPanel mainPanel = new JPanel();
				JPanel additionalPanel = new JPanel();
				JPanel listsPanel = new JPanel();
				additionalPanel.setLayout(new GridLayout(3, 1, 5, 5));
				additionalPanel.setBackground(Color.WHITE);
				mainPanel.setLayout(new GridLayout(0, 2, 5, 5));
				mainPanel.setBackground(Color.WHITE);
				listsPanel.setLayout(new GridLayout(2, 1, 5, 5));
				listsPanel.setBackground(Color.WHITE);

				/**
				 * kontener do przechowywania danych o pacjentach
				 */
				PatientContener patientContener = null;
				try {
					patientContener = ourDB.getPatients();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				/**
				 * kontener do przechowywania danych o badaniach
				 */
				//ExaminationContener examinationContener = new ExaminationContener();

				/**
				 * panel do wprowadzenia wynikow badania
				 */

				/**
				 * panel do wprowadzenia danych pacjenta
				 */
				PatientArea patientPanel = new PatientArea(patientContener, ourDB);
				NewMedicine newMedicine = new NewMedicine(ourDB);
				MedicineArea medicineArea = new MedicineArea(ourDB);

				/**
				 * lista dodanych pacjentow wraz z zaznaczeniem, ktorzy z nich
				 * mieli wykonane badanie
				 */
				PatientListArea patientListPanel = null;
				MedicineListArea medicineListPanel = null;
				try {
					patientListPanel = new PatientListArea(patientContener, patientPanel,ourDB, medicineArea);
					medicineListPanel = new MedicineListArea(ourDB, medicineArea, newMedicine);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				window.getContentPane().add(mainPanel);
				additionalPanel.add(patientPanel);
				additionalPanel.add(medicineArea);
				additionalPanel.add(newMedicine);
				listsPanel.add(patientListPanel);
				listsPanel.add(medicineListPanel);
				mainPanel.add(additionalPanel);
				mainPanel.add(listsPanel);
				window.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(thread);
	}

	/**
	 * Funkcja reagujaca na nacisniecie elementu listy rozwijanej, znajdujacej
	 * sie na gorze panelu. Nacisniecie elementu listy rozwijanej "Zamknij
	 * Alt+F4" powoduje zamkniecie okna aplikacji.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == close) {
			try {
				ourDB.finishDB();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}

}