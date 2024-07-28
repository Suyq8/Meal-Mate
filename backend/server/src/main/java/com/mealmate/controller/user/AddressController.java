package com.mealmate.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmate.constant.MessageConstant;
import com.mealmate.context.BaseContext;
import com.mealmate.entity.Address;
import com.mealmate.result.Result;
import com.mealmate.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("UserAddressController")
@RequestMapping("/user/addressBook")
@Tag(name = "Address Controller (user)")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    @Operation(summary = "get all address")
    public Result<List<Address>> getAllAddress() {
        Address address = Address.builder()
                .userId(BaseContext.getCurrentId())
                .build();

        List<Address> list = addressService.getAllAddress(address);
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "add address")
    public Result<String> addAddress(@RequestBody Address address) {
        log.info("add address: {}", address);
        address.setUserId(BaseContext.getCurrentId());
        address.setIsDefault(0);
        
        addressService.addAddress(address);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get address by id")
    public Result<Address> getById(@PathVariable Long id) {
        log.info("get address by id: {}", id);
        Address address = addressService.getById(id);
        return Result.success(address);
    }

    @PutMapping
    @Operation(summary = "update address")
    public Result<String> updateAddress(@RequestBody Address address) {
        log.info("update address: {}", address);
        addressService.updateAddress(address);
        return Result.success();
    }

    @PutMapping("/default")
    @Operation(summary = "set default address")
    public Result<String> setDefaultAddress(@RequestBody Address address) {
        addressService.setDefaultAddress(address);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "delete address by id")
    public Result<String> deleteById(Long id) {
        addressService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/default")
    @Operation(summary = "get default address")
    public Result<Address> getDefaultAddress() {
        Address address = Address.builder()
                .userId(BaseContext.getCurrentId())
                .isDefault(1)
                .build();

        List<Address> defaultAddress = addressService.getAllAddress(address);
        if (defaultAddress != null && !defaultAddress.isEmpty()) {
            return Result.success(defaultAddress.get(0));
        }

        return Result.error(MessageConstant.DEFAULT_ADDRESS_NOT_FOUND);
    }
}
