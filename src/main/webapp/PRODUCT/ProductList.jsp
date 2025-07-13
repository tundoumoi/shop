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
      text-align: center;
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

    /* Bộ lọc mô tả */
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
  </style>
</head>
<body style="padding-top: 90px;">

<!-- 🔍 Bộ lọc theo description -->
<c:if test="${category == 'Jersey'}">
  <div class="description-filters">
    <a href="products?category=Jersey" class="description-btn ${empty param.description ? 'active' : ''}">ALL</a>
    <a href="products?category=Jersey&description=Home" class="description-btn ${param.description == 'Home' ? 'active' : ''}">HOME</a>
    <a href="products?category=Jersey&description=Away" class="description-btn ${param.description == 'Away' ? 'active' : ''}">AWAY</a>
    <a href="products?category=Jersey&description=Third" class="description-btn ${param.description == 'Third' ? 'active' : ''}">THIRD</a>
    <a href="products?category=Jersey&description=Goalkeeper" class="description-btn ${param.description == 'Goalkeeper' ? 'active' : ''}">GOALKEEPER</a>
  </div>
</c:if>
<c:if test="${category == 'Trainingwear'}">
  <div class="description-filters">
    <a href="products?category=Trainingwear" class="description-btn ${empty param.description ? 'active' : ''}">ALL</a>
    <a href="products?category=Trainingwear&description=Top" class="description-btn ${param.description == 'Top' ? 'active' : ''}">TOP</a>
    <a href="products?category=Trainingwear&description=Jackets and Coats" class="description-btn ${param.description == 'Jackets and Coats' ? 'active' : ''}">JACKETS & COATS</a>
    <a href="products?category=Trainingwear&description=Shorts and Trousers" class="description-btn ${param.description == 'Shorts and Trousers' ? 'active' : ''}">SHORTS & TROUSERS</a>
  </div>
</c:if>

<c:if test="${category == 'Fashion'}">
  <div class="description-filters">
    <a href="products?category=Fashion" class="description-btn ${empty param.description ? 'active' : ''}">ALL</a>
    <a href="products?category=Fashion&description=Tshirt" class="description-btn ${param.description == 'Tshirt' ? 'active' : ''}">Tshirt</a>
    <a href="products?category=Fashion&description=Hoodie" class="description-btn ${param.description == 'Hoodie' ? 'active' : ''}">Hoodie</a>
    <a href="products?category=Fashion&description=Retro" class="description-btn ${param.description == 'Retro' ? 'active' : ''}">Retro</a>
    <a href="products?category=Fashion&description=Nightwear" class="description-btn ${param.description == 'Nightwear' ? 'active' : ''}">Nightwear</a>
    <a href="products?category=Fashion&description=Shorts and pants" class="description-btn ${param.description == 'Shorts and pants' ? 'active' : ''}">Shorts & pants</a>
    <a href="products?category=Fashion&description=Socks and Underwear" class="description-btn ${param.description == 'Socks and Underwear' ? 'active' : ''}">Socks & Underwear</a>
    <a href="products?category=Fashion&description=Shoes" class="description-btn ${param.description == 'Shoes' ? 'active' : ''}">Shoes</a>
  </div>
</c:if>

<c:if test="${category == 'Souvenir'}">
  <div class="description-filters">
    <a href="products?category=Souvenir" class="description-btn ${empty param.description ? 'active' : ''}">ALL</a>
    <a href="products?category=Souvenir&description=Keychain" class="description-btn ${param.description == 'Keychain' ? 'active' : ''}">Keychain</a>
    <a href="products?category=Souvenir&description=Balls" class="description-btn ${param.description == 'Balls' ? 'active' : ''}">Balls</a>
    <a href="products?category=Souvenir&description=Bottles" class="description-btn ${param.description == 'Bottles' ? 'active' : ''}">Bottles</a>
  </div>
</c:if>


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

<!-- Phân trang giữ tham số lọc -->
<c:set var="queryParams" value=""/>
<c:if test="${not empty category}">
  <c:set var="queryParams" value="${queryParams}&category=${category}" />
</c:if>
<c:if test="${not empty description}">
  <c:set var="queryParams" value="${queryParams}&description=${description}" />
</c:if>

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

<jsp:include page="/recommendation.jsp" />
<%@ include file="/HOMES/footer.jsp" %>


