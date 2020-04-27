package com.sicredi.slc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sicredi.slc.generated.model.DOCComplexType;

@Repository
public interface SLCRepository extends JpaRepository<DOCComplexType, Long> {}
