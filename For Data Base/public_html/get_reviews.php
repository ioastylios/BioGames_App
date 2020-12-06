<?php
	include("config.php");

	$vendor_id = $_REQUEST("vendor_id");

	$query = "select * from review_tb where vendor_id='$vendor_id' order by review_id desc";	
	$result = mysql_query($query);

	echo json_encode(array("reviews":$result));
	
?>    