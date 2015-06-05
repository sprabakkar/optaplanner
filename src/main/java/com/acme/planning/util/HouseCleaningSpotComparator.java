package com.acme.planning.util;

import java.util.Comparator;

import com.acme.planning.model.HouseCleaningSpot;

public class HouseCleaningSpotComparator implements
		Comparator<HouseCleaningSpot> {

	@Override
	public int compare(HouseCleaningSpot arg0, HouseCleaningSpot arg1) {
		return arg0.getHouse().getDayOfWeek().getDayId().compareTo(arg1.getHouse().getDayOfWeek().getDayId());
	}
}
