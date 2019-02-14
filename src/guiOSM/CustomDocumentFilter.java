package guiOSM;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
<<<<<<< Updated upstream
 * @author Katarzyna Spaliñska & Ewelina Trochimiuk 
=======
 * @author Katarzyna SpaliÅ„ska & Ewelina Trochimiuk  
>>>>>>> Stashed changes
 * Klasa CustomDocumentFilter sluzy do sprawdzenia, czy wprowadzony ciag znakow jest zgodny z narzuconym wzorcem
 *
 */
public class CustomDocumentFilter extends DocumentFilter {

	
	/**
	 * regex - wzorzec w postaci wyrazenia regularnego
	 */
	String regex; 

	public CustomDocumentFilter(String regex) {
		this.regex = regex;
	}

	/*
	  fb - dane na ktorych cos robimy
	  str - tekst ktory doklejamy do tego co mamy obecnie 
	 */
	@Override
	public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException { 
		if (str == null) { 
			return;
		}

		String text = fb.getDocument().getText(0, fb.getDocument().getLength()); //pobieramy obecna wartosc danego pola, ktorego poprawnosc chcemy sprawdzic
		text += str; // doklejamy kolejny znak

		if (text.matches(regex)) { //jezeli wprowadzony dotad tekst spelnia wzorzec, to akceptujemy go i prezentujemy w polu poddawanym walidacji
			super.insertString(fb, offs, str, a);
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs)
			throws BadLocationException {
		if (str == null) {
			return;
		}

		String text = fb.getDocument().getText(0, fb.getDocument().getLength());
		text += str;

		if (text.matches(regex)) {
			super.replace(fb, offset, length, str, attrs);
		}

	}
}