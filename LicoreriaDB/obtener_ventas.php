<?php
    include 'conexion.php';
    $ventas = array();
    $op = $_POST['op'];
    $limiteIn = $_POST['limiteIn'];
    $limiteS = $_POST['limiteS'];

    if ($limiteS=="" && $limiteIn==""){
      if ($op==1) {
        $sql=$conexion->prepare("select V.idVentas, V.Fecha_venta, V.Cantidad_vendida, P.Nombre_producto, V.Valor from ventas V, productos P where V.Fecha_venta = (SELECT CURRENT_DATE) AND V.Productos_idProductos = P.idProductos ORDER BY V.Fecha_venta DESC");
        $sql->execute();
        $sql->bind_result($id_venta, $fecha_venta, $cantidad_vendida, $productoVendido, $total);
        if ($sql) {
          while ($sql->fetch()){
            $temp = array();
            $temp['idV'] = $id_venta;
            $temp['fechaV'] = $fecha_venta;
            $temp['CantidadV'] = $cantidad_vendida;
            $temp['ProductoV'] = $productoVendido;
            $temp['Total'] = $total;
            array_push($ventas, $temp);
          }
          echo json_encode($ventas);
        }else {
          echo "fallo";
        }

        $sql->close();
      }
      if($op==2){
        $sql=$conexion->prepare("select V.idVentas, V.Fecha_venta, V.Cantidad_vendida, P.Nombre_producto, V.Valor from ventas V, productos P where V.Productos_idProductos = P.idProductos ORDER BY V.Fecha_venta DESC");
        $sql->execute();
        $sql->bind_result($id_venta, $fecha_venta, $cantidad_vendida, $productoVendido, $total);
        if ($sql) {
          while ($sql->fetch()){
            $temp = array();
            $temp['idV'] = $id_venta;
            $temp['fechaV'] = $fecha_venta;
            $temp['CantidadV'] = $cantidad_vendida;
            $temp['ProductoV'] = $productoVendido;
            $temp['Total'] = $total;
            array_push($ventas, $temp);
          }
          echo json_encode($ventas);
        }else {
          echo "fallo";
        }

        $sql->close();
      }
    }else {
      $sql=$conexion->prepare("select V.idVentas, V.Fecha_venta, V.Cantidad_vendida, P.Nombre_producto, V.Valor from ventas V, productos P where V.Productos_idProductos = P.idProductos AND V.Fecha_venta BETWEEN '$limiteIn' AND '$limiteS' ORDER BY V.Fecha_venta DESC");
      $sql->execute();
      $sql->bind_result($id_venta, $fecha_venta, $cantidad_vendida, $productoVendido, $total);
      if ($sql) {
        while ($sql->fetch()){
          $temp = array();
          $temp['idV'] = $id_venta;
          $temp['fechaV'] = $fecha_venta;
          $temp['CantidadV'] = $cantidad_vendida;
          $temp['ProductoV'] = $productoVendido;
          $temp['Total'] = $total;
          array_push($ventas, $temp);
        }
        echo json_encode($ventas);
      }else {
        echo "fallo";
      }

      $sql->close();
    }


    $conexion->close();

?>
