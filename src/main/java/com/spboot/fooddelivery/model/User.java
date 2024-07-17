package com.spboot.fooddelivery.model;

import java.util.Set;

import com.spboot.fooddelivery.model.entity.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="USER_AUTH")
public class User {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	@Column(name = "NAME")
	private String name;
	
	@NotBlank
	@Column(name = "USER_NAME")
	private String userName;

	@NotBlank
	@Column(name = "PASSWORD")
	private String password;
	
	@OneToOne(mappedBy="user")
	private Address address;
	
	@OneToOne(mappedBy="user")
	private ContactInfo contactInfo ;
	
	@OneToOne(mappedBy="user")
	private Cart cart;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "USER_ROLES",
            joinColumns={
                    @JoinColumn( name= "USER_ID", referencedColumnName= "id")
            },
            inverseJoinColumns= {
                    @JoinColumn (name = "ROLE_ID", referencedColumnName = "id")
            })
    private Set<Role> roles;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return List.of(new SimpleGrantedAuthority(roles));
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return userName;
//	}

}
