<?php
require"connect.php";
$id=$_POST["id"];
$type=$_POST["type"];
$value=$_POST["value"];

$sql_query="insert into events_meta(events_id,sensor_type,sensor_value) values('$id','$type','$value');";

if(mysqli_query($con,$sql_query)){
	//echo "  Data insertion successfull";
}else{
	//echo "  Data insertion not successfull";
}

?>