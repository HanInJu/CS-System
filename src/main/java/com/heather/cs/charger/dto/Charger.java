package com.heather.cs.charger.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("charger")
public class Charger {
	private String userId;
	private int numberOfCounsel;
}
