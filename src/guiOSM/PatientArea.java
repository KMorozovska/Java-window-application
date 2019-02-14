package guiOSM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

import dataOSM.OurDataBase;
import dataOSM.Patient;
import dataOSM.PatientContener;

/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk
 * Klasa PatientArea jest klasą, w której utworzony jest panel 'Dane pacjenta' oraz wszystkie jego składowe komponenty. 
 * W ramach tej klasy odbywa się również obsługa zdarzeń tego panelu.
 *
 */

public class PatientArea extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private ButtonGroup bgSize;
	/** rbMan - męczyzna, rbWoman - kobieta */
	public JRadioButton rbMan, rbWoman;
	/** insurance - wybor rodzaju ubezpieczenia */
	public JComboBox<String> insurance;
	private JButton save, cancel;

	/**
	 * Pola przechowujące wartości wprowadzane przez użytkownika - odpowiednio:
	 * imie pacjenta, nazwisko pacjenta oraz PESEL pacjenta
	 */
	public JTextField nameField, surnameField, peselField;
	private JLabel lName, lSurname, lPesel, lSex, lInsurance;
	public String name, surname, PESEL, sex, insuranceVal;
	private Patient patient;
	private PatientContener patientContener;
	private OurDataBase ourDB;
	private PatientListArea patientListArea;
	//private ExaminationArea examinationArea;
	private String[] labels = { "Imię: ", "Nazwisko: ", "PESEL: ", "Płeć: ", "Ubezpieczenie: " };

	/**
	 * flag - zmienna używana do tego, aby zasygnalizować, czy PESEL w przypadku
	 * dodawania nowego pacjenta nie jest PESELem, który już istnieje na liście
	 * pacjentów true - wprowadzony PESEL jest juz zarejestrowany false -
	 * wprowadzony PESEL nie jest jeszcze zarejestrowany
	 */
	private boolean flag;

	/**
	 * patientMode - zmienna uzywana do tego, aby określić z jakim typem pracy
	 * na pacjencie mamy do czynienia: 1 - dodawanie nowego pacjenta 2 - edycja
	 * istniejącego pacjenta
	 */
	public int patientMode;

	/**
	 * initialPESEL - zmienna, ktora uzywana jest w przypadku edycji
	 * istniejacego pacjenta. Zmienna ta przechowuje wartosc numeru PESEL
	 * pacjenta przed edycji. PESEL jest traktowany w tej aplikacji jako
	 * unikatowy identyfikator pacjenta oraz badania. Na podstawie tej zmiennej
	 * odszukiwany jest odpowiedni obiekt na liscie pacjentow i obiekt ten
	 * poddawany jest edycji.
	 */
	String initialPESEL;

	// konstruktor klasy PatientArea
	public PatientArea(PatientContener patientContener, OurDataBase ourDB) {
		this.patientContener = patientContener;
		this.ourDB = ourDB;
		//this.examinationArea = examinationArea;
		TitledBorder paneltitle;
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBackground(Color.WHITE);
		this.setName("Dane pacjenta");
		this.setLayout(new BorderLayout());
		paneltitle = BorderFactory.createTitledBorder(blackline, "Dane pacjenta");
		paneltitle.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(paneltitle);

		this.createComponents();
		this.manageComponents(false);
	}

	/**
	 * Metoda createComponents() jest metoda, w ktorej tworzone sa wszystkie
	 * komponenty wchodzace w sklad panelu 'Dane pacjenta'. Metoda ta wywolywana
	 * jest w konstruktorze klasy.
	 */
	private void createComponents() {
		save = new JButton("Zapisz");
		cancel = new JButton("Anuluj");
		save.setBounds(100, 100, 50, 20);
		cancel.setBounds(100, 100, 50, 20);
		save.addActionListener(this);
		cancel.addActionListener(this);

		bgSize = new ButtonGroup();
		rbMan = new JRadioButton("Mezczyzna", false);
		rbMan.setBounds(50, 150, 100, 20);
		rbMan.setBackground(Color.WHITE);
		rbMan.addActionListener(this);

		rbWoman = new JRadioButton("Kobieta", false);
		rbWoman.setBounds(150, 150, 100, 20);
		rbWoman.setBackground(Color.WHITE);
		rbWoman.addActionListener(this);

		insurance = new JComboBox<String>();
		insurance.setBackground(Color.WHITE);
		insurance.addItem("NFZ");
		insurance.addItem("Prywatne");
		insurance.addItem("Brak");
		insurance.addActionListener(this);

		JPanel buttons = new JPanel(); // panel w ktorym umieszczone zostaly przyciski "Zapisz" oraz "Anuluj"

		buttons.setLayout(new GridLayout(0, 2, 5, 0));
		buttons.setBackground(Color.WHITE);

		JPanel adPanel = new JPanel(); // panel, na ktorym ulozone zostaly wszystkie skladowe komponenty
		adPanel.setLayout(new GridLayout(0, 2, 0, 10));
		adPanel.setBackground(Color.WHITE);
		this.add(adPanel, BorderLayout.CENTER);

		JPanel sexChoice = new JPanel(); // panel, na ktorym umieszczone zostaly  radio buttony do wyboru plci pacjenta
		sexChoice.setLayout(new GridLayout(0, 2, 20, 0));
		sexChoice.setBackground(Color.WHITE);

		lName = new JLabel(labels[0]);
		lName.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lName);
		nameField = new JTextField(10);
		((AbstractDocument) nameField.getDocument()).setDocumentFilter(new CustomDocumentFilter("^[a-zA-Z]+$")); /* dodanie sprawdzenia poprawnosci formatu wprowadzonego 
																													tekstu dla imienia(wprowadzone moga byc jedynie litery male lub wielkie, brak spacji */
															
		lName.setLabelFor(nameField);
		adPanel.add(nameField);

		lSurname = new JLabel(labels[1]);
		lSurname.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lSurname);
		surnameField = new JTextField(10);
		((AbstractDocument) surnameField.getDocument()).setDocumentFilter(new CustomDocumentFilter("^[a-zA-Z]+$")); /* dodanie sprawdzenia poprawnosci formatu wprowadzonego 
																													tekstu dla nazwiska(wprowadzone moga byc jedynie litery male lub wielkie, brak spacji */
		lSurname.setLabelFor(surnameField);
		adPanel.add(surnameField);

		lPesel = new JLabel(labels[2]);
		lPesel.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lPesel);
		peselField = new JTextField(10);
		((AbstractDocument) peselField.getDocument()).setDocumentFilter(new CustomDocumentFilter("^[0-9]{0,11}$")); /* dodanie sprawdzenia poprawnosci formatu wprowadzonego 
																													tekstu dla numeru PESEL(wprowadzone moga byc jedynie cyfry; max. 11, brak spacji) */
		lPesel.setLabelFor(peselField);
		adPanel.add(peselField);

		lSex = new JLabel(labels[3]);
		lSex.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lSex);
		bgSize.add(rbWoman);
		sexChoice.add(rbWoman);
		lSex.setLabelFor(sexChoice);
		adPanel.add(sexChoice);
		bgSize.add(rbMan);
		sexChoice.add(rbMan);
		lSex.setLabelFor(sexChoice);

		lInsurance = new JLabel(labels[4]);
		lInsurance.setHorizontalAlignment(javax.swing.JLabel.LEFT);
		adPanel.add(lInsurance);
		adPanel.add(insurance);
		lInsurance.setLabelFor(insurance);

		buttons.add(save);
		buttons.add(cancel);
		adPanel.add(buttons);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == cancel) {
			this.clearFields(); // W przypadku wcisniecia przycisku "Anuluj" nastepuje wyczyszczenie pol (usuniecie uprzednio wprowadzonych wartosci) oraz anulowanie edycji/dodawania pacjenta
			
				this.manageComponents(false);
		}

		else if (source == insurance) {
			insuranceVal = insurance.getSelectedItem().toString(); // zapamietanie wybranej wartosci rodzaju ubezpieczenia (combobox)
		}

		else if (source == save) {
			if (rbWoman.isSelected()) {
				sex = "K";
			} else if (rbMan.isSelected()) {
				sex = "M";
			}

			name = nameField.getText();
			surname = surnameField.getText();
			PESEL = peselField.getText();

			if (!name.equals("") && !surname.equals("") && !PESEL.equals("") && !PESEL.equals(" ")
					&& (rbWoman.isSelected() || rbMan.isSelected())) // Sprawdzenie, czy wszystkie pola w formularzu zostaly wypelnione.
			{

				if (patientContener.getpatientList().size() == 0) // Kod zawarty w tym if wykonywany jest w przypadku, gdy dodawany jest pierwszy pacjent
												
				{
					this.manageComponents(false); // dezaktywowanie pol panelu
					patient = new Patient(name, surname, PESEL, sex, insuranceVal);
					try {
						ourDB.addPatient(patient);
						ourDB.showPatients();
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					patientContener.addPatient(patient);
					this.clearFields();

					JFrame framePatientAdded = new JFrame();
					JOptionPane.showMessageDialog(framePatientAdded, "Pacjent został poprawnie dodany!",
							"Dodano pacjenta", JOptionPane.INFORMATION_MESSAGE);
				}

				else // Kod zawarty w tym else wykonywany jest, gdy dodajemy kolejnego pacjenta
				{
					patient = new Patient(name, surname, PESEL, sex, insuranceVal);

					for (int k = 0; k < patientContener.getpatientList().size(); k++) // przeszukanie listy pacjentow
					{
						if (PESEL.equals((patientContener.getpatientList().get(k).getPESEL()))) // sprawdzenie, czy wprowadzony przez uzytkownika PESEL jest juz zarejestrowany
						{
							if (this.patientMode == 1) // sprawdzenie, czy mamy do czynienia z dodaniem nowego pacjenta
							{
								JFrame frameSamePESEL = new JFrame();
								flag = true; // ustawienie flagi, ktora mowi o tym, czy wprowadzony PESEL  jest juz zarejestrowany

								JOptionPane.showMessageDialog(frameSamePESEL,
										"Pacjent o podanym adresie PESEL jest już zarejestrowany!",
										"Bład zapisu pacjenta", JOptionPane.ERROR_MESSAGE);
							}

							else if (this.patientMode == 2) /* sprawdzenie, czy mamy do czynienia z edycja ostniejacego pacjenta. Jezeli tak to ustawiamy flage na false i dopuszczamy wprowadzony PESEL, gdyz nie dodajemy nowego pacjenta
																a jedynie edytujemy juz istniejacego pacjenta, a podczas tej edycji mozliwe, ze nie bedzie zmieniany numer PESEL, a jedynie inne parametry. */
							{
								flag = false;
							}
						}
					}

					if (flag) // Jezeli nie dopuszczamy wprowadzonego PESELu, to "zerujemy" flag� (aby przywrocic jej domyslna wartosc) oraz "zerujemy" pole PESEL w panelu
					{
						flag = false;
						peselField.setText("");
					}

					else // Kod w tym else wykonany jest, gdy wprowadzony PESEL jest dopuszczony do zapisu
					{
						this.manageComponents(false);

						if (patientMode == 1) // Dodawanie nowego pacjenta
						{
							try {
								ourDB.addPatient(patient);
								ourDB.showPatients();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							patientContener.addPatient(patient); // Dodanie pacjenta do listy
																	
							this.clearFields();

							JFrame framePatientAdded = new JFrame();
							JOptionPane.showMessageDialog(framePatientAdded, "Pacjent został poprawnie dodany!",
									"Dodano pacjenta", JOptionPane.INFORMATION_MESSAGE);
						}

						else if (patientMode == 2) // Edycja istniejacego pacjenta
						{
							for (int i = 0; i < patientContener.getpatientList().size(); i++) {
								if (patientContener.getpatientList().get(i).getPESEL().equals(initialPESEL)){ // Poszukiwanie na liscie pacjenta o wartosci numeru PESEL sprzed edycji (initialPESEL)															
																													
							}

							String newPESEL = patientContener.editPatient(patient, initialPESEL); /* Informacje o pacjencie zostaja poddane w tym miejscu edyzji. 
																									Dodatkowo metoda odpowiedzialna za edycje pacjenta zwraca wartosc numeru PESEL pacjenta po edycji. 
																									Po edycji pacjenta wybrany rekord zostaje odznaczony w tabeli, ale wciaz mozna edytowac/dodac badanie do tego pacjenta bez koniecznosci ponownego zaznaczania rekordu*/
																										

							this.clearFields();

							JFrame framePatientEdited = new JFrame();
							JOptionPane.showMessageDialog(framePatientEdited, "Pacjent został poprawnie edytowany!",
									"Edytowano pacjenta", JOptionPane.INFORMATION_MESSAGE);
						}

					}
				}
			}}

			else // Kod w tym else wykonywany jest, gdy chociaz jedno z pol formularza nie zostalo wypelnione
			{
				JFrame frame = new JFrame();

				JOptionPane.showMessageDialog(frame,
						"Wszystkie pola w formularzu 'Dane pacjenta' muszą być wypełnione!", "Bład zapisu pacjenta",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * Metoda clearFields() czysci formularz (ustawia wszystkie pola na
	 * defaultowa wartosc).
	 */
	public void clearFields() {
		nameField.setText("");
		surnameField.setText("");
		peselField.setText("");
		bgSize.clearSelection();
		insurance.setSelectedIndex(0);
	}

	/**
	 * Metoda manageComponents sluzy do sterowaniem komponentami (aktywacja lub
	 * dezaktywacja panelu)
	 * 
	 * @param manageFlag
	 *            - flaga, ktora mowi o tym, co nalezy zrobic z danym
	 *            komponentem panelu (true - aktywacja komponentow, false -
	 *            dezaktywania komponentow)
	 */
	public void manageComponents(boolean manageFlag) {
		nameField.setEnabled(manageFlag);
		surnameField.setEnabled(manageFlag);
		peselField.setEnabled(manageFlag);
		insurance.setEnabled(manageFlag);
		rbWoman.setEnabled(manageFlag);
		rbMan.setEnabled(manageFlag);
		save.setEnabled(manageFlag);
		cancel.setEnabled(manageFlag);
	}

}
