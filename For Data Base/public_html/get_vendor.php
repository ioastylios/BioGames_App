<?php
	include("config.php");

	$vendor_id = $_REQUEST("vendor_id");

	$query = "select * from vendor_tb where vendor_id='$vendor_id'";
	$result = mysql_query($query);
	$row = @mysql_fetch_object($result);

	echo json_encode(array("vendor":$row, "vendor_id":$vendor_id));
?>    