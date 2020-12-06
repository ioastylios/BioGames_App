<?php
require"connect.php";

$sql_query="select id from events order by id desc;";

$result=mysqli_query($con,$sql_query);

if(mysqli_num_rows($result)>0)
{
	$row=mysqli_fetch_assoc($result);
	$id=$row["id"];
	echo $id;
}

?>