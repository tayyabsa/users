package com.tayyab.users.config.security;

import com.tayyab.users.entity.PrivilegesEntity;
import com.tayyab.users.entity.RoleEntity;
import com.tayyab.users.entity.UsersEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class UsersPrinciple implements UserDetails {

    private UsersEntity usersEntity;
    private Long userId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<PrivilegesEntity> privilegesEntities = new HashSet<>();

        // Get user Roles
        Collection<RoleEntity> roles = usersEntity.getRoles();

        if(roles == null) return authorities;

        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole())); //role name
            privilegesEntities.addAll(role.getPrivileges());
        });

        privilegesEntities.forEach((privilegesEntity) ->{
            authorities.add(new SimpleGrantedAuthority(privilegesEntity.getPrivilege())); // privileges entity
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.usersEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.usersEntity.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UsersPrinciple(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    public UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
