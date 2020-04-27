package com.sicredi.slc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sicredi.slc.generated.model.DOCComplexType;
import com.sicredi.slc.service.SLCService;

@RequestMapping("slc")
@RestController
public class SLCController {

	@Autowired
	private SLCService service;
	
	
	@GetMapping
	public ResponseEntity<List<DOCComplexType>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<DOCComplexType> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}
}
