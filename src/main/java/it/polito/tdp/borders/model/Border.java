package it.polito.tdp.borders.model;

public class Border {
	
	private Country state1;
	private Country state2;
	private int year;
	private int conttype;
	
	public Border(Country state1, Country state2, int year, int conttype) {
		this.state1=state1;
		this.state2=state2;
		this.year=year;
		this.conttype=conttype;
	}

	public Country getState1() {
		return state1;
	}

	public void setState1(Country state1) {
		this.state1 = state1;
	}

	public Country getState2() {
		return state2;
	}

	public void setState2(Country state2) {
		this.state2 = state2;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getConttype() {
		return conttype;
	}

	public void setConttype(int conttype) {
		this.conttype = conttype;
	}

}
