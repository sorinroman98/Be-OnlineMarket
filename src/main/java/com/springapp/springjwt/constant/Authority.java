package com.springapp.springjwt.constant;

public class Authority {
    private Authority(){}

    private static final String USER_READ = "user:read";
    private static final String USER_UPDATE = "user:update";
    private static final String USER_CREATE = "user:create";
    private static final String USER_DELETE = "user:delete";

    public static final String[] USER_AUTHORITIES = {USER_READ};
    public static final String[] HR_AUTHORITIES =   {USER_READ,USER_UPDATE};
    public static final String[] MANAGER_AUTHORITIES = {USER_READ,USER_UPDATE};
    public static final String[] ADMIN_AUTHORITIES = {USER_READ,USER_UPDATE,USER_CREATE};
    public static final String[] SUPER_ADMIN_AUTHORITIES = {USER_READ,USER_UPDATE,USER_CREATE,USER_DELETE};
}
