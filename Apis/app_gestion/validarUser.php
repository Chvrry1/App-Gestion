<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    include("conexion.php");

    // Obtener datos del cuerpo de la solicitud
    $data = json_decode(file_get_contents("php://input"), true);

    // Verificar si los datos requeridos están presentes
    if (!isset($data['user']) || !isset($data['contraseña'])) {
        echo json_encode(array("mensaje" => "Los parámetros 'user' y 'contraseña' son requeridos"));
        exit;
    }

    // Obtener el nombre de usuario y la contraseña del cuerpo de la solicitud
    $username = $data['user'];
    $password = $data['contraseña'];

    // Validar el usuario y la contraseña en la base de datos
    $query = "SELECT id, username FROM usuario WHERE username = ? AND password = ?";
    if ($stmt = $conexion->prepare($query)) {
        $stmt->bind_param('ss', $username, $password);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            // El usuario y la contraseña son válidos
            $row = $result->fetch_assoc();
            $usuario = array(
                "id" => $row['id'],
                "username" => $row['username']
            );
            echo json_encode(array("mensaje" => "Inicio de sesión exitoso", "usuario" => $usuario));
        } else {
            // El usuario o la contraseña son incorrectos
            echo json_encode(array("mensaje" => "Usuario o contraseña incorrectos"));
        }
    } else {
        // Error en la preparación de la consulta
        echo json_encode(array("mensaje" => "Error en la preparación de la consulta de usuario"));
    }

    // Cerrar la conexión y liberar los recursos
    $conexion->close();
}
?>