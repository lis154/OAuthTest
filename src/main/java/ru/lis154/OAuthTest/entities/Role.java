package ru.lis154.OAuthTest.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{
    USER;

    @Override
    public String getAuthority()
    {
        return name();
    }
}
