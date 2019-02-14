package dataOSM;

import java.util.ArrayList;
import java.util.List;

import dataOSM.PatientContener.PatientAddedListener;
import dataOSM.PatientContener.PatientEditedListener;
import dataOSM.PatientContener.PatientRemovedListener;

public class MedicineContener {

	List<Medicine> medicines= new ArrayList<>();
	List<MedicineRemovedListener> listenersRemove = new ArrayList<>();
	List<MedicineAddedListener> listeners = new ArrayList<>();
	List<MedicineEditedListener> listenersEdit = new ArrayList<>();
	
	public List<Medicine> getmedicinesList() {
		return medicines;
	}
	
	public void addPatient(Medicine m) {
		medicines.add(m);
		listeners.forEach(l -> l.medicineAdded(m)); 
	}
	
	public void deleteMedicine(Medicine m) {
		medicines.remove(m);
		listenersRemove.forEach(l -> l.medicineRemoved(m));
	}
	
	
	public void setpatientList(List<Medicine> medicines) {
		this.medicines = medicines;
	}

	public void addListener(MedicineAddedListener listener) {
		listeners.add(listener);
	}
	

	public void addListenerForEdit(MedicineEditedListener listener) {
		listenersEdit.add(listener);
	}

	public void addListenerForRemoval(MedicineRemovedListener listener) {
		listenersRemove.add(listener);
	}

	public void removeListener(MedicineRemovedListener listener) {
		listenersRemove.remove(listener);
	}

	public void removeListener(MedicineAddedListener listener) {
		listeners.remove(listener);
	}

	public interface MedicineAddedListener {
		void medicineAdded(Medicine m);
	}

	
	public interface MedicineRemovedListener {
		void medicineRemoved(Medicine m);
	}

	public interface MedicineEditedListener {
		void medicineEdited(Medicine m);
	}

	
	
}
