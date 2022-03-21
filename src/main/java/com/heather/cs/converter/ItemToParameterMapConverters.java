package com.heather.cs.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

public class ItemToParameterMapConverters {
	public static <T> Converter<T, Map<String, Object>> createItemToParameterMapConverter() {
		return item -> {
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("item", item);
			return parameter;
		};
	}

}
