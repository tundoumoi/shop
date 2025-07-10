<%-- 
    Document   : thankyou
    Created on : Jul 10, 2025, 1:54:51 PM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Cảm ơn bạn!</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 100px;
        }
        h1 {
            color: green;
        }
        a {
            text-decoration: none;
            color: crimson;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <h1>🎉 Đặt hàng thành công!</h1>
    <p>Chúng tôi đã gửi email xác nhận đơn hàng đến địa chỉ của bạn.</p>
     <a href="${pageContext.request.contextPath}/products?action=list" class="button">🛒 Tiếp tục mua hàng</a>
   

</body>
</html>

