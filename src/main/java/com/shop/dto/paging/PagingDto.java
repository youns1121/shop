package com.shop.dto.paging;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class PagingDto {

    //게시물 총 개수
    private long totalCnt;

    //페이지 번호
    private int pageNo;

    //페이지당 노출 갯수
    private int perPageCnt;

    //노출 페이징수
    private int pageRange;

    //페이징 시작
    private int pageStartNo;

    //페이징 종료
    private int pageEndNo;

    //최종 페이지 번호
    private int pageLastNo;


    private List<Integer> pageNumList;

    private int numberOfElements;

    public PagingDto () {
        this.pageNo = 1;
        this.perPageCnt = 20;
        this.pageRange = 10;
    }

    public long getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(long totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPerPageCnt() {
        return perPageCnt;
    }

    public void setPerPageCnt(int perPageCnt) {
        this.perPageCnt = perPageCnt;
    }

    public int getPageRange() {
        return pageRange;
    }

    public void setPageRange(int pageRange) {
        this.pageRange = pageRange;
    }

    public int getPageStartNo() {
        return pageStartNo;
    }

    public void setPageStartNo(int pageStartNo) {
        this.pageStartNo = pageStartNo;
    }

    public int getPageEndNo() {
        return pageEndNo;
    }

    public void setPageEndNo(int pageEndNo) {
        this.pageEndNo = pageEndNo;
    }

    public int getPageLastNo() {
        return pageLastNo;
    }

    public void setPageLastNo(int pageLastNo) {
        this.pageLastNo = pageLastNo;
    }

    public List<Integer> getPageNumList() {
        return pageNumList;
    }

    public void setPageNumList(List<Integer> pageNumList) {
        this.pageNumList = pageNumList;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
