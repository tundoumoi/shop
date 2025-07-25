<%-- 
    Document   : checkout
    Created on : Jul 10, 2025, 1:38:04 PM
    Author     : HP
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="Model.User, Model.CartItem" %>

<%
    User user = (User) session.getAttribute("user");
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (user == null || cart == null || cart.isEmpty()) {
        response.sendRedirect("cart.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Xác nhận đơn hàng</title>
    <style>
        .checkout-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: center;
        }
        .info, .address-form {
            margin-bottom: 20px;
        }
        .confirm-btn {
            margin-top: 30px;
            text-align: right;
        }
        .confirm-btn button {
            padding: 10px 30px;
            background-color: green;
            color: white;
            border: none;
            font-weight: bold;
            cursor: pointer;
        }
        .confirm-btn button:hover {
            background-color: darkgreen;
        }
        input[type="text"], input[type="email"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
    </style>
</head>
<body>

<div class="checkout-container">
    <h2>Xác nhận thông tin đơn hàng</h2>

    <form action="confirm" method="post">
        <div class="info">
            <p><strong>Khách hàng:</strong> ${user.fullName}</p>

            <label for="email"><strong>Email:</strong></label>
            <input type="email" name="email" value="${user.email}"
                   pattern="^[a-zA-Z0-9._%+-]+@gmail\\.com$"
                   title="Vui lòng nhập địa chỉ Gmail hợp lệ" required />
        </div>

        <div class="address-form">
            <label><strong>Địa chỉ giao hàng:</strong></label><br/>
            <input type="text" name="address" value="${user.address}"
                   title="Bạn có thể thay đổi địa chỉ cho đơn này (không ảnh hưởng hồ sơ)" required />
        </div>

        <table>
            <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Size</th>
                    <th>Đơn giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="total" value="0" />
                <c:forEach var="item" items="${cart}">
                    <tr>
                        <td>${item.product.name}</td>
                        <td>${item.variant.size}</td>
                        <td>${item.product.price} VND</td>
                        <td>${item.quantity}</td>
                        <td>
                            <c:set var="subtotal" value="${item.product.price * item.quantity}" />
                            ${subtotal} VND
                            <c:set var="total" value="${total + subtotal}" />
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="4">Tổng cộng</th>
                    <th>${total} VND</th>
                </tr>
            </tfoot>
        </table>

        <div class="confirm-btn">
            <button type="submit">Tiếp tục chọn phương thức thanh toán</button>
        </div>
    </form>
</div>

</body>
</html>
