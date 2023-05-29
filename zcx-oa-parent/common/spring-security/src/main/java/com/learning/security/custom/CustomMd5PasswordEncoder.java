package com.learning.security.custom;

import com.learning.common.util.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encrypt(String.valueOf(charSequence));
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(MD5.encrypt(String.valueOf(charSequence)));
    }
}
