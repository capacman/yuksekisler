<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Yuksekisler</title>
    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/smoothness/jquery-ui-1.8.17.custom.css'/>
    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/bootstrap.min.css'/>

    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/validationEngine.jquery.css'/>
    <link rel="stylesheet" href='<c:out value="${pageContext.request.contextPath}" />/resources/css/style.css'/>
    <script type="text/javascript">
        appconfig={
          context:'<c:out value="${pageContext.request.contextPath}" />'
        };
    </script>
    <script data-main='<c:out value="${pageContext.request.contextPath}" />/resources/js/main' src='<c:out value="${pageContext.request.contextPath}" />/resources/js/libs/require.js'></script>
    <LINK REL="SHORTCUT ICON" HREF="favicon.ico"/>
</head>
<body>
    <div class="navbar navbar-fixed-top">
    <div id='background'>
    <img src='<c:out value="${pageContext.request.contextPath}" />/resources/images/logo.png' class="stretch" alt=""/>
    </div>
        <div class="navbar-inner">

            <div class="container">
                <ul class="nav">
                    <li class="active"><a href="#">Equipments</a></li>
                    <li><a href="#about">Works</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div id="content" class="container">
    </div>

</body>
</html>