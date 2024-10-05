<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    include("conexion.php");

    // Obtener datos del cuerpo de la solicitud
    $data = json_decode(file_get_contents("php://input"), true);

    // Verificar si el ID de usuario está presente en los datos recibidos
    if (!isset($data['user_id'])) {
        echo json_encode(array("mensaje" => "El parámetro 'user_id' es requerido"));
        exit;
    }

    // Obtener el ID de usuario del cuerpo de la solicitud
    $user_id = $data['user_id'];

    // Obtener la lista de empresas asociadas al usuario
    $query = "SELECT id, nombre, capital FROM empresa WHERE usuario_id = ?";
    if ($stmt = $conexion->prepare($query)) {
        $stmt->bind_param('i', $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        $empresas = array();
        while ($row = $result->fetch_assoc()) {
            $empresas[] = $row;
        }

        if (count($empresas) > 0) {
            // Devolver la lista de empresas asociadas al usuario
            echo json_encode(array("empresas" => $empresas));
        } else {
            // No se encontraron empresas asociadas al usuario
            echo json_encode(array("mensaje" => "No se encontraron empresas para este usuario"));
        }
    } else {
        // Error en la preparación de la consulta
        echo json_encode(array("mensaje" => "Error en la preparación de la consulta de empresas"));
    }

    // Cerrar la conexión y liberar los recursos
    $conexion->close();
}
?>
