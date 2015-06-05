package com.acme.planning.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.acme.planning.model.DayOfWeek;
import com.acme.planning.model.HouseCleaningSpot;

public class CleaningHouseDifficultyComparator implements Comparator<HouseCleaningSpot>, Serializable {


	private static final long serialVersionUID = 1L;
	private static final HashMap<DayOfWeek, Object> countMap = new HashMap<DayOfWeek, Object>();

	public int compare(HouseCleaningSpot a, HouseCleaningSpot b) {
        return new CompareToBuilder()
                // TODO experiment with (aLatitude - bLatitude) % 10
        		.append(countMap.get(a.getHouse().getDayOfWeek()), countMap.get(b.getHouse().getDayOfWeek()))
                .append(a.getId(), b.getId())
                .append(a.getCleaningSpotIndex(), b.getCleaningSpotIndex())
                .toComparison();
    }

}
