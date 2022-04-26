<?php
    include 'conexion.php';
    $id = $_POST['id'];
    $producto = $_POST['producto'];
    $descripcion = $_POST['descripcion'];
    $proveedor = $_POST['proveedor'];
    $cantidad = $_POST['cantidad'];
    $precio = $_POST['precio'];
    $tamaño = $_POST['tamaño'];
    $alcohol = $_POST['alcoholGrados'];

    $sql = "UPDATE productos SET Nombre_producto='$producto',Descripcion='$descripcion',Cantidad='$cantidad',PrecioU='$precio',Tamaño='$tamaño',Grados_alcohol='$alcohol',Proveedores_idProveedores='$proveedor' WHERE idProductos = '$id'";
    $result = mysqli_query($conexion,$sql);
    if ($result) {
      echo "1";
    }
    else {
      echo "error".$sql;
    }

    mysqli_close($conexion);

?>
