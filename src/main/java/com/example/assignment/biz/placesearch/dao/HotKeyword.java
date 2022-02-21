package com.example.assignment.biz.placesearch.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name="KEYWORD")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotKeyword {

	@Id
    private String keyword;

	@NotNull
    private Integer count = 0;

}
