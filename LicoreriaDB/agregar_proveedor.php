<?php
include 'conexion.php';
$idP = $_POST['id'];
$nameP = $_POST['nombre'];
$phoneP = $_POST['telefono'];

$sql = "SELECT idProveedores FROM proveedores WHERE idProveedores = '$idP'";
$result = mysqli_query($conexion, $sql);
if (!$result->num_rows) {
  $sql = "INSERT INTO proveedores(idProveedores, Nombre_proveedores, Telefono_proveedores) VALUES ('$idP','$nameP','$phoneP')";
  $result = mysqli_query($conexion, $sql);

  if ($result) {
    echo "1";
  }
  else {
    echo "error ".$sql;
  }
}
else {
  echo "Este proveedor ya se encuentra registrado";
}


 ?>
