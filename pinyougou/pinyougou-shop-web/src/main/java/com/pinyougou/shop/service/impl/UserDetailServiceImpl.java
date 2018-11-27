package com.pinyougou.shop.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    //用户在前端页面输入的用户名
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //构造用户的角色列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //将使用前端输入的密码与给定的密码进行匹配，如果一致则登录认证成功
        return new User(username, "123456", authorities);
    }
}
