package dataOSM;

/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk
 * Klasa Patient reprezentuje pacjenta - definiuje wszystkie pola, jakie odpowiadaja pacjentowi.
 */
public class Patient { 

	private String name;
	private String surname;
	private String PESEL;
	private String sex;
	private String insurance;

	public Patient(String name, String surname, String PESEL, String sex, String insurance) {
		this.name = name;
		this.surname = surname;
		this.PESEL = PESEL;
		this.sex = sex;
		this.insurance = insurance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPESEL() {
		return PESEL;
	}

	public void setPESEL(String pesel) {
		PESEL = pesel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}



}
