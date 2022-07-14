package com.shop.repository;


import com.shop.domain.Board;
import com.shop.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

    List<BoardFile> findByBoard(Board board);
}
