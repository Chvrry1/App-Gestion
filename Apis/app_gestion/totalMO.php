
<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("conexion.php");

    $id = $_GET['id'];


    $query = "SELECT sum(sueldo) AS total_Mano_obra FROM `mano_de_obra` WHERE id_empresa = ?;";

    if ($stmt = $conexion->prepare($query)) {
        $stmt->bind_param('i', $id);

        $stmt->execute();

        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();

            $array = array(
                'total_Mano_obra' => $row['total_Mano_obra']
                
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