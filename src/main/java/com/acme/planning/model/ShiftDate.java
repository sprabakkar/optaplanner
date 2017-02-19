package com.acme.planning.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ShiftDate {
	private String shiftId;
	private LocalDate workDate;
	//private DayOfWeek dow;

	public ShiftDate() {
		super();
	}

	public ShiftDate(String shiftId, LocalDate workDate, DayOfWeek dow) {
		super();
		this.shiftId = shiftId;
		this.workDate = workDate;
		//this.dow = dow;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	public DayOfWeek getDow() {
		return workDate.getDayOfWeek();
	}

/*	public void setDow(DayOfWeek dow) {
		this.dow = dow;
	}*/
}
