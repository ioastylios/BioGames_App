<?php
$db_name="Your db name";
$mysql_user="Your user name";
$mysql_pass="Your password";
$server_name="localhost";

$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

if(!$con){
	//echo "Connection error";
}else{
	//echo "Connection stablish";
}
?>