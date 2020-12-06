<?php
require"connect.php";
$phone=$_POST["phone"];
$imei=$_POST["imei"];

$sql_query="insert into events(phone_name,imei) values('$phone','$imei');";

if(mysqli_query($con,$sql_query)){
echo "event_code:" . $con->insert_id;
	//echo "  Data insertion successfull";
}else{
	//echo "  Data insertion not successfull";
}

?>