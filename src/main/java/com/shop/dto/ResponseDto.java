package com.shop.dto;

import com.shop.dto.paging.PagingDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto<T> extends PagingDto {

    private List<T> resultList;


    // Page<DTO>인 경우 사용
    public ResponseDto (Page<T> dataList){
        if(dataList != null) {

            setResponsePaginationInfo(dataList);

            this.setResultList(dataList.stream().collect(Collectors.toList()));
        }
    }

    private void setResponsePaginationInfo(Page<T> dataList){
        setPageNo(dataList.getNumber() + 1);
        setPerPageCnt(dataList.getPageable().getPageSize());
        setTotalCnt(dataList.getTotalElements());
        setNumberOfElements(dataList.getNumberOfElements());

        setPageStartNo(((getPageNo() - 1) / getPageRange() ) * getPageRange() + 1);
        setPageEndNo(dataList.getTotalPages() == 0 ? 1 :
                Math.min((getPageStartNo() + (getPageRange() - 1)), dataList.getTotalPages()));

        setPageNumList(IntStream.rangeClosed(getPageStartNo(), getPageEndNo()).boxed().collect(Collectors.toList()));
        setPageLastNo(dataList.getTotalPages());
    }
}
