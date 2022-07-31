package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.form.ItemFormDto;
import com.shop.domain.Item;
import com.shop.service.ItemImgService;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){

        model.addAttribute("itemFormDto", new ItemFormDto());

        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, List<MultipartFile> itemImgFileList) throws IOException {

        if(bindingResult.hasErrors()){// 상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환합니다.
            log.info("errors={}", bindingResult);
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() || itemImgFileList.isEmpty()){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        itemService.saveItem(itemFormDto, itemImgFileList); //상품 저장 로직을 호출합니다

        return "redirect:/"; // 상품이 정상적으로 등록되었다면 메인페이지로 이동;
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){


        ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 조회한 상품 데이터를 모델에 담아서 뷰로 전달
        model.addAttribute("itemFormDto", itemFormDto);

        return "item/itemUpdate";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFileList")
                             List<MultipartFile> itemImgFileList, Model model){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList); // 상품 수정 로직을 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }


    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) // value에 상품 관리 화면 진입 시 url에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable= PageRequest.of(page.isPresent() ? page.get(): 0, 10); // 첫 번째 파라미터로는 조회할 페이지 번호, 두 번째 파라미터로는 한 번에 가ㅣ지고 올 데이터 수, 페이지 번호가 없으면 0페이지를 조회
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); // 조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 객체를 반환 받음
        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("maxPage", 5); // 페이지 번호 최대 개수


        return "item/itemMng";

   }

   @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);

        return "item/itemDtl";
   }

   @ResponseBody
   @GetMapping("/shop/images/{filename}")
   public UrlResource downloadImage(@PathVariable("filename") String filename) throws MalformedURLException {

        return new UrlResource("file:" + itemImgService.getFullPath(filename));
   }








}
