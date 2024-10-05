<?php

    if($_SERVER['REQUEST_METHOD'] == 'GET'){
        require_once("conexion.php");

        $id = $_GET['id'];

        $query = "SELECT * FROM gastos_personales WHERE id_empresa = '$id'";
        $result = $conexion -> query($query);

        if($conexion -> affected_rows > 0){
            while($row = $result -> fetch_assoc() ){
                $array = $row;
            }

            echo json_encode($array);
        }else{
            echo "No se encontraron datos";
        }

        $result -> close();
        $conexion -> close();
    }