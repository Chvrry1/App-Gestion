<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    require_once("conexion.php");

    $nombre = $_POST['nombre'];
    $importe = doubleval($_POST['importe']);
    $tipo_gasto = $_POST['tipo_gasto'];
    $id_empresa = intval($_POST['id_empresa']);

    // Verificar si se proporciona un ID en la solicitud
    if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE gastos_personales SET nombre='$nombre', importe='$importe', tipo_gasto='$tipo_gasto', id_empresa='$id_empresa' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO gastos_personales (nombre, importe, tipo_gasto, id_empresa) VALUES ('$nombre','$importe','$tipo_gasto','$id_empresa')";
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