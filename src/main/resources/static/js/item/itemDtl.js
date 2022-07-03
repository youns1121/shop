// <script th:inline="javascript" >
//             $(document).ready(function(){
//
//             calculateToalPrice();
//
//             $("#count").change( function(){
//             calculateToalPrice();
//         });
//         });
//
//             function calculateToalPrice(){
//             var count = $("#count").val();
//             var price = $("#price").val();
//             var totalPrice = price*count;
//             $("#totalPrice").html(totalPrice + '원');
//         }
//
//             function order(){
//             var token = $("meta[name='_csrf']").attr("content");  //스프링 시큐리트를 사용할 경우 기본적으로 post 방식의 데이터 전송에는 csrf 토큰 값이 필요
//             var header = $("meta[name='_csrf_header']").attr("content"); // 스프링 시큐리트를 사용할 경우 기본적으로 post 방식의 데이터 전송에는 csrf 토큰 값이 필요
//
//             var url = "/order";
//             var paramData = { // 주문할 상품의 아이디와 주문 수량 데이터를 전달할 객체를 생성합니다
//             itemId : $("#itemId").val(),
//             count : $("#count").val()
//         };
//
//             var param = JSON.stringify(paramData); // 서버에 보낼 주문 데이터를 json으로 지정
//
//             $.ajax({
//             url : url,
//             type : "POST",
//             contentType : "application/json", // 서버에 데이터를 보낼 형식을 json으로 지정
//             data : param,
//             beforeSend : function(xhr){
//             /* 데이터를 전송하기 전에 헤더에 csrf 값을 설정 */
//             xhr.setRequestHeader(header, token);
//         },
//             dataType : "json", // 서버에서 결과값으로 받을 데이터의 타입을 json으로 설정
//             cache : false,
//             success : function(result, status){ // 주문 로직 호출이 성공하면 '주문이 완료 되었습니다. 라는 메시지를 보여주고 메인으로 이동
//             alert("주문이 완료 되었습니다.");
//             location.href='/';
//         },
//             error : function(jqXHR, status, error){
//             if(jqXHR.status == '401'){ // 현재 로그인 상태가 아니라면 메시지를 보여주고 메인으로 이동
//             alert('로그인 후 이용해주세요');
//             location.href='/members/login';
//         } else{
//             alert(jqXHR.responseText); // 주문 시 에러가 발생하면 해당 에러를 보여줌
//         }
//         }
//         });
//         }
//
//             function addCart(){
//             var token = $("meta[name='_csrf']").attr("content");
//             var header = $("meta[name='_csrf_header']").attr("content");
//
//             var url = "/cart";
//             var paramData = {
//             itemId : $("#itemId").val(),
//             count : $("#count").val()
//         };
//
//             var param = JSON.stringify(paramData);
//
//             $.ajax({
//             url      : url,
//             type     : "POST",
//             contentType : "application/json",
//             data     : param,
//             beforeSend : function(xhr){
//             /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
//             xhr.setRequestHeader(header, token);
//         },
//             dataType : "json",
//             cache   : false,
//             success  : function(result, status){
//             alert("상품을 장바구니에 담았습니다.");
//             location.href='/';
//         },
//             error : function(jqXHR, status, error){
//
//             if(jqXHR.status == '401'){
//             alert('로그인 후 이용해주세요');
//             location.href='/members/login';
//         } else{
//             alert(jqXHR.responseText);
//         }
//
//         }
//     });
// }
// </script>