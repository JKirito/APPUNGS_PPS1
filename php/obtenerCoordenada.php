<?php

$aula = $_REQUEST['aula'];

if($aula == ''){
  return;
}

$link = mysql_connect("127.0.0.1","root","1234");
if (!$link) 
{
    die('No pudo conectar: ' . mysql_error());
}


mysql_select_db("guiame");

$q=mysql_query("SELECT ubicacion FROM aulas WHERE numero='$aula'");
$output = array();
while($e=mysql_fetch_assoc($q))
      $output[]=$e;
      print(json_encode($output));
mysql_close();
?>
