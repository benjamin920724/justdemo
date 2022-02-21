package com.example.assignment.biz.placesearch.dao;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface HotKeywordRepository extends JpaRepository<HotKeyword, String> {
	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select h from HotKeyword h where h.keyword = :keyword")
	HotKeyword findByIdForUpdate(@Param("keyword")String keyword);
}
