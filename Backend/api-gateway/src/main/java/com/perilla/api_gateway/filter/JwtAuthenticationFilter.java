package com.perilla.api_gateway.filter;



import com.perilla.api_gateway.security.JwtService;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
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

        // Auth APIs are public
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }

        String token = authHeader.substring(7);

        // Validate token
        jwtService.validateToken(token);

        // Propagate context headers
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

