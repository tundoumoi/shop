<%-- 
    Document   : heaer
    Created on : Jun 5, 2025, 10:28:56 PM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

  <style>
    .nav-link {
      font-family: 'Playfair Display', serif;
      font-size: 1rem;
      margin-right: 20px;
    }

    .navbar-brand {
      font-family: 'Playfair Display', serif;
      font-weight: bold;
      font-size: 1.4rem;
    }

    .navbar-brand img {
      height: 55px;
      margin: 0 8px;
    }

    .nav-icon {
      font-size: 1.8rem;
      margin-left: 22px;
      cursor: pointer;
      color: black;
    }

    .nav-icon:hover {
      opacity: 0.7;
    }

    .center-logo {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
    }
  </style>
</head>
<body>

<!-- HEADER / NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom px-3 py-2 position-relative">
  
  <!-- Toggle button (left) -->
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navLinks">
    <span class="navbar-toggler-icon"></span>
  </button>

  <!-- Logo giữa (mobile) -->
  <a class="navbar-brand d-lg-none center-logo" href="#">
    <img src="images/others/logomu.png" >
  </a>

  <!-- Icons bên phải (mobile) -->
  <div class="d-flex d-lg-none ml-auto">
    <span class="nav-icon"><i class="fas fa-search"></i></span>
    <span class="nav-icon"><i class="fas fa-user"></i></span>
    <span class="nav-icon"><i class="fas fa-heart"></i></span>
    <span class="nav-icon"><i class="fas fa-bag-shopping"></i></span>
  </div>

  <!-- Full navbar desktop -->
  <div class="collapse navbar-collapse justify-content-between" id="navLinks">
    
    <!-- Logo bên trái (desktop) -->
    <a class="navbar-brand d-none d-lg-flex align-items-center" href="#">
      <span>UNITED</span>
      <img src="images/others/logomu.png" >
      <span>STORE</span>
    </a>

    <!-- Menu chính -->
    <ul class="navbar-nav">
      <li class="nav-item"><a class="nav-link" href="#">Jerseys</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Trainingwear</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Fashion</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Gifts</a></li>
    </ul>

    <!-- Icons desktop -->
    <div class="d-none d-lg-flex align-items-center">
      <span class="nav-icon"><i class="fas fa-search"></i></span>
      <span class="nav-icon"><i class="fas fa-user"></i></span>
      <span class="nav-icon"><i class="fas fa-heart"></i></span>
      <span class="nav-icon"><i class="fas fa-bag-shopping"></i></span>
    </div>
  </div>
</nav>

</body>
</html>

