<?php
	$conn=mysqli_connect("localhost","pi","**","example");

	if(mysqli_connect_errno($conn))
	{
		echo "Failed to connect to MySQL : ". mysqli_set_charset($conn,"utf8");
	}

	mysqli_set_charset($conn,"utf8");

	$name = $_POST['name'];
	$x1 = $_POST['x1'];
	$y1 = $_POST['y1'];
	$x2 = $_POST['x2'];
	$y2 = $_POST['y2'];
	$end = $_POST['end'];

	mysqli_query($conn, "INSERT INTO fishXY VALUES ('$name','$x1','$y1','$x2','$y2','$end')");

	mysqli_close($conn);
?>

<html>
	<body>
		<form action="<?php $_PHP_SELF ?>" method="POST">
		Name : <input type="text" name="name"/>
		<input type="submit" name="submit"/>
		</form>
	</body>
</html>
