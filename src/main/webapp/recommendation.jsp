<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="Model.product"%>
<%@page import="java.util.List"%>

<%
    List<product> recommended = (List<product>) request.getAttribute("recommended");
    if (recommended != null) {
        request.setAttribute("recommendedList", recommended);
    }
%>

<c:if test="${not empty recommendedList}">
  <div style="margin: 20px; padding: 20px; background: #f9f9f9; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
    <h3 style="margin-bottom: 15px;">🔍 Gợi ý dành cho bạn</h3>
    <div class="product-grid" style="display: flex; flex-wrap: wrap; gap: 20px;">
      <c:forEach var="p" items="${recommendedList}">
        <div class="product-item" style="width: 200px; border: 1px solid #eee; border-radius: 8px; overflow: hidden; background: white; text-align: center; padding: 10px; position: relative;">
          <a href="${pageContext.request.contextPath}/products?action=view&id=${p.id}" style="text-decoration: none; color: inherit;">
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

            <div style="position: relative;">
              <img src="${mainImage}" alt="${p.name}" class="main-img" style="width: 100%; height: auto;" />
              <c:if test="${not empty hoverImage}">
                <img src="${hoverImage}" alt="${p.name} phụ" class="hover-img" style="position: absolute; top: 0; left: 0; width: 100%; height: auto; opacity: 0; transition: opacity 0.3s;" />
              </c:if>
            </div>

            <h4 style="margin-top: 10px; font-size: 16px;">${p.name}</h4>
            <p style="color: #c00; font-weight: bold;"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> đ</p>
          </a>
        </div>
      </c:forEach>
    </div>
  </div>
</c:if>

<!-- Optional: Hover effect -->
<style>
  .product-item:hover .hover-img {
    opacity: 1;
  }
  .product-item:hover .main-img {
    opacity: 0;
  }
</style>
