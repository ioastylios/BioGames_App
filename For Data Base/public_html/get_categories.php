<?php
	include("config.php");

	$query = "select * from category_tb where parent_id='0' order by category_id asc";
	
	$result = mysql_query($query);
	echo json_encode(array("categories":"result"));
	
?>    