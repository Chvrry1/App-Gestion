<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $nombre=$_POST['nombre'];
    $sueldo=doubleval($_POST['sueldo']);
    $id_empresa= intval($_POST['id_empresa']);

     // Verificar si se proporciona un ID en la solicitud
     if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE mano_de_obra SET nombre='$nombre', sueldo='$sueldo', id_empresa='$id_empresa' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO mano_de_obra (nombre, sueldo, id_empresa) VALUES ('$nombre','$sueldo','$id_empresa')";
    }

    $result = $conexion->query($query);

    if ($result == TRUE) {
        echo "Se guardó correctamente";
    } else {
        echo "Error";
    }

    $conexion->close();

}


  

?>