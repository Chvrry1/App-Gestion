<?php
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        require_once("conexion.php");

        $id = $_GET['id'];

        $query = "SELECT sum(IMPORTE_mensual) as total_CI FROM costos_indirectos WHERE id_empresa = '$id'";
        $result = $conexion->query($query);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $total_CI = doubleval($row['total_CI']);
            echo json_encode(array('total_CI' => $total_CI));
        } else {
            echo json_encode(array('total_CI' => 0)); // o cualquier otro valor predeterminado si no hay datos
        }

        $result->close();
        $conexion->close();
    }
?>