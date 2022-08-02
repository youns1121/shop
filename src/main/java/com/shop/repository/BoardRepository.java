package com.shop.repository;

import com.shop.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardIdAndDelYn(Long id, String delYn);

    List<Board> findByDelYnOrderByCreateTimeDesc(String delYn);
}
