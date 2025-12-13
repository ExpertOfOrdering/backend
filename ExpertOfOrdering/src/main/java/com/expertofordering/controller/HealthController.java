package com.expertofordering.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return " EC2 서버 헬스체크 엔드포인트 : 서버 작동중 ";
    }
}
