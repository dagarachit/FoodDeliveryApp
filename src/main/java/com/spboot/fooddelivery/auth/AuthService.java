package com.spboot.fooddelivery.auth;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spboot.fooddelivery.dto.LoginDto;
import com.spboot.fooddelivery.dto.RegisterDto;
import com.spboot.fooddelivery.jwt.security.JwtTokenProvider;
import com.spboot.fooddelivery.model.Address;
import com.spboot.fooddelivery.model.ContactInfo;
import com.spboot.fooddelivery.model.User;
import com.spboot.fooddelivery.model.entity.Role;
import com.spboot.fooddelivery.repository.AddressRepository;
import com.spboot.fooddelivery.repository.ContactInfoRepository;
import com.spboot.fooddelivery.repository.RoleRepository;
import com.spboot.fooddelivery.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Autowired
    private AddressRepository addressRepository;

    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUserName(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public void register(RegisterDto registerDto) {
        Optional<User> userOptional = userRepository.findByUserName(registerDto.getUserName());
        if (userOptional.isPresent()) {
            throw new RuntimeException(MessageFormat.format("User with email ({0}) already exists",
                    registerDto.getUserName()));
        }
        User user = new User();
        user.setUserName(registerDto.getUserName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setName(registerDto.getName());
        Address address= new Address();
        address.setAddressLine(registerDto.getAddressLine());
        address.setCity(registerDto.getCity());
        address.setCountry(registerDto.getCountry());
        address.setLatitude(registerDto.getLatitude());
        address.setLongitude(registerDto.getLongitude());
        address.setPincode(registerDto.getPincode());
        address.setState(registerDto.getState());

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(registerDto.getEmail());
        contactInfo.setPhoneNumber(registerDto.getPhoneNumber());

        Role role = roleRepository.findByName(Role.CUSTOMER);
        user.setRoles(Set.of(role));
        user.setAddress(address);
        user.setContactInfo(contactInfo);
        contactInfoRepository.save(contactInfo);
        addressRepository.save(address);
        address.setUser(user);
        contactInfo.setUser(user);
        userRepository.save(user);
        
    }

}
