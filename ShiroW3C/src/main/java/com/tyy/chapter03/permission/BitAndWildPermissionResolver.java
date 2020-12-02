package com.tyy.chapter03.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.text.IniRealm;

/**
 * <p>User: tyy
 * <p>Date: 20-12-01
 * <p>Version: 1.0
 */
public class BitAndWildPermissionResolver implements PermissionResolver {

    public Permission resolvePermission(String permissionString) {
        if(permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
