
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Yuksekisler</title>
    <LINK REL="SHORTCUT ICON" HREF='<c:out value="${pageContext.request.contextPath}" />/favicon.ico'>

    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/bootstrap.min.css'/>
    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/smoothness/jquery-ui-1.8.17.custom.css'/>
    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/validationEngine.jquery.css'/>
    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/style.css'/>
    <script src='<c:out value="${pageContext.request.contextPath}" />/resources/js/libs/jquery-1.7.1.min.js'></script>
    <script src='<c:out value="${pageContext.request.contextPath}" />/resources/js/libs/jquery-ui-1.8.17.custom.min.js'></script>
    <script src='<c:out value="${pageContext.request.contextPath}" />/resources/js/libs/bootstrap.min.js'></script>
    <script src='<c:out value="${pageContext.request.contextPath}" />/resources/js/libs/languages/jquery.validationEngine-en.js' type="text/javascript"
            charset="utf-8"></script>
    <script src='<c:out value="${pageContext.request.contextPath}" />/resources/js/libs/jquery.validationEngine.js' type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" charset="utf-8">
        $(document).ready(function () {
            $("#loginForm").validationEngine({promptPosition : "topLeft", scroll: false});
            $('#submitButton').button();
            $('#submitButton').click(function(){
                $('#loginForm').submit();
            });
        });
    </script>
</head>
<body>


    <div id='background'>
    <img src='<c:out value="${pageContext.request.contextPath}" />/resources/images/logo.png' class="stretch" alt=""/>
    </div>
    <div class="container">
    <div class='row'>
    <div class="span4 offset4">
    <form id='loginForm' action='<c:out value="${pageContext.request.contextPath}" />/j_spring_security_check' method="post" class='login-form'>
        <div width="100%">

            <p id='loginFailed' style='display:none;opacity: 0;color: red'>Either email or password is wrong</p>
            <label for="j_username" class='loginLabel'>Username</label>

            <input type="text" name="j_username" autocomplete="on" id="j_username"
                   class="validate[required,custom[email]]"/>
            <br/>
            <label for="j_password" class='loginLabel'>Password</label>

            <input type="password" name="j_password" autocomplete="on" id="j_password" class="validate[required]"/>
            <br/>
            <input type='checkbox' name='_spring_security_remember_me'/> Remember me on this computer.
            <br/>

            <input type='button' name="Submit" id='submitButton' label="Submit" title='Submit' value="Submit">


        </div>
    </form>
    </div>
    </div>
    </div>

</body>
</html>