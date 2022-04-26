<?php

include 'conexion.php';
$id = $_POST['id'];
$cantidadVendida = $_POST['cantidad'];
$precio = $_POST['precio'];
$total = intval($cantidadVendida)*intval($precio);

$sql = "UPDATE productos SET Cantidad=Cantidad-'$cantidadVendida' WHERE idProductos='$id'";
$result = mysqli_query($conexion, $sql);
if ($result) {
  $sql1=$conexion->prepare("SELECT Cantidad FROM productos WHERE idProductos = '$id' limit 1");
  $sql1->execute();
  $sql1->bind_result($cant);
  while ($sql1->fetch()) {
    $c = $cant;
  }
  if ($c==0){
    $sql = "UPDATE productos SET Estado = 'Inactivo' WHERE idProductos = '$id'";
    $result = mysqli_query($conexion, $sql);
  }
  $sql = "INSERT INTO ventas(Fecha_venta, Cantidad_vendida, Productos_idProductos, Valor) VALUES (now(),'$cantidadVendida','$id','$total')";
  $result = mysqli_query($conexion, $sql);
  if ($result) {
    echo "1";
  }else {
    echo "error".$sql;
  }
}else {
  echo "error".$sql;
}
$conexion->close();

 ?>
