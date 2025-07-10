<%-- 
    Document   : address
    Created on : Jul 10, 2025, 1:34:54 PM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.User" %>
<%@ include file="HOMES/header.jsp" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Nhập địa chỉ giao hàng</title>
    <style>
        form {
            max-width: 500px;
            margin: 40px auto;
        }
        textarea, button {
            width: 100%;
            padding: 10px;
            font-size: 1rem;
        }
        textarea {
            height: 120px;
            resize: vertical;
        }
    </style>
</head>
<body>

<h2 style="text-align:center;">Nhập địa chỉ giao hàng</h2>

<form action="save-address" method="post">
    <label for="address">Địa chỉ:</label><br/>
    <textarea name="address" required><%= user.getAddress() != null ? user.getAddress() : "" %></textarea><br/>
    <button type="submit">Lưu và tiếp tục thanh toán</button>
</form>

</body>
</html>
