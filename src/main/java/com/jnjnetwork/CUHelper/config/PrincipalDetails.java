package com.jnjnetwork.CUHelper.config;

import com.jnjnetwork.CUHelper.domain.Role;
import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private UserService userService;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        List<Role> roles = userService.selectRolesById(user.getId());
        for(Role role : roles) {
            collect.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.getName();
                }
                @Override
                public String toString() {
                    return role.getName();
                }
            });
        }
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
}
