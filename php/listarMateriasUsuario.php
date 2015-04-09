<?php
	$dni = $_REQUEST['dni'];
	$link = mysql_connect("127.0.0.1","root","1234");
	mysql_set_charset("UTF8", $link);
	mysql_select_db("guiame");
	mysql_query("SET NAMES 'utf8'");
	
	mysql_query("SET CHARACTER SET utf8");
	mysql_query("SET COLLATION_CONNECTION = 'utf8_unicode_ci'");
	$sql = mysql_query("SELECT m.alias, a.numero, c.comision, h.dia, h.horaInicio, h.horaFin, p.nombre  
	FROM materias_registradas mr
	JOIN cursos c 
	ON c.id = mr.id_cursos 
	JOIN cursos_horarios ch
	ON c.id = ch.id_cursos
	JOIN horarios h
	ON h.id = ch.id_horarios
	JOIN materias m
	ON c.id_materias = m.id
	JOIN cursos_profesores cp
	ON c.id = cp.id_cursos
	JOIN profesores p
	ON p.id = cp.id_profesores
	JOIN usuarios u
	ON u.id = mr.id_usuarios
	JOIN aulas a
	ON a.id = c.id_aulas
	WHERE u.dni=$dni");


	while($row=mysql_fetch_assoc($sql))
      $output[]=$row;
      print(json_encode($output));


mysql_close();



  

?>
