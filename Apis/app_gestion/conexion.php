<?php
    $conexion = new mysqli(
        "localhost",
        "root",
        "",
        "app_gestion"
    );

    if($conexion -> connect_error){
        die("Failed to connect". $mysql -> connect_error);
    }