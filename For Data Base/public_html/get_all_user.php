<?php
require"connect1.php";

$sql_query="SELECT id, name FROM users";

if ($result=mysqli_query($con,$sql_query))
{
  $response = "{'users': [";
  $i = 0;
  // Fetch one and one row
  while ($row=mysqli_fetch_row($result))
  {
	if ($i > 0 )
	  $response .= ",";
	$response.= "{'id': '$row[0]', 'name': '$row[1]'}";
	$i++ ;
  }
  $response.= "] }";
  echo $response;
  // Free result set
  mysqli_free_result($result);
}

mysqli_close($con);
?>