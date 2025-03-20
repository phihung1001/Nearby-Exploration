package com.example.foodtourbackend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecretKey jwtSecretKey() {
        //Chuyển chuỗi secret thành byte[] và tạo SecretKey an toàn cho HS512 (>= 512 bits)
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);    }
}
