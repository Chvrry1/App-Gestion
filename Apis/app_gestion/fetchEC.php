<?php

    if($_SERVER['REQUEST_METHOD'] == 'GET'){
        require_once("conexion.php");

        $id = $_GET['id'];

        $query = "SELECT * FROM encuesta WHERE usuario_id = '$id'";
        $result = $conexion -> query($query);
        $array = array(); 

        if($result -> num_rows > 0){
            while($row = $result -> fetch_assoc() ){
                $array[]= $row;
            }

            echo json_encode($array);
        }else{
            echo json_encode($array);
            
        }

        $result -> close();
   
    }