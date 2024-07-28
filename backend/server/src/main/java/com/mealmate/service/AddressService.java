package com.mealmate.service;

import java.util.List;

import com.mealmate.entity.Address;

public interface AddressService {
    /**
     * get all address
     * @param address
     * @return
     */
    List<Address> getAllAddress(Address address);

    /**
     * add address
     * @param address
     */
    void addAddress(Address address);

    /**
     * get address by id
     * @param id
     * @return
     */
    Address getById(Long id);

    /**
     * update address
     * @param address
     */
    void updateAddress(Address address);

    /**
     * set default address
     * @param address
     */
    void setDefaultAddress(Address address);

    /**
     * delete address by id
     * @param id
     */
    void deleteById(Long id);
}
