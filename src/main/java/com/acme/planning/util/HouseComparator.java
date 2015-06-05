package com.acme.planning.util;

import java.util.Comparator;

import com.acme.planning.model.HouseCleaningSpot;

public class HouseComparator implements Comparator<HouseCleaningSpot> {

	@Override
	public int compare(HouseCleaningSpot o1, HouseCleaningSpot o2) {
		return o1.getHouse().getId().compareTo(o2.getHouse().getId());
	}
}
