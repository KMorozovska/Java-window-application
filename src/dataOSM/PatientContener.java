package dataOSM;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Katarzyna Spalińska & Ewelina Trochimiuk Klasa PatientContener jest
 *         klasa, w ktorej znajduje sie lista pacjentow. Posiada rowniez metody,
 *         ktore zostaly stworzone, aby wykonywac rozne operacje na liscie
 *         pacjentow.
 */
public class PatientContener {

	/**
	 * Lista sluchaczy zdarzenia, jakim jest dodanie nowego pacjenta do listy pacjentow.
	 */
	List<PatientAddedListener> listeners = new ArrayList<>();
	
	/**
	 * Lista sluchaczy zdarzenia, jakim jest usuniecie pacjenta z listy pacjentow. 
	 */
	List<PatientRemovedListener> listenersRemove = new ArrayList<>();

	/**
	 * Lista sluchaczy zdarzenia, jakim jest edycja istniejacego pacjenta.
	 */
	List<PatientEditedListener> listenersEdit = new ArrayList<>();

	/**
	 * Lista pacjentow (obiektow klasy Patient)
	 */
	
	List<Patient> patientList = new ArrayList<Patient>();

	
	/**
	 * Metoda do dodawania nowego pacjenta do listy
	 * @param p - pacjent, ktory ma byc dodany
	 */
	public void addPatient(Patient p) {
		patientList.add(p);

		listeners.forEach(l -> l.patientAdded(p)); 
	}
	
	/**
	 * Metoda do usuwania pacjenta z listy
	 * @param p - pacjent, ktory ma byc byc usuniety
	 */
	public void deletePatient(Patient p) {
		patientList.remove(p);
		listenersRemove.forEach(l -> l.patientRemoved(p));
	}

	/**
	 * Metoda do edycji pacjenta znajdujacego sie juz na liscie
	 * @param p - pacjent do edycji
	 * @param ID - PESEL pacjenta, ktory ma byc edytowany 
	 * @return - PESEL pacjenta po edycji
	 */
	public String editPatient(Patient p, String ID) {
		for (int i = 0; i < patientList.size(); i++) {
			if (patientList.get(i).getPESEL().equals(ID)) {
				patientList.get(i).setPESEL(p.getPESEL());
				patientList.get(i).setName(p.getName());
				patientList.get(i).setSurname(p.getSurname());
				patientList.get(i).setSex(p.getSex());
				patientList.get(i).setInsurance(p.getInsurance());
				listenersEdit.forEach(l -> l.patientEdited(p));

				return patientList.get(i).getPESEL();
			}
		}
		return null;
	}

	public List<Patient> getpatientList() {
		return patientList;
	}

	public void setpatientList(List<Patient> patientList) {
		this.patientList = patientList;
	}

	public void addListener(PatientAddedListener listener) {
		listeners.add(listener);
	}
	

	public void addListenerForEdit(PatientEditedListener listener) {
		listenersEdit.add(listener);
	}

	public void addListenerForRemoval(PatientRemovedListener listener) {
		listenersRemove.add(listener);
	}

	public void removeListener(PatientRemovedListener listener) {
		listenersRemove.remove(listener);
	}

	public void removeListener(PatientAddedListener listener) {
		listeners.remove(listener);
	}

	public interface PatientAddedListener {
		void patientAdded(Patient patient);
	}

	
	public interface PatientRemovedListener {
		void patientRemoved(Patient patient);
	}

	public interface PatientEditedListener {
		void patientEdited(Patient patient);
	}

}