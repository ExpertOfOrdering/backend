package com.expertofordering.repository;

import com.expertofordering.entity.Mypage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MypageRepository extends JpaRepository<Mypage, Long> {
    Optional<Mypage> findByUser_IdAndYearMonth(Long userId, String yearMonth);

}
