package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.AddressRequest;
import com.ujjwal.ecommerce.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(AddressRequest request);

    List<AddressResponse> getMyAddresses();

    AddressResponse getAddressById(Long addressId);

    AddressResponse updateAddress(
            Long addressId,
            AddressRequest request
    );

    void deleteAddress(Long addressId);

}
