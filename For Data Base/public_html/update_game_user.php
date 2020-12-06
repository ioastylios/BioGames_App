<?php
require"connect1.php";
$id = $_POST["id"];
$name=$_POST["name"];
$age=$_POST["age"];
$gender=$_POST["gender"];
$game_state = $_POST["game_state"];

if ($id < 0 )
	$sql_query="insert into users(name,age, gender, game_state) values('$name', '$age', '$gender', '$game_state');";
else
	$sql_query="UPDATE users SET `name` = '$name', `age` = '$age', `gender` = '$gender', `game_state` = '$game_state' WHERE `id` = '$id';";

if(mysqli_query($con,$sql_query)){
	if ($id < 0 )
	{
		$id = mysqli_insert_id($con);
	}
	echo "{response: sucess, id: " . $id . "}";
}else{
	echo "{response: fail}";
}
mysqli_close($con);
?>