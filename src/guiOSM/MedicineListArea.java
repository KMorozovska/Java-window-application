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

import dataOSM.MedicineContener;
import dataOSM.MedicineTableModel;
import dataOSM.OurDataBase;
import dataOSM.PatientContener;
import dataOSM.PatientTableModel;

/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk Klasa MedicineListArea jest
 *         klasa, w ktorej utworzony jest panel 'Stan lekow w bazie' oraz
 *         wszystkie jego skladowe komponenty. W ramach tej klasy odbywa sie
 *         rowniez obsluga zdarzen tego panelu.
 *
 */
public class MedicineListArea extends JPanel implements ActionListener {

	private JTable medicineTable;
	private JScrollPane scrollPane;
	private MedicineArea medicineArea;
	private NewMedicine newMedicine;

	private String[] columnNames = { "Nazwa leku", "Dostępna ilość" };
	private AbstractTableModel model;

	private JPanel table = new JPanel();
	private JPanel buttons = new JPanel();
	private JButton addButton = new JButton("Dodaj lek");
	private JButton deleteButton = new JButton("Usuń lek");

	private OurDataBase ourDB;

	private MedicineContener medicines;
	private String value; // do tej zmiennej zapisywana jest wartosc nru PESEL
							// pacjenta znajdujacego sie w tabeli

	public MedicineListArea(OurDataBase ourDB, MedicineArea medicineArea, NewMedicine newMedicine) throws SQLException {
		this.ourDB = ourDB;
		this.medicines = ourDB.getMedicines();
		this.medicineArea = medicineArea;
		this.newMedicine = newMedicine;
		// this.examinationArea = examinationArea;
		TitledBorder paneltitle;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBackground(Color.WHITE);

		this.setLayout(new BorderLayout());

		paneltitle = BorderFactory.createTitledBorder(blackline, "Stan leków w bazie");
		paneltitle.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(paneltitle);

		model = new MedicineTableModel(medicines.getmedicinesList(),
				columnNames); /*
								 * W tym miejscu tworzony jest model tabeli,
								 * ktory zdefiniowany zostal w klasie
								 * PatientTableModel
								 */
		this.createComponents();
	}

	private void createComponents() {
		medicineTable = new JTable(model);
		medicineTable.setRowSelectionAllowed(true);

		ListSelectionModel rowSelectionModel = medicineTable.getSelectionModel();
		rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Tylko
																					// jeden
																					// rekord
																					// tabeli
																					// na
																					// raz
																					// moze
																					// byc
																					// wybrany

		scrollPane = new JScrollPane(medicineTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table.add(scrollPane);
		this.add(table, BorderLayout.CENTER);
		table.setBackground(Color.WHITE);

		buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
		buttons.setLayout(new GridLayout(1, 2, 10, 5));
		buttons.setBackground(Color.WHITE);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		buttons.add(addButton);
		buttons.add(deleteButton);
		this.add(buttons, BorderLayout.PAGE_END);

	}

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
			medicineArea.medicineMode = 1;

			newMedicine.manageComponents(true);
		}

		if (source == deleteButton) {

			int row = medicineTable.getSelectedRow();
			if (row >= 0) { // Ponizszy fragment kodu wykona sie, gdy wybrany
							// zostal jakis rekord w tabeli
				value = medicineTable.getModel().getValueAt(row, 0).toString();
				// String examinationExisting = patientTable.getValueAt(row,
				// 4).toString();
			
				
				for (int k = 0; k < medicines.getmedicinesList().size(); k++) {
					if (value.equals(medicines.getmedicinesList().get(k).getName())) {

						medicines.addListenerForRemoval(p -> model.fireTableRowsDeleted(row, row));

						try {
							ourDB.deleteMedicine(medicines.getmedicinesList().get(k).getName());
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						medicines.deleteMedicine(medicines.getmedicinesList().get(k));

						JFrame framePatientDeleted = new JFrame();
						JOptionPane.showMessageDialog(framePatientDeleted, "Usunięto lek o nazwie: " + value,
								"Usunięto lek", JOptionPane.INFORMATION_MESSAGE);

					}
				}
			}

		}

	}
}
