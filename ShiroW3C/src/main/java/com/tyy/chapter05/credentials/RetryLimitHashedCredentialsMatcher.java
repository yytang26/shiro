package com.tyy.chapter05.credentials;


import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>User: tyy
 * <p>Date: 2020-12-01
 * <p>Version: 1.0
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Ehcache passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager = CacheManager.newInstance(CacheManager.class.getClassLoader().getResource("src/main/resources/chapter05/ehcache.xml"));
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        Element element = passwordRetryCache.get(username);
        if(element == null) {
            element = new Element(username , new AtomicInteger(0));
            passwordRetryCache.put(element);
        }
        AtomicInteger retryCount = (AtomicInteger)element.getObjectValue();
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }else{
            element = new Element(username , retryCount.addAndGet(1));
            passwordRetryCache.put(element);
        }
        return matches;
//        boolean matches = super.doCredentialsMatch(token, info);
//        String username = (String)token.getPrincipal();
//        Element element = passwordRetryCache.get(username);
//        if(matches) {
//            if(element!=null){
//                //clear retry count
//                passwordRetryCache.remove(username);
//            }
//        }else{
//            if(element == null) {
//                element = new Element(username , new AtomicInteger(1));
//                passwordRetryCache.put(element);
//            }else{
//                AtomicInteger retryCount = (AtomicInteger)element.getObjectValue();
//                if(retryCount.incrementAndGet() > 5) {
//                    //if retry count > 5 throw
//                    throw new ExcessiveAttemptsException();
//                }
//                element =new Element(username,retryCount.addAndGet(1));
//                passwordRetryCache.put(element);
//            }
//        }
//        return matches;
    }
}
