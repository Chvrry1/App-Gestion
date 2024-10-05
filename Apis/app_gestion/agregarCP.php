<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        require_once("conexion.php");

        $nombre_concepto = $_POST['nombre_concepto'];
        $importe_mensual =doubleval($_POST['importe_mensual']);
        $id_empresa = intval($_POST['id_empresa']);

        // Verificar si se proporciona un ID en la solicitud
        if (isset($_POST['id'])) {
            $id = intval($_POST['id']);
            // Realizar una actualización en lugar de una inserción
            $query = "UPDATE costos_indirectos SET nombre_concepto='$nombre_concepto', importe_mensual='$importe_mensual' WHERE id=$id";
        } else {
            // Si no se proporciona un ID, realizar una inserción normal
            $query = "INSERT INTO costos_indirectos (nombre_concepto, importe_mensual, id_empresa) 
                      VALUES ('$nombre_concepto', '$importe_mensual', '$id_empresa')";
        }

        if ($conexion->query($query) === TRUE) {
            echo "Se guardó correctamente";
        } else {
            echo "Error al guardar: " . $mysql->error;
        }

        $conexion->close();
    }
?>