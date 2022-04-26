<?php
    include 'conexion.php';
    $id = $_POST['id'];
    $estado = $_POST['estado'];
    $cantidad = $_POST['cantidad'];

    if ($estado == "Activo") {
        $sql = "UPDATE productos SET Estado = 'Inactivo' WHERE idProductos = '$id'";
        $result = mysqli_query($conexion,$sql);
        if ($result) {
          echo "Desactivado";
        }else {
          echo "error ".$sql;
        }
        $conexion->close();
    }
    if($estado == "Inactivo" && $cantidad == "0"){
        $sql = "UPDATE productos SET Estado = 'Activo', Cantidad = 1 WHERE idProductos = '$id'";
        $result = mysqli_query($conexion,$sql);
        if ($result) {
          echo "Activado";
        }else {
          echo "error ".$sql;
        }
        $conexion->close();
    }
    if($estado == "Inactivo" && $cantidad != "0"){
        $sql = "UPDATE productos SET Estado = 'Activo' WHERE idProductos = '$id'";
        $result = mysqli_query($conexion,$sql);
        if ($result) {
          echo "Activado";
        }else {
          echo "error ".$sql;
        }
        $conexion->close();
    }



?>
