package com.expertofordering.service;

import com.expertofordering.dto.MypageResponse;
import com.expertofordering.entity.Mypage;
import com.expertofordering.repository.MypageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final MypageRepository mypageRepository;

    public Optional<MypageResponse> getMonthlySummary(Long userId, String yearMonth) {
        return mypageRepository.findByUser_IdAndYearMonth(userId, yearMonth)
                .map(MypageResponse::new);
    }
}
