package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.mengCommon.backend.form.ManagerLoginInformation;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>application name：librarySystemPatient - UserDetailsServiceImpl</p>
 * <p>application describing： </p>
 * <p>copyright： </p>
 * <p>company： </p>
 * <p>time：2022-12-14 16:17:54</p>
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminLoginService adminLoginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取到用户名去数据库查询数据
        ManagerLoginInformation managerLoginInformation = adminLoginService.getAdminInformation(username);
        if (managerLoginInformation == null) {
            throw new UsernameNotFoundException("not find user, please Check your username");
        }
        int size = managerLoginInformation.getModelId().size();
        String[] strings = managerLoginInformation.getModelId().toArray(new String[size + 1]);
        strings[size] = "ROLE_" + managerLoginInformation.getRoleId();
        return new User(managerLoginInformation.getLoginEmail(), managerLoginInformation.getPassword(),
                AuthorityUtils.createAuthorityList(strings));
    }
}
