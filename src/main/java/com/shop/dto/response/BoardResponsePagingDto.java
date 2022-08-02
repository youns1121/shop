package com.shop.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponsePagingDto {

    private Long boardId;

    private String boardTitle;

    private String boardContents;

    private String delYn;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String boardFileYn;

    private String memberEmail;

    private String memberName;

}
