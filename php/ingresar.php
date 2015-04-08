<?php
$link = mysql_connect("127.0.0.1","root","1234");
mysql_select_db("guiame");
$dni = @$_POST['dni'];
$contrasena = @$_POST['contrasena'];

if (!empty($_POST)) 
{
    if(empty($_POST['dni']) || empty($_POST['contrasena']))
    {
	$response["success"] = 0;
	$response["message"] = "Hay algún campo vacío";
	die(json_encode($response));
    }
    	$q=mysql_query("SELECT * FROM usuarios WHERE dni='$dni' AND contrasena='$contrasena'");
    	$sqlQuery = mysql_query($query);
	$row = mysql_fetch_array($sqlQuery);
	if(!empty($row))
	{
		$response["success"] = 1; 
		$response["message"] = "Logeo con exito"; 
		die(json_encode($response));
	}
  	else
	{
		$response["success"] = 0; 
		$response["message"] = "Usuario o contrasena invalidos"; 
		die(json_encode($response)); 
	}
 }

else
{ 
	$response["success"] = 0; 
	$response["message"] = "Hay campos vacios";
 	die(json_encode($response)); 
} 
mysql_close(); 
?>