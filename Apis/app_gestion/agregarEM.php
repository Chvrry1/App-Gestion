<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    include 'conexion.php'; // Asegúrate de tener un archivo conexion.php con los detalles de la conexión a tu base de datos

    $nombre = $_POST['nombre'];
    $doc_identificador = $_POST['doc_identificador'];
    $razon_social = $_POST['razon_social'];
    $numero_telf = $_POST['numero_telf'];
    $direccion = $_POST['direccion'];
    $pais = $_POST['pais'];
    $capital = $_POST['capital'];
    $usuario_id = $_POST['usuario_id'];

    // Validar entradas
    if (!empty($nombre) && !empty($doc_identificador) && !empty($razon_social) && !empty($numero_telf) && !empty($direccion) && !empty($pais) && !empty($capital) && !empty($usuario_id)) {
        // Preparar consulta para evitar inyecciones SQL
        $stmt = $conexion->prepare("INSERT INTO empresa (nombre, doc_identificador, razon_social, numero_telf, direccion, pais, capital, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssssdi", $nombre, $doc_identificador, $razon_social, $numero_telf, $direccion, $pais, $capital, $usuario_id);

        // Ejecutar consulta
        if ($stmt->execute()) {
            echo "Empresa registrada correctamente";
        } else {
            echo "Error al registrar la empresa: " . $stmt->error;
        }

        // Cerrar declaración y conexión
        $stmt->close();
    } else {
        echo "Por favor, complete todos los campos.";
    }

    mysqli_close($conexion);
}
?>
