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

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing을 적용하기 위해
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용함, 부모 클래슬르 상속 받는 자식 클래스에 매핑 정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate // Entity가 생성되어 저장될 때 시간이 자동 저장
    @Column(updatable = false)
    private LocalDateTime createTime; //생성시간

    @LastModifiedDate // 조회한 Entity의 값을 변경할 때 시간이 자동 저장
    @Column
    private LocalDateTime updateTime;  //수정시간
}
