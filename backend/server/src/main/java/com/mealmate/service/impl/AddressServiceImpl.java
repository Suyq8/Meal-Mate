package com.mealmate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealmate.context.BaseContext;
import com.mealmate.entity.Address;
import com.mealmate.mapper.AddressMapper;
import com.mealmate.service.AddressService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getAllAddress(Address address){
        log.info("address: {}", address);
        return addressMapper.getAllAddress(address);
    }

    @Override
    public void addAddress(Address address){
        log.info("address: {}", address);
        addressMapper.insert(address);
    }

    @Override
    public Address getById(Long id){
        return addressMapper.getById(id);
    }

    @Override
    public void updateAddress(Address address){
        addressMapper.update(address);
    }

    @Override
    public void setDefaultAddress(Address address){
        // set all address to not default
        addressMapper.setDefaultByUserId(0, BaseContext.getCurrentId());

        // set the address to default
        address.setIsDefault(1);
        addressMapper.update(address);
    }

    @Override
    public void deleteById(Long id) {
        addressMapper.deleteById(id);
    }
}
