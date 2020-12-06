<?php
require"connect.php";
$id=$_POST["id"];
$sql_query="update events set end_time=NOW() where id='$id';";

if(mysqli_query($con,$sql_query)){
	//echo "  Data insertion successfull";
}else{
	//echo "  Data insertion not successfull";
}

?>