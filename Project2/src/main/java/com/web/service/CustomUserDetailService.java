package com.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.entity.User;
import com.web.repository.UserRepository;
import com.web.security.UserPrincipal;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

	  @Override
	    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
	        User user = userRepository.findByUserId(userId);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found with userId: " + userId);
	        }
	        return new UserPrincipal(user);
	    }

        // 사용자의 권한을 설정함
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        if ("admin".equals(user.getUserId())) {
//            authorities.add(new SimpleGrantedAuthority("ADMIN"));
//        } else {
//            authorities.add(new SimpleGrantedAuthority("USER"));
//        }
//
//        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPassword(), authorities);
//    }

	
}
