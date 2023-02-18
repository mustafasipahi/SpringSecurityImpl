package com.custom;

import com.constant.Role;
import com.entity.User;
import com.util.DateUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class CustomUserDetail implements UserDetails {

    private final String username;
    private final String password;
    private final boolean locked;
    private final boolean enabled;
    private final Date createDate;
    private final Role role;

    public CustomUserDetail(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.locked = user.isLocked();
        this.enabled = user.isEnabled();
        this.createDate = user.getCreateDate();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (createDate == null) {
            return true;
        }
        final Date now = DateUtil.nowAsDate();
        final Date before = DateUtil.sub(now, Duration.ofDays(180));
        return createDate.compareTo(before) >= 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
