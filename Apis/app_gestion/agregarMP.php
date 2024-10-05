<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $nombre=$_POST['nombre'];
    $costo= doubleval($_POST['costo']);
    $unidad=$_POST['unidad'];
    $pro_producto= doubleval($_POST['pro_producto']);
    $id_producto= intval($_POST['id_producto']);

    if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE materia_prima SET nombre='$nombre', costo='$costo', unidad='$unidad', pro_producto='$pro_producto', id_producto='$id_producto' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO materia_prima (nombre, costo, unidad, pro_producto, id_producto) VALUES ('$nombre','$costo','$unidad','$pro_producto','$id_producto')";
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