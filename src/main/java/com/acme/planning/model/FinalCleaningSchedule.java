package com.acme.planning.model;

import java.time.LocalDate;

public class FinalCleaningSchedule {
	private String houseId;
	private String houseCleaningSpot;
	private String dayId;
	private String cleanerId;
	private LocalDate shiftDate;

	public FinalCleaningSchedule() {
		super();
	}

	public FinalCleaningSchedule(String houseId, String houseCleaningSpot,
			String dayId, String cleanerId) {
		super();
		this.houseId = houseId;
		this.houseCleaningSpot = houseCleaningSpot;
		this.dayId = dayId;
		this.cleanerId = cleanerId;
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getHouseCleaningSpot() {
		return houseCleaningSpot;
	}

	public void setHouseCleaningSpot(String houseCleaningSpot) {
		this.houseCleaningSpot = houseCleaningSpot;
	}

	public String getDayId() {
		return dayId;
	}

	public void setDayId(String dayId) {
		this.dayId = dayId;
	}

	public String getCleanerId() {
		return cleanerId;
	}

	public void setCleanerId(String cleanerId) {
		this.cleanerId = cleanerId;
	}

	public LocalDate getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(LocalDate shiftDate) {
		this.shiftDate = shiftDate;
	}

	@Override
	public String toString() {
		return "FinalCleaningSchedule [houseId=" + houseId
				+ ", houseCleaningSpot=" + houseCleaningSpot + ", dayId="
				+ dayId + ", cleanerId=" + cleanerId + "]";
	}
}
