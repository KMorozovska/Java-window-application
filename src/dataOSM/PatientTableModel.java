package dataOSM;

import java.util.List;

import javax.swing.table.AbstractTableModel;


/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk 
 * Klasa PatientTableModel definuje nasz wlasny model tabeli dostosowany do potrzeb aplikacji
 */
public class PatientTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<Patient> patients;
	private String[] columnNames;
	private String name;
	private String surname;

	public PatientTableModel(List<Patient> patients, String[] columnNames) { //przyjmuje liste pacjentow oraz tablice z nazwami kolumn tabeli
		this.patients = patients;
		this.columnNames = columnNames;
	}

	/*public Patient getPatientToDelete(int row) {
		Patient patient = patients.get(row);
		return patient;
	} */

	@Override
	public int getColumnCount() { //zwraca liczbe kolumn tabeli
		return columnNames.length;
	}

	@Override
	public int getRowCount() { //zwraca liczbe wierszy tabeli
		return patients.size();
	}

	public static class Columns { // mappowanie dla poszczegolnych kolumn tabeli
		public static final int NAMEandSURNAME = 0;
		public static final int SEX = 1;
		public static final int PESEL = 2;
		public static final int INSURANCE = 3;
		//public static final int EXAMINATION = 4;

	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { // Pozyskiwanie wartosci danej komorki tabeli

		Patient patient = patients.get(rowIndex); 

		switch (columnIndex) {
		case Columns.NAMEandSURNAME: {
			name = patient.getName();
			surname = patient.getSurname();
			return name + " " + surname;
		}

		case Columns.SEX:
			return patient.getSex();

		case Columns.PESEL:
			return patient.getPESEL();

		case Columns.INSURANCE:
			return patient.getInsurance();

		/*case Columns.EXAMINATION:
			return patient.getExamination(); */

		default:
			return "";
		}
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) { //Ustawianie wartosci danej komorki tabeli (stosowane przy edycji danego rekordu)

		Patient patient = patients.get(rowIndex);

		switch (columnIndex) {
		case Columns.NAMEandSURNAME: {
			String nameANDsurname = value.toString();

			String delim = " ";
			String[] personalData = nameANDsurname.split(delim);
			patient.setName(personalData[0]);
			patient.setSurname(personalData[1]);
		}
		case Columns.SEX:
			patient.setSex(value.toString());

		case Columns.PESEL:
			patient.setPESEL(value.toString());

		case Columns.INSURANCE:
			patient.setInsurance(value.toString());

		/*case Columns.EXAMINATION:
			patient.setExamination(Boolean.parseBoolean((value.toString()))); */
		}

		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int column) { /*Stosuje sie to, aby rozroznic typy poszczegolnych kolumn tabeli. 
												Gdybysmy tego nie mieli - wowczas wartosc boolean, ktora prezentujemy w kolumnie "Badanie" bylaby prezentowana jako String - nie byloby checkBoxa */
		switch (column) {

		/* case Columns.EXAMINATION:
			return Boolean.class; */
		default:
			return String.class;

		}

	}

}
