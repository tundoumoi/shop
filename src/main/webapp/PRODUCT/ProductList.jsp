<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
      position: relative;
      overflow: hidden;
      transition: 0.3s;
    }
    .product-item:hover {
      transform: translateY(-5px);
      box-shadow: 0 6px 10px rgba(0,0,0,0.15);
    }
    .product-item img {
      width: 100%;
      height: 350px;
      object-fit: cover;
      border-radius: 6px;
      transition: opacity 0.3s ease;
    }
    .hover-img {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      opacity: 0;
    }
    .product-item:hover .hover-img {
      opacity: 1;
    }
    .product-item:hover .main-img {
      opacity: 0;
    }
    .product-item h3 {
      margin: 10px 0 5px;
      font-size: 1.1rem;
      color: #007bff;
    }
    .product-item p {
      margin: 0;
      font-weight: bold;
      color: #333;
    }
    .pagination {
      margin: 30px 0;
      padding-top: 20px;
      text-align: center;
      border-top: 1px solid #ddd;
      background-color: #fff;
    }
    .pagination a, .pagination span {
      margin: 0 4px;
      padding: 6px 12px;
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
    .description-filters {
      display: flex;
      gap: 12px;
      margin: 30px 20px 10px;
      flex-wrap: wrap;
    }
    .description-btn {
      padding: 6px 16px;
      border-radius: 20px;
      border: 1px solid #ccc;
      text-decoration: none;
      color: black;
      font-weight: bold;
      background-color: white;
      transition: 0.3s;
    }
    .description-btn:hover {
      background-color: #f0f0f0;
    }
    .description-btn.active {
      background-color: black;
      color: white;
    }
    .recommend-img {
      height: 250px !important;
      object-fit: cover;
    }
    .recommend-grid {
      display: flex;
      justify-content: center;
      gap: 16px;
      padding: 20px;
      flex-wrap: nowrap;
      overflow-x: auto;
    }
    .recommend-grid .product-item {
      width: 220px;
      flex-shrink: 0;
    }
  </style>
</head>
<body style="padding-top: 90px;">

<%-- Bộ lọc giữ nguyên --%>

<!-- Danh sách sản phẩm -->
<div class="product-grid">
  <c:forEach var="p" items="${productList}">
    <div class="product-item">
      <a href="${pageContext.request.contextPath}/products?action=view&id=${p.id}">
        <c:set var="mainImage" value="" />
        <c:set var="hoverImage" value="" />
        <c:forEach var="img" items="${p.images}">
          <c:if test="${img.isPrimary and empty mainImage}">
            <c:set var="mainImage" value="${img.imageUrl}" />
          </c:if>
          <c:if test="${not img.isPrimary and empty hoverImage}">
            <c:set var="hoverImage" value="${img.imageUrl}" />
          </c:if>
        </c:forEach>
        <img src="${mainImage}" class="main-img" alt="${p.name}" />
        <c:if test="${not empty hoverImage}">
          <img src="${hoverImage}" class="hover-img" alt="${p.name} phụ" />
        </c:if>
        <h3>${p.name}</h3>
        <p><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true" /> đ</p>
      </a>
    </div>
  </c:forEach>
</div>

<!-- Gợi ý dành cho bạn  -->
<c:if test="${not empty recommended}">
  <div style="padding: 40px 60px; background: #f5f5f5; margin-top: 40px;">
    <h2 style="margin-bottom: 20px;">🔍 Gợi ý dành cho bạn</h2>
    <div class="recommend-grid">
      <c:forEach var="p" items="${recommended}">
        <div class="product-item">
          <a href="${pageContext.request.contextPath}/products?action=view&id=${p.id}">
            <c:set var="mainImage" value="" />
            <c:set var="hoverImage" value="" />
            <c:forEach var="img" items="${p.images}">
              <c:if test="${img.isPrimary and empty mainImage}">
                <c:set var="mainImage" value="${img.imageUrl}" />
              </c:if>
              <c:if test="${not img.isPrimary and empty hoverImage}">
                <c:set var="hoverImage" value="${img.imageUrl}" />
              </c:if>
            </c:forEach>
            <img src="${mainImage}" class="recommend-img main-img" alt="${p.name}" />
            <c:if test="${not empty hoverImage}">
              <img src="${hoverImage}" class="recommend-img hover-img" alt="${p.name} phụ" />
            </c:if>
            <h3>${p.name}</h3>
            <p><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true" /> đ</p>
          </a>
        </div>
      </c:forEach>
    </div>
  </div>
</c:if>

<!-- Phân trang giữ tham số lọc -->
<div class="pagination">
  <c:if test="${currentPage > 1}">
    <a href="products?page=${currentPage - 1}${queryParams}">&laquo; Prev</a>
  </c:if>
  <c:forEach var="i" begin="1" end="${totalPages}">
    <c:choose>
      <c:when test="${i == currentPage}">
        <span class="current">${i}</span>
      </c:when>
      <c:otherwise>
        <a href="products?page=${i}${queryParams}">${i}</a>
      </c:otherwise>
    </c:choose>
  </c:forEach>
  <c:if test="${currentPage < totalPages}">
    <a href="products?page=${currentPage + 1}${queryParams}">Next &raquo;</a>
  </c:if>
</div>

</body>
</html>

<%@ include file="/HOMES/footer.jsp" %>