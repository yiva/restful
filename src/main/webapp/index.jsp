<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>index</title>
<script type="text/javascript" src="<%=basePath %>resources/js/jquery.2.1.1.min.js"></script>
<script type="text/javascript">
	$(function() {
		$.ajax({
			type : "get",
			url : "http://localhost:8080/restful/userinfo/1/lucy",
			success : function(res) {
				var data = JSON.stringify(res);
				$("#res").html(data);
			},
			error : function(res) {
				alert(res.statusText);
			}
		});
	})
</script>
</head>
<body>
	<div id="res"></div>
</body>

</html>