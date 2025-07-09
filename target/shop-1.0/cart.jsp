<%-- 
    Document   : Cart
    Created on : Jul 9, 2025, 8:35:21 PM
    Author     : HP
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List, Model.CartItem" %>

<%
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    if (cart == null) {
        cart = new java.util.ArrayList<>();
    }
    request.setAttribute("cart", cart);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: center; }
        .actions { margin-top: 20px; text-align: right; }
        button, a.button {
            padding: 10px 20px;
            background-color: crimson;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 10px;
        }
        button:hover, a.button:hover {
            background-color: darkred;
        }
    </style>
</head>
<body>

<h2>Giỏ hàng của bạn</h2>

<c:choose>
    <c:when test="${empty cart}">
        <p>Giỏ hàng đang trống.</p>
        <a href="${pageContext.request.contextPath}/products?action=list" class="button">Tiếp tục mua hàng</a>
    </c:when>

    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Size</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                    <th>Hành động</th>
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
                        <td>
                            <form action="cart" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="remove" />
                                <input type="hidden" name="variantId" value="${item.variant.id}" />
                                <button type="submit">Xoá</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="4">Tổng cộng:</th>
                    <th colspan="2">${total} VND</th>
                </tr>
            </tfoot>
        </table>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/products?action=list" class="button">🛒 Tiếp tục mua hàng</a>
            <a href="checkout.jsp" class="button">💳 Thanh toán</a>
        </div>
    </c:otherwise>
</c:choose>

</body>
</html>
