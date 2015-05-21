<?php
$servername = "myservernamegoeshere";
$username = "wrobel731";
$password = "mypassgoeshere";
$dbname = "wrobel731";

$conn = new mysqli($servername, $username, $password, $dbname);
if(! $conn )
{
  die('Could not connect: ' . mysql_error());
}


$got_iddev = $_GET["iddev"];
$got_lon = $_GET["lon"];
$got_lat = $_GET["lat"];
$got_timestmp = $_GET["timestmp"];

//echo $got_iddev;
//echo $got_lon;
//echo $got_lat;
//echo $got_timestmp;
$sql1 = "INSERT INTO pos (iddev,lon,lat,timestmp)
VALUES ('$got_iddev','$got_lon','$got_lat','$got_timestmp');";

if ($conn->query($sql1) === TRUE) {
    echo "Record updated successfully";
} else {
    echo "Error updating record: " . $conn->error;
}

$conn->close();
?>
