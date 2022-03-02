package com.heather.cs.charger;

import java.util.Comparator;

import com.heather.cs.charger.dto.Charger;

public class ChargerComparableClass implements Comparator<Charger> {

	@Override
	public int compare(Charger charger1, Charger charger2) {
		if(charger1.getNumberOfCounsel() < charger2.getNumberOfCounsel()) {
			return -1;
		}
		if(charger1.getNumberOfCounsel() > charger2.getNumberOfCounsel()) {
			return 1;
		}
		return charger1.getUserId().compareTo(charger2.getUserId());
	}
}
