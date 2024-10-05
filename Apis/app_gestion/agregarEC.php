<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $pregunta=$_POST['pregunta'];
    $valoracion=$_POST['valoracion'];
    $usuario_id= intval($_POST['usuario_id']);

     // Verificar si se proporciona un ID en la solicitud
     if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE encuesta SET pregunta='$pregunta', valoracion='$valoracion', usuario_id='$usuario_id' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO encuesta (pregunta, valoracion, usuario_id) VALUES ('$pregunta','$valoracion','$usuario_id')";
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