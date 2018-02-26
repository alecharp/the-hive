package com.cloudbees.hive.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Adrien Lecharpentier
 */
@Entity
public class Bee implements OAuth2User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private String location;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "bee_role", joinColumns = @JoinColumn(name = "bee_id"))
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles = new HashSet<>(1);

    private Bee() {
    }

    public Bee(String name, String email, Role role) {
        this(name, email, 0, 0, null, role);
    }

    public Bee(String name, String email, double latitude, double longitude, String location, Role role) {
        this.name = name;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.roles.add(role);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
              .map(role -> new SimpleGrantedAuthority(role.name()))
              .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("name", getName());
        stringObjectHashMap.put("email", getEmail());
        stringObjectHashMap.put("roles", getAuthorities());
        return stringObjectHashMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bee bee = (Bee) o;
        return Objects.equals(name, bee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
