<?php
    include 'conexion.php';
    $inventario = array();
    $search = $_POST['search'];

    if ($search!="") {
      $sql=$conexion->prepare("select PD.idProductos, PD.Nombre_producto, PD.Descripcion, PD.Cantidad, PD.PrecioU, PD.Tamaño, PD.Grados_alcohol ,PD.Estado, PD.Proveedores_idProveedores, P.Nombre_proveedores from productos PD, proveedores P where PD.Proveedores_idProveedores = P.idProveedores AND PD.Estado = 'Activo' AND PD.Nombre_producto LIKE '$search' ORDER BY PD.Nombre_producto ASC");
      $sql->execute();
      $sql->bind_result($id, $producto, $descripcion, $cantidad, $precio, $tamaño, $gradosAlcohol, $estado, $proveedor, $proveedorName);
    }else if($search==""){
      $sql=$conexion->prepare("select PD.idProductos, PD.Nombre_producto, PD.Descripcion, PD.Cantidad, PD.PrecioU, PD.Tamaño, PD.Grados_alcohol ,PD.Estado, PD.Proveedores_idProveedores, P.Nombre_proveedores from productos PD, proveedores P where PD.Proveedores_idProveedores = P.idProveedores AND PD.Estado = 'Activo' ORDER BY PD.Nombre_producto ASC");
      $sql->execute();
      $sql->bind_result($id, $producto, $descripcion, $cantidad, $precio, $tamaño, $gradosAlcohol, $estado, $proveedor, $proveedorName);
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
        $temp['proveedor'] = $proveedor;
        $temp['proveedorName'] = $proveedorName;
        array_push($inventario, $temp);
      }
      echo json_encode($inventario);
    }else {
      echo "fallo".$sql;
    }


    $sql->close();
    $conexion->close();

?>
