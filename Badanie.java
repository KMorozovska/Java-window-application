//Badanie: markery raka piersi

public class Badanie {
	
	Date data;
	boolean param1;		//obecność mutacji „5382insC"
	boolean param2;		//obecność mutacji „C61G,300T>G”
	int poziom_antyg;	//poziom antygenu CA 125
	int wynik;
	
	Badanie(boolean p1, boolean p2, int p_a) {
		
		this.param1 = p1;
		this.param2 = p2;
		this.poziom_antyg = p_a;
	}
	
	public void wynik_badania(Badanie a) {
		if(a.param1==true && a.param2==true && a.poziom_antyg > 120) a.wynik = 1;
		if(a.param1==false || a.param2==false || a.poziom_antyg < 120) a.wynik = 0;
	}
}
