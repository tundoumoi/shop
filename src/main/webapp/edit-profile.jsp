<%-- 
    Document   : edit-profile.jsp
    Created on : Jul 11, 2025
    Author     : HP
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.User" %>

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
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin cá nhân</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
        }
        .header {
            padding: 10px 20px;
            background: #333;
            color: white;
            display: flex;
            justify-content: space-between;
        }
        .header a {
            color: white;
            text-decoration: none;
            font-weight: bold;
        }
        .container {
            max-width: 500px;
            margin: 50px auto;
        }
        label {
            font-weight: bold;
        }
        .form-group {
            position: relative;
            margin-bottom: 16px;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        .toggle-password {
            position: absolute;
            top: 50%;
            right: 10px;
            transform: translateY(-50%);
            cursor: pointer;
            color: gray;
        }
        button {
            padding: 10px 20px;
            background-color: green;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: darkgreen;
        }
    </style>
</head>
<body>

<!-- ✅ Nút logout -->
<div class="header">
    <div><strong>Xin chào, <%= user.getFullName() %></strong></div>
    <div><a href="logout">Đăng xuất</a></div>
</div>

<div class="container">
    <h2>Chỉnh sửa thông tin cá nhân</h2>
    <form action="update-profile" method="post">

        <label>Email (không thay đổi):</label>
        <div class="form-group">
            <input type="text" name="email" value="<%= user.getEmail() %>" readonly />
        </div>

        <label>Họ tên:</label>
        <div class="form-group">
            <input type="text" name="fullName" value="<%= user.getFullName() %>" required />
        </div>

        <label>Mật khẩu mới:</label>
        <div class="form-group">
            <input type="password" name="password" id="password" placeholder="Nhập mật khẩu mới nếu muốn đổi" />
            <i class="fas fa-eye toggle-password" onclick="togglePassword(this)"></i>
        </div>

        <label>Địa chỉ:</label>
        <div class="form-group">
            <input type="text" name="address" value="<%= user.getAddress() != null ? user.getAddress() : "" %>" />
        </div>

        <div>
            <button type="submit">Cập nhật</button>
            <a href="${pageContext.request.contextPath}/products?action=list"><button type="button">Quay về trang chủ</button></a>
        </div>
    </form>
</div>

<script>
    function togglePassword(icon) {
        const pwdInput = document.getElementById("password");
        const isHidden = pwdInput.type === "password";
        pwdInput.type = isHidden ? "text" : "password";
        icon.classList.toggle("fa-eye");
        icon.classList.toggle("fa-eye-slash");
    }
</script>

</body>
</html>
