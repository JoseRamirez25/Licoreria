<?php
include 'conexion.php';
$nombre = $_POST['nombre'];
$descripcion = $_POST['descripcion'];
$cantidad = $_POST['cantidad'];
$precio = $_POST['precio'];
$size = $_POST['tamaño'];
$gradosAlcohol = $_POST['gradosA'];
$proveedor = $_POST['proveedor'];

$sql = "INSERT INTO productos(Nombre_producto, Descripcion, Cantidad, PrecioU, Tamaño, Grados_alcohol, Estado, Proveedores_idProveedores) VALUES ('$nombre','$descripcion','$cantidad','$precio','$size','$gradosAlcohol','Activo','$proveedor')";
$result = mysqli_query($conexion, $sql);

if ($result) {
  echo "1";
}
else {
  echo "error ".$sql;
}


 ?>
