package com.tyy.chapter02.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @author:tyy
 * @date:2020/11/30
 */
public class MyRealm1 implements Realm {
    public String getName() {
        return "myRealm1";
    }

    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName=(String)token.getPrincipal();
        char[] ch=(char[])token.getCredentials();
        StringBuilder password=new StringBuilder();
        for(char c:ch) password.append(c);
        if(!"zhang".equals(userName)){
            throw new UnknownAccountException();
        }
        if(!"123".equals(password.toString())){
            throw  new IncorrectCredentialsException();
        }

        return new SimpleAuthenticationInfo(userName,password,getName());
    }
}
