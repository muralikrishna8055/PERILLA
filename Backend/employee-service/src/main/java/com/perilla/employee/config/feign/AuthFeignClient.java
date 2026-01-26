package com.perilla.employee.config.feign;

import com.perilla.employee.dto.request.AuthUserCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "auth-service", url = "http://localhost:8081")
public interface AuthFeignClient {

    @PostMapping("/api/internal/auth/register-user")
    void createUser(@RequestBody AuthUserCreateRequest request);
}

