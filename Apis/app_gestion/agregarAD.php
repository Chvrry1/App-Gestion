<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $nombre=$_POST['nombre'];
    $pago_anticipado=doubleval($_POST['pago_anticipado']);
    $vigencia=$_POST['vigencia'];
    $id_empresa= intval($_POST['id_empresa']);

     // Verificar si se proporciona un ID en la solicitud
     if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE activo_diferido SET nombre='$nombre', pago_anticipado='$pago_anticipado', vigencia='$vigencia', id_empresa='$id_empresa' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO activo_diferido (nombre, pago_anticipado, vigencia, id_empresa) VALUES ('$nombre','$pago_anticipado','$vigencia','$id_empresa')";
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