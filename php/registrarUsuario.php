<?php
  $nombre= $_REQUEST["nombre"];
  $mail= $_REQUEST["mail"];
  $dni = $_REQUEST["dni"];
  $pass = $_REQUEST["contrasena"];

 if ($nombre != "" && $mail != "" && $dni != "" && $pass != "")
{
  $con = mysql_connect("127.0.0.1","root","1234");
  mysql_select_db("guiame",$con);

	

  $sql = "insert into usuarios (nombre, mail, dni, contrasena) values('$nombre', '$mail','$dni','$pass')";
  $result = mysql_query($sql,$con);
  mysql_close();
 }
  
?>