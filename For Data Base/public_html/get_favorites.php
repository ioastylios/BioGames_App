<?php
	include("config.php");

	$vendor_id = $_REQUEST("vendor_id");

	$query = "select * from favorite_tb where vendor_id='$vendor_id' order by favorite_id desc";	
	$result = mysql_query($query);

	echo json_encode(array("favories":"result"));
	
?>    