<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.*, Model.User" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    User currentUser = (User) session.getAttribute("user");
    if (!"admin".equals(currentUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>SmartPhone Store - Admin</title>
  <!-- Bootstrap 5 + FontAwesome + Chart.js + jQuery -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" rel="stylesheet"/>
  <script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chart.js@4.2.1/dist/chart.umd.min.js"></script>
  <style>
    body { margin:0; height:100vh; overflow:hidden; }
    .sidebar { width:240px; background:#2f353a; color:#fff; }
    .sidebar .brand { padding:1rem; font-size:1.2rem; border-bottom:1px solid #444; }
    .sidebar .nav-link { color:#adb5bd; }
    .sidebar .nav-link.active,
    .sidebar .nav-link:hover { background:#1e282c; color:#fff; }
    .content { flex-grow:1; background:#2f353a; color:#fff; overflow-y:auto; }
    .content .container-fluid { padding:1.5rem; }
  </style>
</head>
<body>
  <div class="d-flex h-100">
    <!-- SIDEBAR -->
    <nav class="sidebar d-flex flex-column">
      <div class="brand">SmartPhone Store - Admin</div>
      <div class="menu-title px-3 text-uppercase text-secondary small">Menu</div>
      <ul class="nav flex-column mb-auto">
        <li><a href="#" id="revenueLink" class="nav-link active"><i class="fa-solid fa-chart-line me-2"></i> Doanh thu</a></li>
        <li><a href="#" id="productLink" class="nav-link"><i class="fa-solid fa-box-open me-2"></i> Sản phẩm</a></li>
        <li><a href="#" id="userLink" class="nav-link"><i class="fa-solid fa-user me-2"></i> Người dùng</a></li>
        <li><a href="#" id="agentLink" class="nav-link"><i class="fa-solid fa-robot me-2"></i> Chạy Agent</a></li>
      </ul>
      <div class="mt-auto mb-3 px-3">
        <a href="logout" class="nav-link"><i class="fa-solid fa-sign-out-alt me-2"></i> Đăng xuất</a>
      </div>
    </nav>

    <!-- CONTENT AREA -->
    <div class="content">
      <div id="contentArea" class="container-fluid">
        <!-- trống, AJAX sẽ load vào đây -->
      </div>
    </div>
  </div>

  <!-- Bootstrap JS Bundle -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>

  <script>
    // Hàm load fragment vào contentArea
    function loadPage(link, url) {
      $('.nav-link').removeClass('active');
      $(link).addClass('active');
      $('#contentArea').fadeOut(100, function(){
        $(this).load(url, function(){
          $(this).fadeIn(200);
        });
      });
      return false;
    }

    $(function(){
      // Khi click menu
      $('#revenueLink').click(function(){ return loadPage(this, 'revenues'); });
      $('#productLink').click(function(){ return loadPage(this, 'products'); });
      $('#userLink').click(function(){ return loadPage(this, 'users'); });

      // Load mặc định "Doanh thu"
      $('#revenueLink').trigger('click');
    });
  </script>
  
  <script>
    document.getElementById("agentLink").addEventListener("click", function(e) {
      e.preventDefault();
      fetch("${pageContext.request.contextPath}/runrecommendationagent")
        .then(resp => {
          if (!resp.ok) throw new Error("HTTP " + resp.status);
          return resp.text();
        })
        .then(msg => alert("Agent đã chạy thành công!"))
        .catch(err => alert("Lỗi khi chạy Agent: " + err));
    });
  </script>
</body>
</html>
