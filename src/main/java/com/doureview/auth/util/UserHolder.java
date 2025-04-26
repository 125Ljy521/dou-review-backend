package com.doureview.auth.util;

import com.doureview.user.entity.User;

public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void save(User user) {
        tl.set(user);
    }

    public static User get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}