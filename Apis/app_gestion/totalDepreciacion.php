<?php
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        require_once("conexion.php");

        $id = $_GET['id'];
       
        $query = "SELECT sum(((unidades*valor_unitario)/vida_util)/12) as total_Depreciacion FROM activo_fijo WHERE id_empresa = '$id'";
        $result = $conexion->query($query);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $totalCI = doubleval($row['total_Depreciacion']);
            echo json_encode(array("total_costos_indirectos" => $totalCI));
        } else {
            echo json_encode(array("total_costos_indirectos" => 0)); // o cualquier otro valor predeterminado si no hay datos
        }

        $result->close();
        $conexion->close();
    }
?>