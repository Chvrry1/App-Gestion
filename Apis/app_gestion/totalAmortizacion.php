
<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    require_once("conexion.php");

    $id = $_GET['id'];

    $query = "SELECT sum((pago_anticipado/vigencia)/12) as total_amortizacion FROM activo_diferido WHERE id_empresa = '$id'";
    $result = $conexion->query($query);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $totalAmortizacion = doubleval($row['total_amortizacion']);
        echo json_encode(array('total_amortizacion' => $totalAmortizacion));
    } else {
        echo "0"; // o cualquier otro valor predeterminado si no hay datos
    }

    $result->close();
    $conexion->close();
}
?>