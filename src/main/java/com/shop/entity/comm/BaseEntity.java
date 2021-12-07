package com.shop.entity.comm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class}) //해당 클래스에 Auditing 기능을 포함
public abstract class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    String createdBy;

    @LastModifiedDate
    private String modifiedBy;

    @Column(name="del_yn"/*, nullable = false*/, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String delYn; //삭제여부


}
