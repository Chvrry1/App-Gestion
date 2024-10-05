<?php

    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        require_once("conexion.php");

        $id = $_GET['id'];
       

        $query = "SELECT * FROM variables_inversion WHERE id_empresa = '$id' ";
        $result = $conexion->query($query);

        $array = array(); // Inicializar el array vacío

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo json_encode($row);
        } else {
            echo "No se encontraron datos";
        }

        $result->close();
        $conexion->close();
    }
?>