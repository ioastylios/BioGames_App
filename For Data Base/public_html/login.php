<?php
	include("config.php");

	$email = $_REQUEST("email");
	$password = $_REQUEST("password");

	$query = "select * from user_tb where email='$email' and password='$password'";
	$result = mysql_query($query);

	if (@mysql_num_rows($result) == 0) {
		echo json_encode(array("success":"0", "msg":"Your account or password is incorrect. Try again."));
	}
	else {
		$row = @mysql_fetch_object($result);
		$user_id = $row->user_id;

		$query = "update user_tb set access_time=NOW() where user_id='$user_id'";
		mysql_query($query);

		echo json_encode(array("success":"1", "userinfo":$row));
	}
?>    