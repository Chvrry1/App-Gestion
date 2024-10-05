<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $nombre=$_POST['nombre'];
    $proyeccion_venta= intval($_POST['proyeccion_venta']);
    $precio_venta=doubleval($_POST['precio_venta']);
    $id_empresa= intval($_POST['id_empresa']);


    if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE producto SET nombre='$nombre', proyeccion_venta='$proyeccion_venta', precio_venta='$precio_venta', id_empresa='$id_empresa' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO producto (nombre, proyeccion_venta, precio_venta, id_empresa) VALUES ('$nombre','$proyeccion_venta','$precio_venta','$id_empresa')";
    }
    $result = $conexion->query($query);
    if($result == true){
        $id_creado = mysqli_insert_id($conexion); 
        echo json_encode(array('id_producto' => $id_creado));
    }else{
        echo "error al agregar";
    }
    $conexion->close();

}


  

?>