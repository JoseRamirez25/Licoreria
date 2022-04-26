<?php
    include 'conexion.php';
    $idP = $_POST['id'];
    $nameP = $_POST['nombre'];
    $phoneP = $_POST['telefono'];

      $sql = "UPDATE proveedores SET Nombre_proveedores = '$nameP', Telefono_proveedores = '$phoneP' WHERE idProveedores = '$idP'";
      $result = mysqli_query($conexion,$sql);
      if ($result) {
        echo "1";
      }
      else {
        echo "error ".$sql;
      }



    mysqli_close($conexion);

?>
