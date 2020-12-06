<?php
require"connect1.php";
$stage=$_POST["stage"];
$contents=$_POST["contents"];
$username=$_POST["username"];
$userid=$_POST["userid"];
$score=$_POST["score"];
$sql_query="insert into game_state(userid, username, stage, contents, score) values('$userid','$username','$stage','$contents', '$score');";

if(mysqli_query($con,$sql_query)){
	echo "{response: sucess}";
}else{
	echo "{response: fail}";
}
mysqli_close($con);
?>