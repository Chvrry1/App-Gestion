<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $nombre=$_POST['nombre'];
    $unidades= intval($_POST['unidades']);
    $valor_unitario=doubleval($_POST['valor_unitario']);
    $vida_util=intval($_POST['vida_util']);
    $id_empresa= intval($_POST['id_empresa']);


     // Verificar si se proporciona un ID en la solicitud
     if (isset($_POST['id'])) {
        $id = intval($_POST['id']);
        // Realizar una actualización en lugar de una inserción
        $query = "UPDATE activo_fijo SET nombre='$nombre', unidades='$unidades', valor_unitario='$valor_unitario', vida_util='$vida_util', id_empresa='$id_empresa' WHERE id=$id";
    } else {
        // Si no se proporciona un ID, realizar una inserción normal
        $query = "INSERT INTO activo_fijo (nombre, unidades, valor_unitario, vida_util, id_empresa) VALUES ('$nombre','$unidades','$valor_unitario','$vida_util','$id_empresa')";
    }

    $result = $conexion->query($query);

    if ($result == TRUE) {
        echo "Se guardó correctamente";
    } else {
        echo "Error";
    }


}


  

?>