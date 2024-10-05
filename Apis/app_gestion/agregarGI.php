<?php
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'conexion.php';
    $nombre_concepto=$_POST['nombre_concepto'];
    $importe_mensual= doubleval($_POST['importe_mensual']);
    $id_empresa= intval($_POST['id_empresa']);


    $consulta="INSERT INTO costos_indirectos (nombre_concepto, importe_mensual, id_empresa) VALUES ('$nombre_concepto','$importe_mensual','$id_empresa')";
    $result= mysqli_query($conexion,$consulta);
    if($result == true){
        echo "datos agregados correctamente";
    }else{
        echo "error al agregar";
    }
    mysqli_close($conexion);

}


  

?>