package guiOSM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import dataOSM.OurDataBase;
import dataOSM.PatientContener;
import dataOSM.PatientTableModel;

/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk Klasa PatientListArea jest
 *         klasa, w ktorej utworzony jest panel 'Lista pacjentow' oraz wszystkie
 *         jego skladowe komponenty. W ramach tej klasy odbywa sie rowniez
 *         obsluga zdarzen tego panelu.
 *
 */
public class PatientListArea extends JPanel implements ActionListener {

	/**
	 * patientTable - tabela w ktorej wyswietlani sa zarejestrowani pacjenci
	 */
	private JTable patientTable;
	private JScrollPane scrollPane;
	private MedicineArea medicineArea;

	/**
	 * columnNames - nazwy kolumn tabli
	 */
	private String[] columnNames = { "Imię i nazwisko", "Płeć", "PESEL", "Ubezpieczenie" };
	private AbstractTableModel model;

	/**
	 * table - panel, na ktorym umieszczona zostala tabela prezentujaca
	 * zarejestrowanych pacjentow
	 */
	private JPanel table = new JPanel();

	/**
	 * buttons - panel, na ktorym umieszczone zostaly przyciski "Dodaj" oraz
	 * "Usuń"
	 */
	private JPanel buttons = new JPanel();
	private JButton addButton = new JButton("Dodaj");
	private JButton deleteButton = new JButton("Usuń");
	private JButton addMedicine = new JButton("Dodaj lekarstwo");
	private JButton editMedicine = new JButton("Edytuj lekarstwo");
	private JButton showMedicineInfo = new JButton("Przyjmowane leki");
	private JButton giveMedicine = new JButton("Podaj lekarstwo");

	private OurDataBase ourDB;

	private PatientArea patientArea;
	private PatientContener patients;
	// private ExaminationArea examinationArea;
	private String value; // do tej zmiennej zapisywana jest wartosc nru PESEL
							// pacjenta znajdujacego sie w tabeli

	public PatientListArea(PatientContener patients, PatientArea patientArea, OurDataBase ourDB,
			MedicineArea medicineArea) {
		this.ourDB = ourDB;
		this.patients = patients;
		this.medicineArea = medicineArea;
		this.patientArea = patientArea;
		// this.examinationArea = examinationArea;
		TitledBorder paneltitle;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBackground(Color.WHITE);
		this.setName("Lista pacjentów");
		this.setLayout(new BorderLayout());

		paneltitle = BorderFactory.createTitledBorder(blackline, "Lista pacjentów");
		paneltitle.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(paneltitle);

		model = new PatientTableModel(patients.getpatientList(),
				columnNames); /*
								 * W tym miejscu tworzony jest model tabeli,
								 * ktory zdefiniowany zostal w klasie
								 * PatientTableModel
								 */
		patients.addListener(p -> model.fireTableDataChanged());

		this.createComponents();
	}

	private void createComponents() {
		patientTable = new JTable(model);
		patientTable.setRowSelectionAllowed(true);

		ListSelectionModel rowSelectionModel = patientTable.getSelectionModel();
		rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Tylko
																					// jeden
																					// rekord
																					// tabeli
																					// na
																					// raz
																					// moze
																					// byc
																					// wybrany

		rowSelectionModel.addListSelectionListener(new ListSelectionListener() { // Ten
																					// fragment
																					// kodu
																					// dziala
																					// na
																					// zaznaczonym
																					// rekordzie
																					// w
																					// tabeli
			public void valueChanged(ListSelectionEvent e) {

				int selectedRow = patientTable.getSelectedRow(); // index
																	// zaznaczonego
																	// rekordu w
																	// tabeli
				if (selectedRow >= 0) { // sprawdzenie, czy jakikolwiek rekord
										// zostal zaznaczony
					String nameANDsurname = patientTable.getValueAt(selectedRow, 0).toString(); // pobranie
																								// wartosci
																								// imienia
																								// i
																								// nazwiska
																								// pacjenta
																								// (wartosci
																								// rozdzielone
																								// spacja)
					String delim = " ";
					String[] personalData = nameANDsurname.split(delim); // rozdzielenie
																			// imienia
																			// i
																			// nazwiska
																			// na
																			// dwa
																			// oddzielne
																			// pola
																			// i
																			// umieszczenie
																			// ich
																			// w
																			// tablicy
					patientArea.patientMode = 2; // ustawienie trybu edycji
													// pacjenta
					patientArea.nameField.setText(personalData[0]); // ustawienie
																	// biezacej
																	// wartosci
																	// imienia
																	// wybranego
																	// pacjenta
																	// w
																	// odpowiednim
																	// polu na
																	// panelu
																	// 'Dane
																	// pacjenta'
					patientArea.surnameField.setText(personalData[1]); // ustawienie
																		// biezacej
																		// wartosci
																		// nazwiska
																		// wybranego
																		// pacjenta
																		// w
																		// odpowiednim
																		// polu
																		// na
																		// panelu
																		// 'Dane
																		// pacjenta'
					patientArea.initialPESEL = patientTable.getValueAt(selectedRow, 2).toString(); // pobranie
																									// wartosci
																									// nru
																									// PESEL
																									// wybranego
																									// pacjenta
																									// przed
																									// edycja

					patientArea.peselField.setText(patientTable.getValueAt(selectedRow, 2).toString());

					if (patientTable.getValueAt(selectedRow, 1).toString().equals("M")) { // ustawienie
																							// odpowiednio
																							// plci
																							// na
																							// panelu
																							// 'Dane
																							// pacjenta'
																							// zgodnie
																							// z
																							// biezacymi
																							// ustawieniami
																							// dla
																							// wybranego
																							// pacjenta
						patientArea.rbMan.setSelected(true);
					} else if (patientTable.getValueAt(selectedRow, 1).toString().equals("K")) {
						patientArea.rbWoman.setSelected(true);
					}

					patientArea.insurance.setSelectedItem(patientTable.getValueAt(selectedRow, 3).toString()); 

					patientArea.manageComponents(true); 
					
				}
			}

		});

		scrollPane = new JScrollPane(patientTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table.add(scrollPane);
		this.add(table, BorderLayout.CENTER);
		table.setBackground(Color.WHITE);
		// buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.setLayout(new GridLayout(3, 0, 10, 5));
		buttons.setBackground(Color.WHITE);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		addMedicine.addActionListener(this);
		giveMedicine.addActionListener(this);
		showMedicineInfo.addActionListener(this);
		buttons.add(addButton);
		// buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(deleteButton);
		// buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(addMedicine);
		// buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(editMedicine);
		// buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(showMedicineInfo);
		buttons.add(giveMedicine);
		this.add(buttons, BorderLayout.PAGE_END);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if (source == addButton) { /*
									 * Po kliknieciu przycisku "Dodaj"
									 * aktywowany jest tryb dodawania nowego
									 * pacjenta, formularz jest czyszczony i
									 * aktywowany. Podczas dodawania nowego
									 * uzytkownika panel 'Badanie' jest
									 * nieaktywny.
									 */
			patientArea.patientMode = 1;
			patientArea.clearFields();
			patientArea.manageComponents(true);

		}

		if (source == deleteButton) { /*
										 * Po kliknieciu przycisku "Usuń"
										 * nastepuje wyszukanie na liscie
										 * pacjentow zaznaczonego w tabeli
										 * pacjenta oraz jego usuniecie z listy
										 * oraz tabeli
										 */

			int row = patientTable.getSelectedRow();
			if (row >= 0) { // Ponizszy fragment kodu wykona sie, gdy wybrany
							// zostal jakis rekord w tabeli
				value = patientTable.getModel().getValueAt(row, 2).toString();
				// String examinationExisting = patientTable.getValueAt(row,
				// 4).toString();

				for (int k = 0; k < patients.getpatientList().size(); k++) {
					if (value.equals(patients.getpatientList().get(k).getPESEL())) {

						patients.addListenerForRemoval(p -> model.fireTableRowsDeleted(row, row));

						try {
							ourDB.deletePatient(patients.getpatientList().get(k).getPESEL());
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						patients.deletePatient(patients.getpatientList().get(k));

						patientArea.clearFields();
						patientArea.manageComponents(false);

						JFrame framePatientDeleted = new JFrame();
						JOptionPane.showMessageDialog(framePatientDeleted,
								"Usunięto pacjenta o numerze PESEL: " + value, "Usunięto pacjenta",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}

		if (source == addMedicine) {

			medicineArea.manageComponents(true);

			int selectedRow = patientTable.getSelectedRow(); // index
																// zaznaczonego
																// rekordu w
																// tabeli
			if (selectedRow >= 0) {

				medicineArea.selectedPESEL = patientTable.getValueAt(selectedRow, 2).toString();

				System.out.println("Wcisnieto Dodaj lekarstwo");
				
					try {
						ourDB.getMedicines().getmedicinesList().forEach(c -> medicineArea.type.addItem(c.getName()));

					} catch (SQLException e1) {
						System.out.println("Chyba cos jest nie tak :( ");
						e1.printStackTrace();
					}

			}

		}

		if (source == giveMedicine) {

			medicineArea.manageComponents(true);
			System.out.println("Wcisnieto podaj lek");

			try {
				ourDB.giveMedicine();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if (source == showMedicineInfo) {

			System.out.println("Wcisnieto Pokaz Leki");
			
			int selectedRow = patientTable.getSelectedRow();
			if (selectedRow >= 0) {

				medicineArea.selectedPESEL = patientTable.getValueAt(selectedRow, 2).toString();
				
				try {
					
					ourDB.showPatientsMedicines();
					
					JFrame frameMedAdd = new JFrame();
					JOptionPane.showMessageDialog(frameMedAdd, "Pacjent przyjmuje: ", 
							"Przyjmowane leki",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (SQLException e1) {
					System.out.println("Chyba cos jest nie tak :( ");
					e1.printStackTrace();
				}
			}
		}

	}

}
