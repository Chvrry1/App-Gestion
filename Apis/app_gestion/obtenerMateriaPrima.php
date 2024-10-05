<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("conexion.php");

    $id = $_GET['id'];

    // Asegúrate de que los nombres de las columnas y las tablas sean correctos
    $query = "SELECT SUM(p.proyeccion_venta * MP.total_costo) * 12 AS materiaprima
              FROM producto AS p
              INNER JOIN empresa AS e ON e.id = p.id_empresa
              INNER JOIN (
                  SELECT id_producto, SUM(costo * pro_producto) AS total_costo
                  FROM materia_prima
                  GROUP BY id_producto
              ) AS MP ON MP.id_producto = p.id
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