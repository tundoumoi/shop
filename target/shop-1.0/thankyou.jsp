<%-- 
    Document   : thankyou
    Created on : Jul 10, 2025, 1:54:51 PM
    Author     : HP
--%>

<%@page contentType="text/html; charset=UTF-8" %>
<%
    String resultCode = request.getParameter("resultCode");
    boolean success = "0".equals(resultCode);
   // boolean success = true;
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= success ? "Thanh toán thành công" : "Thanh toán thất bại" %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 80px;
        }
        .box {
            padding: 40px;
            margin: 0 auto;
            width: 50%;
            border-radius: 10px;
            background-color: <%= success ? "#e8fff0" : "#ffe8e8" %>;
            border: 2px solid <%= success ? "green" : "red" %>;
        }
        h2 {
            color: <%= success ? "green" : "red" %>;
        }
        a.button {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 24px;
            background-color: crimson;
            color: white;
            border-radius: 6px;
            font-weight: bold;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="box">
    <% if (success) { %>
        <h2>🎉 Đặt hàng thành công!</h2>
        <p>Chúng tôi đã gửi email xác nhận đơn hàng đến địa chỉ của bạn.</p>
    <% } else { %>
        <h2>❌ Thanh toán thất bại!</h2>
        <p>Giao dịch đã bị hủy hoặc từ chối. Vui lòng thử lại.</p>
    <% } %>

    <a href="${pageContext.request.contextPath}/products?action=list" class="button">🛒 Tiếp tục mua hàng</a>
</div>

</body>
</html>
