package dataOSM;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;

public class OurDataBase {

	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL = "jdbc:derby:DawkowanieLekow;create=true"; // DawkowanieLekow
																					// -
																					// nazwa
																					// bazy
																					// danych
	private Connection connection = null;
	private DatabaseMetaData dbmd = null;
	private Statement statement = null;
	private ResultSet result = null;
	private MedicineContener medicineContener;
	private PatientContener patientContener;
	private int suma, id;

	public void createDB() throws ClassNotFoundException, SQLException {

		Class.forName(DRIVER);
		connection = DriverManager.getConnection(JDBC_URL);

		// pobranie informacji o bazie danych i utworzenie obiektu zapytania
		dbmd = connection.getMetaData();
		statement = connection.createStatement();

		result = dbmd.getTables(null, null, "PACJENCI", null);
		if (!result.next()) {
			statement.execute("CREATE TABLE PACJENCI" + "("
					+ "id_pacjenta INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
					+ "imie VARCHAR(20) NOT NULL," + "nazwisko VARCHAR(40) NOT NULL," + "pesel CHAR(11) NOT NULL,"
					+ "plec VARCHAR(10) NOT NULL," + "ubezpieczenie VARCHAR(40) NOT NULL" + ")");

			System.out.println("Utworzono tabele PACJENCI");
		} else
			System.out.println("Tabela PACJENCI juz istnieje");

		result = dbmd.getTables(null, null, "LEKI", null);
		if (!result.next()) {
			statement.execute("CREATE TABLE LEKI ("
					+ "id_leku INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
					+ "nazwa VARCHAR(40) NOT NULL," + "data_waznosci DATE NOT NULL," + "dostepna_ilosc INT NOT NULL )");

			System.out.println("Utworzono tabele LEKI");
		} else
			System.out.println("Tabela LEKI juz istnieje");

		result = dbmd.getTables(null, null, "PACJENCI_LEKI", null);
		if (!result.next()) {
			statement.execute("CREATE TABLE PACJENCI_LEKI ("
					+ "id_zwiazku INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
					+ "id_pacjenta INT NOT NULL, FOREIGN KEY (id_pacjenta) REFERENCES PACJENCI(id_pacjenta),"
					+ "id_leku INT NOT NULL, FOREIGN KEY (id_leku) REFERENCES LEKI(id_leku)," + "dawka INT NOT NULL,"
					+ "ile_razy_dziennie INT NOT NULL," + "ile_juz_podano INT )");

			System.out.println("Utworzono tabele PACJENCI_LEKI");
		} else
			System.out.println("Tabela PACJENCI_LEKI juz istnieje");
	}

	public void addPatient(Patient patient) throws SQLException {

		String name = patient.getName();
		String surname = patient.getSurname();
		String PESEL = patient.getPESEL();
		String sex = patient.getSex();
		String insurance = patient.getInsurance();

		// dbmd=connection.getMetaData();
		statement = connection.createStatement();
		statement.execute("INSERT INTO PACJENCI (imie,nazwisko,pesel,plec,ubezpieczenie)" + "VALUES(" + "'" + name + "'"
				+ "," + "'" + surname + "'" + "," + "'" + PESEL + "'" + "," + "'" + sex + "'" + "," + "'" + insurance
				+ "'" + ")");
		System.out.println("Dodano pacjenta!");

	}

	public void addMedicine(Medicine medicine) throws SQLException {
		String name = medicine.getName();
		String date = medicine.getDate();
		int quantity = Integer.parseInt(medicine.getQuantity());

		statement = connection.createStatement();
		statement.execute("INSERT INTO LEKI (nazwa,data_waznosci,dostepna_ilosc)" + "VALUES(" + "'" + name + "'" + ","
				+ "'" + date + "'" + "," + quantity + ")");

		System.out.println("Dodano lek!");
	}

	public void addMedicinePatient(String name, String PESEL, String portion, String timesDaily) throws SQLException {
		int medicinePortion = Integer.parseInt(portion);
		int medicineTimesDaily = Integer.parseInt(timesDaily);

		statement = connection.createStatement();
		statement.execute("INSERT INTO PACJENCI_LEKI (id_pacjenta,id_leku,dawka,ile_razy_dziennie)"
				+ "VALUES ((SELECT id_pacjenta FROM PACJENCI WHERE pesel = " + "'" + PESEL + "'" + ")" + ","
				+ "(SELECT id_leku FROM LEKI WHERE nazwa = " + "'" + name + "'" + ")" + "," + medicinePortion + ","
				+ medicineTimesDaily + ")");
		System.out.println("Dodano lek do pacjenta!");

	}

	public MedicineContener getMedicines() throws SQLException {
		medicineContener = new MedicineContener();
		result = statement.executeQuery("SELECT * FROM LEKI ORDER BY nazwa");
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (result.next()) {
			String s = formatter.format(result.getDate("data_waznosci"));
			Medicine medicine = new Medicine(result.getString("nazwa"),
					Integer.toString(result.getInt("dostepna_ilosc")), s);
			medicineContener.medicines.add(medicine);
		}
		return medicineContener;

	}

	public void showMedicines() throws SQLException {
		result = statement.executeQuery("SELECT * FROM LEKI ORDER BY nazwa");
		while (result.next())
			System.out.println(result.getInt("id_leku") + "\t" + result.getString("nazwa") + "\t"
					+ result.getString("data_waznosci") + "\t" + result.getInt("dostepna_ilosc"));
	}

	public void showPatients() throws SQLException {
		result = statement.executeQuery("SELECT * FROM PACJENCI ORDER BY nazwisko");
		while (result.next())
			System.out.println(result.getInt("id_pacjenta") + "\t" + result.getString("imie") + "\t"
					+ result.getString("nazwisko") + "\t" + result.getString("pesel") + "\t" + result.getString("plec")
					+ "\t" + result.getString("ubezpieczenie"));
	}

	public void showGivenMedicines() throws SQLException {
		result = statement.executeQuery("SELECT * FROM PACJENCI_LEKI");
		while (result.next())
			System.out.println(result.getInt("id_zwiazku") + "\t" + result.getInt("ile_juz_podano") + "\t"
					+ result.getInt("dawka"));
	}

	public void showPatientsMedicines() throws SQLException {
		result = statement.executeQuery("SELECT * FROM PACJENCI_LEKI");
		String patientsMeds = new String();
		while (result.next()) {
			ResultSet result2;
			result2 = statement.executeQuery("SELECT FROM PACJENCI_LEKI where id_leku =" + result.getInt("id_leku"));
			patientsMeds += result2.getString("nazwa") + " ";
			System.out.println(patientsMeds);
		}
	}

	public PatientContener getPatients() throws SQLException {
		patientContener = new PatientContener();
		result = statement.executeQuery("SELECT * FROM PACJENCI ORDER BY id_pacjenta");
		while (result.next()) {
			Patient patient = new Patient(result.getString("imie"), result.getString("nazwisko"),
					result.getString("pesel"), result.getString("plec"), result.getString("ubezpieczenie"));
			patientContener.patientList.add(patient);
		}

		return patientContener;

	}

	public void drop_table() throws SQLException {
		statement.execute("DROP TABLE PACJENCI_LEKI");
		statement.execute("DROP TABLE LEKI");
		statement.execute("DROP TABLE PACJENCI");
	}

	public void editPatient(Patient patient, String PESEL) throws SQLException {

		String name = patient.getName();
		String surname = patient.getSurname();
		String newPESEL = patient.getPESEL();
		String sex = patient.getSex();
		String insurance = patient.getInsurance();

		String sql = "UPDATE PACJENCI SET imie=" + "'" + name + "'" + ",nazwisko=" + "'" + surname + "'" + ",pesel="
				+ "'" + newPESEL + "'" + ",plec=" + "'" + sex + "'" + ",ubezpieczenie=" + "'" + insurance + "'"
				+ "WHERE pesel=" + "'" + PESEL + "'";
		statement.executeUpdate(sql);
	}

	public void deletePatient(String PESEL) throws SQLException {

		result = statement.executeQuery("SELECT * FROM PACJENCI WHERE pesel=" + "'" + PESEL + "'");
		while (result.next()) {
			System.out.println("PESEL pacjenta do usuniecia: " + result.getInt("id_pacjenta"));
			Statement statement2 = connection.createStatement();
			statement2.execute("DELETE from PACJENCI_LEKI where id_pacjenta =" + result.getInt("id_pacjenta"));
			Statement statement3 = connection.createStatement();
			statement3.execute("DELETE from PACJENCI where id_pacjenta =" + result.getInt("id_pacjenta"));
		}
		System.out.println("Usunieto pacjenta z bazy danych");

	}

	public void deleteMedicine(String Name) throws SQLException {

		result = statement.executeQuery("SELECT * FROM LEKI WHERE nazwa=" + "'" + Name + "'");
		while (result.next()) {
			System.out.println("Nazwa leku do usuniecia: " + result.getString("nazwa"));
			System.out.println("ID leku do usuniecia: " + result.getInt("id_leku"));
			Statement statement2 = connection.createStatement();
			statement2.execute("DELETE from PACJENCI_LEKI where id_leku =" + result.getInt("id_leku"));
			Statement statement3 = connection.createStatement();
			statement3.execute("DELETE from LEKI where nazwa =" + "'" + Name + "'");
		}
		System.out.println("Usunieto lek z bazy danych");

	}

	public void giveMedicine() throws SQLException {

		// Statement statement3 = connection.createStatement();

		statement.execute("UPDATE PACJENCI_LEKI AS lPat SET lPat.ile_juz_podano = lPat.ile_juz_podano + lPat.dawka");

		result = statement.executeQuery("SELECT sum(dawka) as Suma, id_leku from PACJENCI_LEKI group by id_leku");

		while (result.next()) {

			suma = result.getInt(1);
			id = result.getInt(2);
			Statement statement2 = connection.createStatement();
			statement2.execute("UPDATE LEKI SET dostepna_ilosc=dostepna_ilosc-" + suma + "WHERE id_leku = " + id);
			showMedicines();
		}
	}

	public void finishDB() throws SQLException {
		connection.close();
		System.out.println("\nZamknieto polaczenie z baza danych DawkowanieLekow");
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se) {
			System.out.println("Zamknieto silnik bazodanowy");
		}
	}

}
