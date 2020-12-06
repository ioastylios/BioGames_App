<?php
	include("config.php");

	$user_id = $_REQUEST("user_id");
	$vendor_id = $_REQUEST("vendor_id");
	$message = $_REQUEST("message");

	$query = "insert into suggestion_tb (user_id, vendor_id, message, reg_date) values ('$user_id', '$vendor_id', '$message', NOW())";
	
	if (mysql_query($query)) {
		echo json_encode(array("success":"1"));
	}
	else {
		echo json_encode(array("success":"0"));
	}
	
?>    