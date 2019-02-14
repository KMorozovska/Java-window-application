package guiOSM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

import dataOSM.Medicine;
import dataOSM.OurDataBase;


public class NewMedicine extends JPanel implements ActionListener{
	
	private String[] labels = { "Nazwa: ", "Data waznosci [YYYY-MM-DD] ", "Ilosc [mg]: " };
	private JLabel lName, lDate, lQuantity;
	public JTextField nameField, dateField, quantityField;
	private JButton save, cancel;
	private Medicine medicine;
	String name, date, quantity;
	OurDataBase ourDB;
	public boolean wasUpdated=false;
	
	public NewMedicine(OurDataBase ourDB){
		
		this.ourDB=ourDB;
		TitledBorder paneltitle;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBackground(Color.WHITE);
		this.setName("Dodaj nowe lekarstwo");
		this.setLayout(new BorderLayout());
		paneltitle = BorderFactory.createTitledBorder(blackline, "Dodaj nowe lekarstwo");
		paneltitle.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(paneltitle);
		
		this.createComponent();
		this.manageComponents(false);
		
	}
	
	private void createComponent(){
		
		JPanel buttons = new JPanel(); // panel w ktorym umieszczone zostaly przyciski "Zapisz" oraz "Anuluj"
		
		save = new JButton("Zapisz");
		save.addActionListener(this);
		cancel = new JButton("Anuluj");
		cancel.addActionListener(this);
		save.setBounds(100, 100, 50, 20);
		cancel.setBounds(100, 100, 50, 20);

		buttons.setLayout(new GridLayout(0, 2, 5, 0));
		buttons.setBackground(Color.WHITE);
		
		
		JPanel adPanel = new JPanel(); // panel, na ktorym ulozone zostaly wszystkie skladowe komponenty
		adPanel.setLayout(new GridLayout(0, 2, 0, 10));
		adPanel.setBackground(Color.WHITE);
		this.add(adPanel, BorderLayout.CENTER);
		
		lName = new JLabel(labels[0]);
		lName.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lName);
		nameField = new JTextField(10);
		
		lName.setLabelFor(nameField);
		adPanel.add(nameField);
		
		lDate = new JLabel(labels[1]);
		lDate.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lDate);
		dateField = new JTextField(10);
		
		
		lDate.setLabelFor(dateField);
		adPanel.add(dateField);

		lQuantity = new JLabel(labels[2]);
		lQuantity.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lQuantity);
		quantityField = new JTextField(10);
		((AbstractDocument) quantityField.getDocument()).setDocumentFilter(new CustomDocumentFilter("^[0-9]+$"));
		lQuantity.setLabelFor(quantityField);
		adPanel.add(quantityField);
		
		buttons.add(save);
		buttons.add(cancel);
		adPanel.add(buttons);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == cancel) {
			
		}

		else if (source == save) {
			name = nameField.getText();
			date = dateField.getText();
			quantity = quantityField.getText();

			if (!name.equals("") && !date.equals("") && !quantity.equals("")) // Sprawdzenie, czy wszystkie pola w formularzu zostaly wypelnione.
			{		
					
					medicine = new Medicine(name,quantity,date);
					try {
						ourDB.addMedicine(medicine);
						ourDB.showMedicines();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					JFrame framePatientAdded = new JFrame();
					JOptionPane.showMessageDialog(framePatientAdded, "Lek został poprawnie dodany!",
							"Dodano lek", JOptionPane.INFORMATION_MESSAGE);
				}
			wasUpdated=true;
		
	}

		
	}

	
	void manageComponents(boolean a){
		
		nameField.setEnabled(a);
		dateField.setEnabled(a);
		quantityField.setEnabled(a);
		cancel.setEnabled(a);;
		save.setEnabled(a);
	}


}
