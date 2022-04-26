<?php
    include 'conexion.php';
    $inventario = array();
    $search = $_POST['search'];

    if ($search!="") {
      $sql=$conexion->prepare("select PD.idProductos, PD.Nombre_producto, PD.Descripcion, PD.Cantidad, PD.PrecioU, PD.Tamaño, PD.Grados_alcohol ,PD.Estado, PD.Proveedores_idProveedores, P.Nombre_proveedores from productos PD, proveedores P where PD.Proveedores_idProveedores = P.idProveedores AND PD.Nombre_producto LIKE '$search' ORDER BY Estado ASC");
      $sql->execute();
      $sql->bind_result($id, $producto, $descripcion, $cantidad, $precio, $tamaño, $gradosAlcohol, $estado, $proveedorId, $proveedorName);
    }else if($search==""){
      $sql=$conexion->prepare("select PD.idProductos, PD.Nombre_producto, PD.Descripcion, PD.Cantidad, PD.PrecioU, PD.Tamaño, PD.Grados_alcohol ,PD.Estado, PD.Proveedores_idProveedores, P.Nombre_proveedores from productos PD, proveedores P where PD.Proveedores_idProveedores = P.idProveedores ORDER BY Estado ASC");
      $sql->execute();
      $sql->bind_result($id, $producto, $descripcion, $cantidad, $precio, $tamaño, $gradosAlcohol, $estado, $proveedorId, $proveedorName);
    }

    if ($sql) {
      while ($sql->fetch()){
        $temp = array();
        $temp['id'] = $id;
        $temp['producto'] = $producto;
        $temp['descripcion'] = $descripcion;
        $temp['cantidad'] = $cantidad;
        $temp['precio'] = $precio;
        $temp['size'] = $tamaño;
        $temp['gradosAlcohol'] = $gradosAlcohol;
        $temp['estado'] = $estado;
        $temp['proveedor'] = $proveedorId;
        $temp['proveedorName'] = $proveedorName;
        array_push($inventario, $temp);
      }
      echo json_encode($inventario);
    }else {
      echo "fallo";
    }


    $sql->close();
    $conexion->close();

?>
