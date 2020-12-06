<?php
require"connect1.php";
$id = $_POST["id"];
//$id = $_GET["id"];

$sql_query="SELECT * FROM users WHERE `id` = '$id';";

if ($result=mysqli_query($con,$sql_query))
  {
  // Fetch one and one row
  while ($row=mysqli_fetch_row($result))
  {
    echo "{'id': '$row[0]', 'name': '$row[1]', 'age': '$row[2]', 'gender': '$row[3]', 'game_state': '$row[4]'}";
  }
  // Free result set
  mysqli_free_result($result);
}

mysqli_close($con);
?>