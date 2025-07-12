<%-- 
    Document   : Login
    Created on : May 16, 2025, 8:51:24 AM
    Author     : LENOVO Ideapad 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>MUSTORE</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
    <meta name="google-signin-client_id" content="415815610320-dlhsbb2a2183s8psfbb3rou7pamen143.apps.googleusercontent.com">
    

    <!-- Google Web Fonts -->
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;500&family=Lora:wght@600;700&display=swap" rel="stylesheet"> 

    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="lib/animate/animate.min.css" rel="stylesheet">
    <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

    <!-- Customized Bootstrap Stylesheet -->
    <link href="CSS/login.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    
</head>
<body>
    
    <div class="container" id="container">
        <div class="form-container sign-up-container">
        <form id="registerForm" action="${pageContext.request.contextPath}/register?action=register" method="post">
            <h1>Create Account</h1>

            <div class="social-container">
              <!-- Facebook Login -->
                <a href="javascript:void(0)" class="social" id="fbSignUpBtn">
                  <i class="fab fa-facebook-f"></i>
                </a>
            </div>

            <span>or use your email for registration</span>
            <input type="text"  name="fullName" placeholder="Full Name" required />
            <input type="email" name="email"    placeholder="Email"     required />
            <input type="password" name="password" placeholder="Password" required />
            <input type="text"  name="address" placeholder="Address" />
            <button type="submit">Sign Up</button>
        </form>
      </div>
        <div class="form-container sign-in-container">
            <form id="signInForm" action="${pageContext.request.contextPath}/login?action=login" method="post">
                <h1>Sign in</h1>
                <div class="social-container">
                    <!-- Facebook Login -->
                    <a href="javascript:void(0)" class="social" id="fbLoginBtn">
                      <i class="fab fa-facebook-f"></i>
                    </a>
                </div>
                <span>or use your account</span>
                <input type="email" name="email" placeholder="Email" />
                <input type="password" name="password" placeholder="Password" />
                <button type="submit">Sign In</button>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h2>Already have an account</h2>
                    <button class="ghost" id="signIn">Sign In</button>
                </div>
                <div class="overlay-panel overlay-right">
                    <h2>No account</h2>
                    <button class="ghost" id="signUp">Sign Up</button>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        const signUpButton = document.getElementById('signUp');
        const signInButton = document.getElementById('signIn');
        const container = document.getElementById('container');

        signUpButton.addEventListener('click', () => {
            container.classList.add('right-panel-active');
        });

        signInButton.addEventListener('click', () => {
            container.classList.remove('right-panel-active');
        });
    </script>
    
    <script>
    // Facebook SDK init (ví dụ)
    window.fbAsyncInit = function() {
      FB.init({
        appId      : '1033293561707776',
        cookie     : true,
        xfbml      : false,
        version    : 'v12.0'
      });
    };

    document.getElementById('fbSignUpBtn').addEventListener('click', function() {
    FB.login(function(response) {
      if (response.status === 'connected') {
        // Lấy token đúng
        var accessToken = response.authResponse.accessToken;
        console.log("FB Access Token:", accessToken);  // <-- debug xem token
        // Gửi lên servlet
        var form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/register?action=register';
        form.innerHTML = 
          '<input type="hidden" name="provider" value="facebook">' +
          '<input type="hidden" name="access_token" value="' + accessToken + '">';
        document.body.appendChild(form);
        form.submit();
      } else {
        alert('Facebook login không thành công.');
      }
    }, { scope: 'email' });
      });
      
    document.getElementById('fbLoginBtn').addEventListener('click', function() {
    FB.login(function(response) {
      if (response.status === 'connected') {
        // Lấy token đúng
        var accessToken = response.authResponse.accessToken;
        console.log("FB Access Token:", accessToken);  // <-- debug xem token
        // Gửi lên servlet
        var form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/login?action=facebook';
        form.innerHTML = 
          '<input type="hidden" name="provider" value="facebook">' +
          '<input type="hidden" name="access_token" value="' + accessToken + '">';
        document.body.appendChild(form);
        form.submit();
      } else {
        alert('Facebook login không thành công.');
      }
    }, { scope: 'email' });
      });
    </script>

    <script src="https://connect.facebook.net/en_US/sdk.js"></script>
    <script src="https://apis.google.com/js/platform.js?onload=onGoogleLibraryLoad" async defer></script>
</body>

</html>
