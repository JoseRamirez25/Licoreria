<?php
    include 'conexion.php';
    $idV = $_POST['id'];

    $sql1 = $conexion->prepare("SELECT Cantidad_vendida, Productos_idProductos FROM ventas WHERE idVentas = '$idV'");
    $sql1->execute();
    $sql1->bind_result($c, $p);
    while ($sql1->fetch()) {
      $cant = $c;
      $idP = $p;
    }
    $sql = "UPDATE productos SET Cantidad = Cantidad+'$cant' WHERE idProductos = '$idP'";
    $result = mysqli_query($conexion, $sql);
    if ($result) {
      $sql = "DELETE FROM ventas WHERE idVentas = '$idV'";
      $result = mysqli_query($conexion, $sql);
      if ($result) {
        echo "1";
      }else {
        echo "error".$sql;
      }
    }

    $conexion->close();

?>
