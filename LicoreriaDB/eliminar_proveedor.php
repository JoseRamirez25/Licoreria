<?php
    include 'conexion.php';
    $idP = $_POST['id'];

    $sql = "UPDATE productos SET Proveedores_idProveedores= 'asignar' WHERE Proveedores_idProveedores = '$idP'";
    $result = mysqli_query($conexion,$sql);

    if ($result) {
      $sql = "delete from proveedores where idProveedores= '$idP'";
      $result = mysqli_query($conexion, $sql);
      if ($result) {
        echo "1";
      }else {
        echo "0";
      }
    }
    else {
      echo "Error consulta ".$sql;
    }
    mysqli_close($conexion);

?>
