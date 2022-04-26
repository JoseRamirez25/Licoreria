<?php
    $hostname='localhost';
    $database='licoreria_inventario';
    $username='root';
    $password='';

    $conexion=new mysqli($hostname,$username,$password,$database);
    if($conexion->connect_errno){
        echo "Error en la Conexion";
    }

?>
