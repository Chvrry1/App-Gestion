<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $inflacion= doubleval($_POST['inflacion']);
    $tasa_libre_riesgo=doubleval($_POST['tasa_libre_riesgo']);
    $id_empresa= intval($_POST['id_empresa']);


    // Verificar si se proporciona un ID en la solicitud
    if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE variables_inversion SET inflacion='$inflacion', tasa_libre_riesgo='$tasa_libre_riesgo', id_empresa='$id_empresa' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO variables_inversion (inflacion, tasa_libre_riesgo, id_empresa) VALUES ('$inflacion','$tasa_libre_riesgo','$id_empresa')";
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