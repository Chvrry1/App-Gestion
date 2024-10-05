<?php
    if ($_SERVER['REQUEST_METHOD'] == 'GET') {
        require_once("conexion.php");

        $id = $_GET['id'];
       
        $query = "SELECT sum(IMPORTE) as total_sueldo FROM gastos_personales WHERE id_empresa = '$id' and tipo_gasto ='Necesario' ";
        $result = $conexion->query($query);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $total_sueldo = doubleval($row['total_sueldo']);
            echo json_encode(array('total_sueldo' => $total_sueldo));
        } else {
            echo json_encode(array('total_sueldo' => 0)); // o cualquier otro valor predeterminado si no hay datos
        }

        $result->close();
        $conexion->close();
    }
?>

