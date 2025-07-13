<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Model.product"%>
<%@page import="Model.productImage"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/HOMES/header.jsp" %>

<%
    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Kết quả tìm kiếm</title>
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
      padding: 10px;
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
      height: 250px;
      object-fit: cover;
      border-radius: 6px;
      transition: opacity 0.3s ease;
    }
    .hover-img {
      position: absolute;
      top: 10px;
      left: 10px;
      right: 10px;
      opacity: 0;
    }
    .product-item:hover .hover-img {
      opacity: 1;
    }
    .product-item:hover .main-img {
      opacity: 0;
    }
    .product-item h3 {
      font-size: 1.1rem;
      margin-bottom: 5px;
      color: #007bff;
    }
    .product-item p {
      margin: 0;
      color: #333;
      font-weight: bold;
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
  </style>
</head>
<body style="padding-top: 90px;">
  <div class="container">
    <h2>Kết quả tìm kiếm cho: "<%= request.getAttribute("keyword") %>"</h2>

    <div class="product-grid">
    <%
        List<product> results = (List<product>) request.getAttribute("searchResults");
        if (results != null && !results.isEmpty()) {
            for (product p : results) {
                String mainImage = "resources/images/default.jpg";
                String hoverImage = "";
                List<productImage> images = p.getImages();
                if (images != null && !images.isEmpty()) {
                    for (productImage img : images) {
                        if (img.isIsPrimary() && mainImage.equals("resources/images/default.jpg")) {
                            mainImage = img.getImageUrl();
                        }
                        if (!img.isIsPrimary() && hoverImage.equals("")) {
                            hoverImage = img.getImageUrl();
                        }
                    }
                    if (mainImage.equals("resources/images/default.jpg")) {
                        mainImage = images.get(0).getImageUrl();
                    }
                }
    %>
        <div class="product-item">
            <a href="<%= request.getContextPath() %>/products?action=view&id=<%= p.getId() %>">
                <img src="<%= mainImage %>" alt="<%= p.getName() %>" class="main-img" />
                <% if (!hoverImage.equals("")) { %>
                    <img src="<%= hoverImage %>" alt="<%= p.getName() %> phụ" class="hover-img" />
                <% } %>
                <h3><%= p.getName() %></h3>
                <p><%= formatter.format(p.getPrice()) %> đ</p>
            </a>
        </div>
    <%
            }
        } else {
    %>
        <p>Không tìm thấy sản phẩm nào.</p>
    <%
        }
    %>
    </div>

    <!-- ✅ PHÂN TRANG -->
    <%
        Integer currentPage = (Integer) request.getAttribute("currentPage");
        Integer totalPages = (Integer) request.getAttribute("totalPages");
        String keyword = (String) request.getAttribute("keyword");

        if (currentPage != null && totalPages != null && totalPages > 1) {
    %>
    <div class="pagination">
        <%
            if (currentPage > 1) {
        %>
            <a href="search?keyword=<%= keyword %>&page=<%= currentPage - 1 %>">&laquo; Prev</a>
        <%
            }

            for (int i = 1; i <= totalPages; i++) {
                if (i == currentPage) {
        %>
            <span class="current"><%= i %></span>
        <%
                } else {
        %>
            <a href="search?keyword=<%= keyword %>&page=<%= i %>"><%= i %></a>
        <%
                }
            }

            if (currentPage < totalPages) {
        %>
            <a href="search?keyword=<%= keyword %>&page=<%= currentPage + 1 %>">Next &raquo;</a>
        <%
            }
        %>
    </div>
    <%
        }
    %>
  </div>
</body>
</html>

<%@ include file="/HOMES/footer.jsp" %>
