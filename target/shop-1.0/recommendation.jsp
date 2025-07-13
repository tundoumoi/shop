<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="Model.product" %>
<%@ page import="java.util.List" %>

<%
    List<product> recommended = (List<product>) request.getAttribute("recommended");
    request.setAttribute("recommendedList", recommended);  // Gán lại để dùng JSTL
%>

<c:if test="${not empty recommendedList}">
  <div style="margin: 20px; padding: 20px; background: #f9f9f9; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); position: relative; z-index: 999;">
    <h3>🔍 Gợi ý dành cho bạn</h3>
    <div class="product-grid">
      <c:forEach var="p" items="${recommendedList}">
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
  </div>
</c:if>
