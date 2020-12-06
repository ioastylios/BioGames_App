<?php
	include("config.php");

	$user_id = $_REQUEST("user_id");
	$vendor_id = $_REQUEST("vendor_id");
	$review = $_REQUEST("review");

	$query = "insert into review_tb (user_id, vendor_id, review, reg_date) values ('$user_id', '$vendor_id', '$review', NOW())";
	
	if (mysql_query($query)) {
		echo json_encode(array("success":"1"));
	}
	else {
		echo json_encode(array("success":"0"));
	}
	
?>    