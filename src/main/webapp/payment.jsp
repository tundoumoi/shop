<%-- 
    Document   : payment
    Created on : Jul 10, 2025, 2:30:10 PM
    Author     : HP
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ page import="Model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.CartItem" %>
<%
    User user = (User) session.getAttribute("user");
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (user == null || cart == null || cart.isEmpty()) {
        response.sendRedirect("cart.jsp");
        return;
    }

    long totalAmount = 0;
    for (CartItem item : cart) {
        totalAmount += item.getQuantity() * item.getProduct().getPrice().longValue();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Phương thức thanh toán</title>
    <style>
        .payment-container {
            max-width: 700px;
            margin: 40px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
        }

        .method {
            margin: 15px 0;
        }

        button {
            background-color: crimson;
            color: white;
            border: none;
            padding: 10px 25px;
            font-weight: bold;
            cursor: pointer;
            border-radius: 5px;
        }

        h2 {
            color: #333;
        }
    </style>
</head>
<body>
<div class="payment-container">
    <h2>Chọn phương thức thanh toán</h2>

    <p><strong>Tổng tiền:</strong> <%= totalAmount %> VND</p>

    <!-- Mặc định action là Momo -->
    <form id="paymentForm" method="post" action="MomoPaymentServlet">
        <input type="hidden" name="amount" value="<%= totalAmount %>" />

        <div class="method">
            <input type="radio" name="paymentMethod" value="momo" id="momo" required>
            <label for="momo">Thanh toán qua ví Momo</label>
        </div>

        <div class="method">
            <input type="radio" name="paymentMethod" value="cod" id="cod">
            <label for="cod">Thanh toán khi nhận hàng (COD)</label>
        </div>

        <button type="submit">Thanh toán</button>
    </form>
</div>

<script>
    const form = document.getElementById("paymentForm");
    form.addEventListener("submit", function (e) {
        const selected = document.querySelector("input[name='paymentMethod']:checked");
        if (!selected) {
            e.preventDefault();
            alert("Vui lòng chọn phương thức thanh toán.");
            return;
        }

        const method = selected.value;
        if (method === "momo") {
            form.action = "MomoPaymentServlet";
        } else {
            form.action = "confirm";
        }
    });
</script>
</body>
</html>
