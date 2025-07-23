

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.*, Model.productImage, Model.product" %>
<%@ include file="/HOMES/header.jsp" %>

<%
    product p = (product) request.getAttribute("product");
    List<productImage> imgList = (p != null && p.getImages() != null) ? p.getImages() : new ArrayList<>();
    List<String> encodedImageUrls = new ArrayList<>();

    for (productImage img : imgList) {
        String url = img.getImageUrl();
        int lastSlash = url.lastIndexOf('/');
        String prefix = url.substring(0, lastSlash + 1);
        String fileName = url.substring(lastSlash + 1);
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        encodedImageUrls.add(prefix + encodedFileName);
    }

    request.setAttribute("encodedImageUrls", encodedImageUrls);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết sản phẩm</title>
    <style>
        .product-container {
            display: flex;
            gap: 30px;
            margin: 40px;
        }
        .product-images img {
            width: 300px;
            border: 1px solid #ccc;
            margin-bottom: 10px;
        }
        .product-details {
            max-width: 400px;
        }
        .product-details h2 {
            margin-top: 0;
        }
        .product-details form {
            margin-top: 20px;
        }
        select, input[type="number"] {
            margin-bottom: 15px;
            padding: 5px;
            width: 100%;
        }
        .button-group {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        .button-group button,
        .button-group .btn-link {
            flex: 1;
            padding: 10px 20px;
            background-color: crimson;
            color: white;
            border: none;
            text-align: center;
            text-decoration: none;
            font-weight: bold;
            cursor: pointer;
            border-radius: 4px;
        }
        .button-group .btn-link:hover,
        .button-group button:hover {
            background-color: darkred;
        }
    </style>
</head>

<body style="padding-top: 80px;">
<body>

<div class="product-container">
    <!-- Ảnh sản phẩm -->
    <div class="product-images">
        <c:forEach var="url" items="${encodedImageUrls}" varStatus="status">
            <c:if test="${status.index < 2}">
                <img src="${pageContext.request.contextPath}/${url}" alt="Ảnh ${status.index + 1}" />
            </c:if>
        </c:forEach>
    </div>

    <!-- Thông tin sản phẩm -->
    <div class="product-details">
        <h2>${product.name}</h2>
        <p><strong>Giá:</strong> ${product.price} VND</p>
        <p><strong>Mô tả:</strong> ${product.description}</p>

        <!-- Form thêm vào giỏ hàng -->
        <form action="cart" method="post">
            <input type="hidden" name="productId" value="${product.id}" />

            <select id="variantSelect" name="variantId" required>
                <c:forEach var="v" items="${variants}">
                    <option value="${v.id}" data-quantity="${v.quantity}">
                        ${v.size} (Còn ${v.quantity} cái)
                    </option>
                </c:forEach>
            </select>

            <label for="quantity">Số lượng:</label>
            <input type="number" id="quantityInput" name="quantity" value="1" min="1" max="${maxQuantity.getQuantity()}" required />
            
            <!-- Nhóm nút -->
            <div class="button-group">
                <button type="submit">Thêm vào giỏ hàng</button>
                <a href="${pageContext.request.contextPath}/products?action=list" class="btn-link">
                    🛒 Tiếp tục mua hàng
                </a>
            </div>
        </form>
    </div>
</div>

<script>
let startTime = Date.now();

// Gửi thời gian khi người dùng rời trang
window.addEventListener("beforeunload", function () {
    const timeSpent = Math.floor((Date.now() - startTime) / 1000); // thời gian tính bằng giây

    if (timeSpent > 0) {
        navigator.sendBeacon(
            "${pageContext.request.contextPath}/products?action=viewTime&id=${product.id}",
            new Blob([new URLSearchParams({ time: timeSpent })], { type: 'application/x-www-form-urlencoded' })
        );
    }
});
</script>

<!-- Script để cập nhật max ngay khi load và khi đổi variant -->
<script>
  // Lấy DOM
  const variantSelect = document.getElementById('variantSelect');
  const qtyInput      = document.getElementById('quantityInput');

  // Hàm cập nhật thuộc tính max của qtyInput
  function updateMaxQty() {
    // từ <option> đang chọn, đọc data-quantity
    const available = variantSelect
      .selectedOptions[0]
      .dataset
      .quantity;
    // gán max
    qtyInput.max = available;
    // nếu giá trị hiện tại > max thì hạ xuống max
    if (+qtyInput.value > +available) {
      qtyInput.value = available;
    }
  }
  
  // Khi trang load lần đầu, set tức thì
  document.addEventListener('DOMContentLoaded', updateMaxQty);
  // Khi user chọn biến thể khác
  variantSelect.addEventListener('change', updateMaxQty);
</script>

</body>
</html>
