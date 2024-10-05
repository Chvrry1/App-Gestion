<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("conexion.php");

    $id = $_GET['id'];

    $query = "
    SELECT 
        (SELECT SUM(ma.costo * ma.pro_producto * p.proyeccion_venta) 
         FROM materia_prima as ma 
         INNER JOIN producto as p ON ma.id_producto = p.id 
         WHERE p.id_empresa = ?) AS costo_total,
        SUM(p.proyeccion_venta * p.precio_venta) AS ventas_totales
    FROM 
        producto as p
    WHERE 
        p.id_empresa = ?;
    ";

    if ($stmt = $conexion->prepare($query)) {
        $stmt->bind_param('ii', $id, $id);

        $stmt->execute();

        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();

            $array = array(
                'costo_total' => $row['costo_total'],
                'ventas_totales' => $row['ventas_totales']
            );

            echo json_encode($array);
        } else {
            echo json_encode(array("mensaje" => "No se encontraron datos"));
        }

        $stmt->close();
    } else {
        echo json_encode(array("mensaje" => "Error en la preparación de la consulta"));
    }

    $conexion->close();
}
?>