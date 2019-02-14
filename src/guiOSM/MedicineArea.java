package guiOSM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import dataOSM.MedicineContener;
import dataOSM.OurDataBase;
import dataOSM.Patient;

public class MedicineArea extends JPanel implements ActionListener {

	JButton save, edit, add, info, cancel;
	JComboBox<String> type;
	JComboBox<String> timesDaily;
	JComboBox<String> amount;
	JLabel lType, lAmount, lTimesDaily;
	public String selectedPESEL;
	public int medicineMode = 0;
	private String selectedName, selectedAmount, selectedDailyTimes;
	OurDataBase ourDB;

	public MedicineArea(OurDataBase ourDB) {

		this.ourDB = ourDB;
		TitledBorder paneltitle;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBackground(Color.WHITE);
		this.setName("Lekarstwa");
		this.setLayout(new BorderLayout());
		paneltitle = BorderFactory.createTitledBorder(blackline, "Lekarstwa");
		paneltitle.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(paneltitle);

		this.createComponents();
		this.manageComponents(false);

	}

	/**
	 * Funkcja tworzaca wszystkie elementy widoczne w panelu: podpisy, pola do
	 * wprowadzania danych, informacje obok kazdego z parametrow na temat tego
	 * czy miesci sie w normie czy nie
	 */
	private void createComponents() {

		JPanel adPanel = new JPanel();

		adPanel.setLayout(new GridLayout(0, 2, 0, 15));
		adPanel.setBackground(Color.WHITE);
		this.add(adPanel, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(0, 2, 5, 0));
		buttons.setBackground(Color.WHITE);

		save = new JButton("Zapisz");
		cancel = new JButton("Anuluj");
		save.addActionListener(this);
		cancel.addActionListener(this);
		buttons.add(save);
		buttons.add(cancel);

		lType = new JLabel("Nazwa lekarstwa");
		lType.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lType);
		type = new JComboBox<String>();
		type.setBackground(Color.WHITE);

		adPanel.add(type);

		lAmount = new JLabel("Dawka");
		lAmount.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lAmount);
		amount = new JComboBox<String>();
		amount.setBackground(Color.WHITE);
		amount.addItem("50");
		amount.addItem("100");
		adPanel.add(amount);

		lTimesDaily = new JLabel("Ile razy dziennie");
		lTimesDaily.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lTimesDaily);
		timesDaily = new JComboBox<String>();
		timesDaily.setBackground(Color.WHITE);
		timesDaily.addItem("1");
		timesDaily.addItem("2");
		timesDaily.addItem("3");
		adPanel.add(timesDaily);

		adPanel.add(buttons);

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == save) {
			System.out.println("Wcisnieto zapisz!");
			selectedName = type.getSelectedItem().toString();
			selectedAmount = amount.getSelectedItem().toString();
			selectedDailyTimes = timesDaily.getSelectedItem().toString();
			try {
				ourDB.addMedicinePatient(selectedName, selectedPESEL, selectedAmount, selectedDailyTimes);
				
				JFrame frameMedAdd = new JFrame();
				JOptionPane.showMessageDialog(frameMedAdd, "Dodano lekarstwo do pacjenta!",
						"Dodawanie leku do pacjenta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		if (source == cancel) {

			this.manageComponents(false);
		}

		else if (source == type) {
			selectedName = type.getSelectedItem().toString(); // zapamietanie
																// wybranej
																// wartosci
																// rodzaju
																// ubezpieczenia
																// (combobox)
		} else if (source == timesDaily) {
			selectedDailyTimes = timesDaily.getSelectedItem().toString(); // zapamietanie
																			// wybranej
																			// wartosci
																			// rodzaju
																			// ubezpieczenia
																			// (combobox)
		} else if (source == amount) {
			selectedAmount = amount.getSelectedItem().toString(); // zapamietanie
																	// wybranej
																	// wartosci
																	// rodzaju
																	// ubezpieczenia
																	// (combobox)
		}
		
	}

	/**
	 * Funkcja decydujaca o tym, czy panel jest aktywny czy nie
	 */
	public void manageComponents(boolean manageFlag) {
		type.setEnabled(manageFlag);
		amount.setEnabled(manageFlag);
		timesDaily.setEnabled(manageFlag);
		save.setEnabled(manageFlag);
		cancel.setEnabled(manageFlag);
	}

	public void clearFields(boolean manageFlag) {

	}

}