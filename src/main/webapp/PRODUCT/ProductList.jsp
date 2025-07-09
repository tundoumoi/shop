<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/HOMES/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Danh sách sản phẩm</title>
  <style>
    .product-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
      padding: 20px;
    }
    .product-item {
      width: calc(25% - 16px);
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      padding: 8px;
      text-align: center;
      border-radius: 6px;
      background-color: #fff;
    }
    .product-item img {
      max-width: 100%;
      height: auto;
      border-radius: 4px;
    }
    .product-item h3 {
      margin: 10px 0 5px;
    }
    .product-item p {
      margin: 0;
    }
    .pagination {
      margin-top: 20px;
      text-align: center;
    }
    .pagination a, .pagination span {
      margin: 0 4px;
      padding: 5px 10px;
      text-decoration: none;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    .pagination .current {
      background-color: #007bff;
      color: white;
      font-weight: bold;
      border-color: #007bff;
    }
    .pagination a:hover {
      background-color: #f1f1f1;
    }
  </style>
</head>
<body style="padding-top: 80px;">
<body>

<div class="product-grid">
  <c:forEach var="p" items="${productList}">
    <div class="product-item">
      <!-- Sửa ở đây: gọi servlet /product -->
      <a href="${pageContext.request.contextPath}/product?id=${p.id}">
        <c:choose>
          <c:when test="${not empty p.images}">
            <c:forEach var="img" items="${p.images}">
              <c:if test="${img.isPrimary}">
                <img src="${img.imageUrl}" alt="${p.name}" />
              </c:if>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <img src="resources/images/default.jpg" alt="${p.name}" />
          </c:otherwise>
        </c:choose>
        <h3>${p.name}</h3>
        <p>${p.price} VND</p>
      </a>
    </div>
  </c:forEach>
</div>

<!-- Phân trang -->
<div class="pagination">
  <c:if test="${currentPage > 1}">
    <a href="products?page=${currentPage - 1}">&laquo; Prev</a>
  </c:if>

  <c:forEach var="i" begin="1" end="${totalPages}">
    <c:choose>
      <c:when test="${i == currentPage}">
        <span class="current">${i}</span>
      </c:when>
      <c:otherwise>
        <a href="products?page=${i}">${i}</a>
      </c:otherwise>
    </c:choose>
  </c:forEach>

  <c:if test="${currentPage < totalPages}">
    <a href="products?page=${currentPage + 1}">Next &raquo;</a>
  </c:if>
</div>

</body>
</html>

<%@ include file="/HOMES/footer.jsp" %>
