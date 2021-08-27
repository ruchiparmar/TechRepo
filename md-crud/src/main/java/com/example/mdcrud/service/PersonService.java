package com.example.mdcrud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.mdcrud.dao.AddressRepo;
import com.example.mdcrud.dao.PersonRepo;
import com.example.mdcrud.model.Address;
import com.example.mdcrud.model.Person;

@Service
public class PersonService {

	private final PersonRepo personRepo;
	private final AddressRepo addressRepo;
	
	@Autowired
	MongoTemplate mongoTemplate;

	public PersonService(PersonRepo personRepo, AddressRepo addressRepo) {
		this.personRepo = personRepo;
		this.addressRepo = addressRepo;
	}

	public Person insertPersonData(Person person) {
		Person personAchilles = new Person();
		personAchilles.setPersonId(1l);
		personAchilles.setName("Person11");
		personRepo.save(personAchilles);

		Address address = new Address(1, "221b Baker Street", "London NW1",
				"London", 12345l);
		Address add = addressRepo.save(address);
		
		Person personHektor = new Person();
		personHektor.setPersonId(12l);
		personHektor.setName("Person12");
		List<Address> addresses = personHektor.getAddresses();
		addresses.add(add);
		personHektor.setAddresses(addresses);
		
		personRepo.save(personHektor);
		return personHektor;
	}

	public Iterable<Person> getAllPersonInformation() {
		Iterable<Person> personList = personRepo.findAll();
		System.out.println("Person List : ");
		for (Person person : personList) {
			person.getAddresses();
			System.out.println(person);
		}
		
		
		List<Person> lstP = mongoTemplate.findAll(Person.class);
		
		System.out.println("Person List from template: ");
		for (Person person : lstP) {
			person.getAddresses();
			System.out.println(person);
		}
		
		return personList;
	}
}
