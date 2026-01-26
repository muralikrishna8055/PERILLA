package com.perilla.auth_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
public class RevokedToken {
    @Id
    private String token;
    private Date revokedAt;


}
