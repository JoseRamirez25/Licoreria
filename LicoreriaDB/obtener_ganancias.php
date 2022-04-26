<?php
    include 'conexion.php';
    $op = $_POST['op'];
    $limiteIn = $_POST['limiteIn'];
    $limiteS = $_POST['limiteS'];

if ($limiteIn=="" && $limiteS=="") {
  if ($op==1) {
    $sql=$conexion->prepare("SELECT SUM(Valor) FROM ventas WHERE Fecha_venta = (SELECT CURRENT_DATE)");
    $sql->execute();
    $sql->bind_result($ganancia);
    if ($sql->fetch()) {
      $gan = $ganancia;
    }
    if ($sql) {
      echo $gan;
    }else {
      echo "fallo";
    }
    $conexion->close();
  }
  if($op==2){
    $sql=$conexion->prepare("SELECT SUM(Valor) FROM ventas");
    $sql->execute();
    $sql->bind_result($ganancia);
    if ($sql->fetch()) {
      $gan = $ganancia;
    }
    if ($sql) {
      echo $gan;
    }else {
      echo "fallo";
    }
    $conexion->close();
  }
}else {
  $sql=$conexion->prepare("SELECT SUM(Valor) FROM ventas WHERE Fecha_venta BETWEEN '$limiteIn' AND '$limiteS'");
  $sql->execute();
  $sql->bind_result($ganancia);
  if ($sql->fetch()) {
    $gan = $ganancia;
  }
  if ($sql) {
    echo $gan;
  }else {
    echo "fallo";
  }
  $conexion->close();
}



?>
