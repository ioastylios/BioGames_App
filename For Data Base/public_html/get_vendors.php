<?php
	include("config.php");

	$query = "select * from vendor_tb order by vendor_id desc";
	$result = mysql_query($query);

	echo json_encode(array("vendors":$result));
?>    