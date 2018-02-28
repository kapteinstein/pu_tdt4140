package tdt4140.gr1808.server;

public class Touple {
	
	private int puls;
	private String dateTime;
	
	public Touple(int puls, String dateTime) {
		this.puls = puls;
		this.dateTime = dateTime;
	}
	
	public int getPuls() {
		return puls;
	}
	
	public String getDateTime() {
		return dateTime;
	}

}
