<?php

    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        require_once("conexion.php");

        $id = $_GET['id'];
       

        $query = "SELECT * FROM activo_fijo WHERE id_empresa = '$id' ";
        $result = $conexion->query($query);

        $array = array(); // Inicializar el array vacío

        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                // Agregar cada fila al array
                $array[] = $row;
            }

            echo json_encode($array);
        } else {
            echo "No se encontraron datos";
        }

        $result->close();
        $conexion->close();
    }
?>