<?php
    include 'conexion.php';
    $proveedores = array();
    $search = $_POST['search'];

    if ($search!="") {
      $sql=$conexion->prepare("select idProveedores, Nombre_proveedores, Telefono_proveedores from proveedores where (idProveedores = '$search' || Nombre_proveedores = '$search') AND idProveedores != 'asignar' AND idProveedores != 'temp' ORDER BY idProveedores ASC");
      $sql->execute();
      $sql->bind_result($Pid, $Pnombre, $Ptelefono);
    }else {
      $sql=$conexion->prepare("select idProveedores, Nombre_proveedores, Telefono_proveedores from proveedores where idProveedores != 'asignar' AND idProveedores != 'temp' ORDER BY idProveedores ASC");
      $sql->execute();
      $sql->bind_result($Pid, $Pnombre, $Ptelefono);
    }


    if ($sql) {
      while ($sql->fetch()){
        $temp = array();
        $temp['PrID'] = $Pid;
        $temp['PrNombre'] = $Pnombre;
        $temp['PrTelefono'] = $Ptelefono;
        array_push($proveedores, $temp);
      }
      echo json_encode($proveedores);
    }else {
      echo "fallo";
    }


    $sql->close();
    $conexion->close();

?>
