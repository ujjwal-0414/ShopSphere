package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.AddressRequest;
import com.ujjwal.ecommerce.dto.response.AddressResponse;
import com.ujjwal.ecommerce.entity.Address;
import com.ujjwal.ecommerce.entity.User;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.exception.UnauthorizedException;
import com.ujjwal.ecommerce.mapper.AddressMapper;
import com.ujjwal.ecommerce.repository.AddressRepository;
import com.ujjwal.ecommerce.security.CustomUserDetails;
import com.ujjwal.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressResponse createAddress(AddressRequest request){
        User user = getAuthenticatedUser();

        Address address = new Address();
        address.setUser(user);
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());

        boolean makeDefault = Boolean.TRUE.equals(request.getIsDefault());
        if(makeDefault){
            removeExistingDefaultAddress(user);
        }
        address.setIsDefault(makeDefault);

        Address savedAddress = addressRepository.save(address);

        return addressMapper.toAddressResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {

        User user = getAuthenticatedUser();

        return addressRepository.findByUser(user)
                .stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long addressId) {

        User user = getAuthenticatedUser();

        Address address = addressRepository
                .findByIdAndUser(addressId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"));

        return addressMapper.toAddressResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(
            Long addressId,
            AddressRequest request
    ) {

        User user = getAuthenticatedUser();

        Address address = addressRepository
                .findByIdAndUser(addressId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"));

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());

        boolean makeDefault =
                Boolean.TRUE.equals(request.getIsDefault());

        if (makeDefault) {
            removeExistingDefaultAddress(user);
        }

        address.setIsDefault(makeDefault);

        Address updatedAddress =
                addressRepository.save(address);

        return addressMapper.toAddressResponse(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {

        User user = getAuthenticatedUser();

        Address address = addressRepository
                .findByIdAndUser(addressId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"));

        addressRepository.delete(address);
    }

    private void removeExistingDefaultAddress(User user) {

        addressRepository
                .findByUserAndIsDefaultTrue(user)
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    addressRepository.save(existingDefault);
                });
    }

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)){
            throw new UnauthorizedException(
                    "Authenticated user not found"
            );
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

}
