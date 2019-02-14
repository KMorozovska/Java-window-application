package dataOSM;

public class Medicine {

	private String name;
	private String quantity;
	private String date;
	
	public Medicine(String name, String quantity, String date)
	{
		this.setName(name);
		this.setQuantity(quantity);
		this.setDate(date);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
