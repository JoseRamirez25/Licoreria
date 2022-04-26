<?php
    include 'conexion.php';

    $usuario=$_POST['usuario'];
    $password=$_POST['password'];

    $sql=$conexion->prepare("select * from usuario where idUsuario=? and PassUsuario=?");
    $sql->bind_param('ss',$usuario,$password);
    $sql->execute();

    $registros=$sql->get_result();
    if($fila=$registros->fetch_assoc())
    {
        echo json_encode($fila,JSON_UNESCAPED_UNICODE);

    }
    $sql->close();
    $conexion->close();

?>
