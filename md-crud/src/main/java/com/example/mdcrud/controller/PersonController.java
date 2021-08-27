package com.example.mdcrud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mdcrud.model.Person;
import com.example.mdcrud.service.PersonService;

@RestController
@RequestMapping("person")
public class PersonController {

	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PostMapping
	public Person create(@RequestBody Person person) {
		return personService.insertPersonData(person);
	}

	@GetMapping
	public Iterable<Person> read() {
		return personService.getAllPersonInformation();
	}
}
