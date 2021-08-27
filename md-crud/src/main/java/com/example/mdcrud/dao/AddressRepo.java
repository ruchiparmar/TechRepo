package com.example.mdcrud.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mdcrud.model.Address;

public interface AddressRepo extends MongoRepository<Address, Long>
{
}
