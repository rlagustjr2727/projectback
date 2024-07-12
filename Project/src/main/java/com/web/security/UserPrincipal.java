package com.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.entity.User;



public class UserPrincipal implements UserDetails {

    private User user;

    // Constructor
    public UserPrincipal(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
    
    public String getUserProfileImage() {
    	return user.getUserProfileImage();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 필요한 경우 권한 설정
    }

    public String getNickname() {
    	return user.getUserNickName();
    }
    
    public String getProfileImageUrl() {
        return user.getUserProfileImage();
    }
    
    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
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
