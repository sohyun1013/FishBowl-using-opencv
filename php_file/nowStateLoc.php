<?php
	$conn=mysqli_connect("localhost","pi","**","example");
	
	if(mysqli_connect_errno($conn))
	{
		echo "Failed to connect to MySQL : " . mysqli_connect_error();
	}

mysqli_set_charset($conn,"utf8");

$res = mysqli_query($conn, "select * from nowStateLoc");

$result = array();

while($row = mysqli_fetch_array($res))
{
	array_push($result, array('id'=>$row[0], 'loc'=>$row[1]));
}

echo json_encode(array("result"=>$result));

mysqli_close($conn);

?>
