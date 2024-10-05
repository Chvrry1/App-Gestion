<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("conexion.php");

    $id = $_GET['id'];

    $query = "SELECT SUM(sueldo) * 12 AS manoObra 
              FROM mano_de_obra AS mo 
              INNER JOIN empresa AS e ON e.id = mo.id_empresa 
              WHERE e.id = '$id'";
    $result = $conexion->query($query);

    $array = array();

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $array[] = $row;
        }
        echo json_encode($array);
    } else {
        echo "No se encontraron datos";
    }

    $result->close();
}
?>