package com.dv.oficina.dto.response;

import java.time.Instant;

public record TokenResponse(String token, Instant expiraEm) {}