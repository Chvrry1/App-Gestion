<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    require_once("conexion.php");

    // Verifica si se proporciona un ID en la solicitud
    if (isset($_POST['id'])) {
        $id = intval($_POST['id']);

        // Construye la consulta para eliminar el registro con el ID proporcionado
        $query2 = "DELETE FROM materia_prima WHERE id_producto=$id";
        $query = "DELETE FROM producto WHERE id=$id";

        $result2 = $conexion->query($query2);
        $result = $conexion->query($query);

        if ($result === TRUE && $result2 === TRUE) {
            // La eliminación fue exitosa
            echo "Eliminación exitosa";
        } else {
            // Error al eliminar el registro
            echo "Error";
        }
    } else {
        // No se proporcionó un ID en la solicitud
        echo "No se proporcionó un ID en la solicitud";
    }

    $conexion->close();
}

?>