package com.heather.cs.charger.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("charger")
public class Charger implements Comparable<Charger> {
	private String userId;
	private int numberOfCounsel;

	@Override
	public int compareTo(Charger o) {
		if(this.numberOfCounsel < o.numberOfCounsel) {
			return -1;
		}
		if(this.numberOfCounsel > o.numberOfCounsel) {
			return 1;
		}
		return this.userId.compareTo(o.userId);
	}
}
