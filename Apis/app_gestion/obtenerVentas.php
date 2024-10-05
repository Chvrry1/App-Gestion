<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("conexion.php");

    $id = $_GET['id'];

    $query = "SELECT SUM(p.proyeccion_venta * p.precio_venta) * 12 AS Ventas 
              FROM producto AS p 
              INNER JOIN empresa AS e ON e.id = p.id_empresa 
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