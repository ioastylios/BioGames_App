<?php
require"connect.php";
$id=$_POST["id"];
$type=$_POST["type"];
$value=$_POST["value"];
$duration=$_POST["duration"];

$sql_query="insert into events_meta(events_id,sensor_type,sensor_value, duration) values('$id','$type','$value','$duration');";

if(mysqli_query($con,$sql_query)){
	//echo "  Data insertion successfull";
}else{
	//echo "  Data insertion not successfull";
}

?>