package com.sicredi.slc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sicredi.slc.generated.model.DOCComplexType;
import com.sicredi.slc.repository.SLCRepository;

@Service
public class SLCService {

	@Autowired
	private SLCRepository repository;
	
	
	public List<DOCComplexType> findAll() {
		return repository.findAll();
	}

	public DOCComplexType findById(Long id) {
		return repository.findById(id).get();
	}
}
