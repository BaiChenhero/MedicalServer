<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>upload pic</title>
</head>
<body>
	<form id="form1" method="post"
		action="uploadUserInfo/savePic"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td width="25%" align="right">上传文件：</td>
				<td><input id="files" type="file" NAME="files"
					style="width: 300px;"></td>
				<td><input type="submit" value="提交" /></td>
			</tr>

		</table>
	</form>
</body>
</html>