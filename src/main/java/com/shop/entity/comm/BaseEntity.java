package com.shop.entity.comm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class}) //해당 클래스에 Auditing 기능을 포함
public class BaseEntity {

    @CreatedDate // Entity가 생성되어 저장될 때 시간이 자동 저장
    @Column(name="create_time", nullable = false)
    private LocalDateTime createTime; //생성시간

    @LastModifiedDate // 조회한 Entity의 값을 변경할 때 시간이 자동 저장
    @Column(name="update_time", nullable = false)
    private LocalDateTime updateTime;  //수정시간

    @Column(name="del_yn", nullable = false, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String delYn; //삭제여부


}
