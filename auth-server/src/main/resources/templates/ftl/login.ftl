<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>微服务统一认证</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/signin.css" rel="stylesheet">
</head>

<body class="sign_body">
<div class="container form-margin-top">
    <form class="form-signin" action="/token/form" method="post">
        <h2 class="form-signin-heading" align="center">统一认证系统</h2>
        <input type="hidden" name="client_id" class="form-control" value="test" placeholder="所属客户端">
        <input type="hidden" name="grant_type" class="form-control" value="password" placeholder="所属客户端">
        <input type="hidden" id="redirect" name="redirect">
        <input type="text" name="username" class="form-control form-margin-top" placeholder="账号" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit" onclick="sign()">sign in</button>
        <#if error??>
            <span style="color: red; ">${error}</span>
        </#if>
    </form>
</div>
</body>

<script>
    function sign() {
        getRedirectUrl();
    }

    function getRedirectUrl() {
        let queryString = getQueryString("redirect");
        document.getElementById("redirect").value = (queryString !== null && queryString !== '' && isUrl(queryString)) ? queryString : "";
    }

    function getQueryString(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }
    function isUrl(str) {
        var v = new RegExp('^(?!mailto:)(?:(?:http|https|ftp)://|//)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$', 'i');
        return v.test(str);
    }
</script>

</html>
