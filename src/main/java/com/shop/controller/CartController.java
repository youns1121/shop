package com.shop.controller;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.service.CartService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    @ResponseBody
    public  ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto,
                                              BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) { //장바구니에 담을 상품 정보를 받는 cartItemDto 객체에 데이터 바인딩 시 에러가 있는지 검사

            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrorList) {

                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName(); // 현재 로그인한 회원의 이메일 정보를 변수에 저장합니다.
        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }


    //장바구니 페이지 이동
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){

        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName()); // 현재 로그인한 사용자의 이메일 정보를 이용하여 장바구니 상품 정보를 조회
        model.addAttribute("cartItems", cartDetailDtoList); // 조회한 장바구니 상품 정보를 뷰로 전달

        return "cart/cartList";

    }

    @PatchMapping(value = "/cartItem/{cartItemId}") // 장바구니 상품의 수량만 업데이트
    @ResponseBody
    public ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        if(count <= 0){ // 0개이하로 업데이트 요청시 에러 메시지를 담아 반환
            return new ResponseEntity<>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);

        }else if(!cartService.validateCartItem(cartItemId, principal.getName())){ // 수정 권한 체크

            return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);// 장바구니 상품의 개수를 업데이트
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);

    }

    /**
     * 장바구니 상품 삭제 요청
     * @param cartItemId
     * @param principal
     * @return
     */
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName())){ //수정권한 체크

            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PostMapping(value = "/cart/orders")
    @ResponseBody
    public ResponseEntity<String> orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0) { //주문할 상품을 선택하지 않았는지 체크

            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }

        for(CartOrderDto cartOrder : cartOrderDtoList){
            if(!(cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName()))){ //주문 권한 체크
                    return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName()); // 주문 로직 호출 결과 생성된 주문 번호를 반환

        return new ResponseEntity<String>(orderId.toString(), HttpStatus.OK); // 생성된 주문 번호와 요청이 생공했다는 HTTP 응답 상태 코드를 반환
    }
}
