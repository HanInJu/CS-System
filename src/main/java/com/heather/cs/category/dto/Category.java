package com.heather.cs.category.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
	private Long id;
    private Long parentId;
	private String name;
	private String useYn;
	private int depth;

	private List<Category> children;

	public void addChild(Category child) {
		if (Objects.isNull(children)) {
			children = new ArrayList<>();
		}

		children.add(child);
	}

}
