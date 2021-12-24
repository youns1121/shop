package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHisDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order (@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {


        if (bindingResult.hasErrors()) { // 주문 정보를 받는 OrderDto 객체에 데이터 바인딩 시 에러가 있는지 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrorList) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST); // 에러 정보를 ResponseEneity 객체에 담아서 반환
        }


        String email = principal.getName(); // 현재 로그인 유저의 정보를 얻기 위해 Principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email); // 화면으로부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK); // 결과값으로 생성된 주문 번호와 요청이 성공했다는 HTTP응 답 상 태 코드를 반환
        }

        //구매이력 조회
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> pagem, Principal principal, Model model){

        Pageable pageable = PageRequest.of(pagem.isPresent() ? pagem.get() : 0, 4); //한 번에 가지고 올 주문의 개수는 4개로 설정

        Page<OrderHisDto> orderHisDtoList = orderService.getOrderList(principal.getName(), pageable); // 현재 로그인한 회원은 이메일과 페이징 객체를 파라미터로 전달하여 화면에 전달한 주문 목록 데이터를 리턴 값으로 받음

        model.addAttribute("orders", orderHisDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPAge", 5);

        return "order/orderHist";

    }


}
