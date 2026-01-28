package com.perilla.api_gateway.filter;



import com.perilla.api_gateway.security.JwtService;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // ✅ Allow auth & OPTIONS requests without JWT
        if (path.startsWith("/api/auth")
                || exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // ❌ NEVER throw RuntimeException in Gateway
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        jwtService.validateToken(token);

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header("X-Tenant-Code", jwtService.extractTenant(token))
                .header("X-Role", jwtService.extractRole(token))
                .header("X-Employee-Code",
                        String.valueOf(jwtService.extractEmployeeCode(token)))
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }
}

