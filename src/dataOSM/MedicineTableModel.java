package dataOSM;

import java.util.List;

import javax.swing.table.AbstractTableModel;


/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk 
 * Klasa PatientTableModel definuje nasz wlasny model tabeli dostosowany do potrzeb aplikacji
 */
public class MedicineTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<Medicine> medicines;
	private String[] columnNames;
	private String name;

	public MedicineTableModel(List<Medicine> medicines, String[] columnNames) { //przyjmuje liste pacjentow oraz tablice z nazwami kolumn tabeli
		this.medicines = medicines;
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
		return medicines.size();
	}

	public static class Columns { // mappowanie dla poszczegolnych kolumn tabeli
		public static final int NAME = 0;
		public static final int QUANTITY = 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { // Pozyskiwanie wartosci danej komorki tabeli

		Medicine medicine = medicines.get(rowIndex); 

		switch (columnIndex) {
		case Columns.NAME: {
			name = medicine.getName();
			return name;
		}


		case Columns.QUANTITY:
			return medicine.getQuantity();

		default:
			return "";
		}
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) { //Ustawianie wartosci danej komorki tabeli (stosowane przy edycji danego rekordu)

		Medicine medicine = medicines.get(rowIndex);

		switch (columnIndex) {
		case Columns.NAME: 
		medicine.setName(value.toString());
		
		case Columns.QUANTITY:
			medicine.setQuantity(value.toString());

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
