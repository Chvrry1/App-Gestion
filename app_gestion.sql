-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-08-2024 a las 23:29:48
-- Versión del servidor: 10.4.32-MariaDB

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `activo_diferido`
--

CREATE TABLE `activo_diferido` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `pago_anticipado` decimal(10,2) DEFAULT NULL,
  `vigencia` varchar(255) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `activo_diferido`
--

INSERT INTO `activo_diferido` (`id`, `nombre`, `pago_anticipado`, `vigencia`, `id_empresa`) VALUES
(1, 'Publicidad inicial', 30000.00, '8', 1),
(5, 'ff', 444.00, '4', 1),
(10, 'difer 1', 30000.00, '8', 2),
(12, 'Publicidad inicial', 30000.00, '8', 3),
(13, 'fsdf', 55666.00, '5', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `activo_fijo`
--

CREATE TABLE `activo_fijo` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `unidades` int(11) DEFAULT NULL,
  `valor_unitario` decimal(10,2) DEFAULT NULL,
  `vida_util` int(11) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `activo_fijo`
--

INSERT INTO `activo_fijo` (`id`, `nombre`, `unidades`, `valor_unitario`, `vida_util`, `id_empresa`) VALUES
(1, 'Escritorios', 5, 1500000.00, 10, 1),
(2, 'sillas', 10, 800000.00, 5, 1),
(9, 'activ 1', 5, 1500000.00, 10, 2),
(10, 'actv 2', 10, 800000.00, 5, 2),
(12, 'escritorio', 5, 1500000.00, 10, 3),
(13, 'sillas', 10, 800000.00, 5, 3),
(14, 'activo 1', 4, 444444.00, 4, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `costos_indirectos`
--

CREATE TABLE `costos_indirectos` (
  `id` int(11) NOT NULL,
  `nombre_concepto` varchar(255) DEFAULT NULL,
  `importe_mensual` decimal(10,2) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `costos_indirectos`
--

INSERT INTO `costos_indirectos` (`id`, `nombre_concepto`, `importe_mensual`, `id_empresa`) VALUES
(1, 'Luz', 250000.00, 1),
(2, 'Agua', 180000.00, 1),
(11, 'luz', 200000.00, 3),
(12, 'agua', 180000.00, 3),
(20, 'luz', 200000.00, 2),
(21, 'agua', 180000.00, 2),
(23, 'dsfds', 22333.00, 5),
(25, 'yy', 5000000.00, 2),
(26, 'uyy', 50.00, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE `empresa` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `doc_identificador` varchar(20) DEFAULT NULL,
  `razon_social` varchar(255) DEFAULT NULL,
  `numero_telf` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `pais` varchar(100) DEFAULT NULL,
  `capital` decimal(15,2) DEFAULT NULL,
  `usuario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`id`, `nombre`, `doc_identificador`, `razon_social`, `numero_telf`, `direccion`, `pais`, `capital`, `usuario_id`) VALUES
(1, 'empresa_prueba', NULL, NULL, NULL, NULL, NULL, 20000.00, 1),
(2, 'empresa_prueba2', '1', 'a', '1', 'a', 'a', 100000.00, 1),
(3, 'empresa 3', '11111', 'sfdfds', '342424', 'ffsdfdsf', 'ffff', 0.00, 1),
(4, 'Adriann EIRL', '20000', 'hhaa', '9888', 'na', 'peru', 20000.00, 4),
(5, 'Empresa Uno', '3355665322', 'gjjhgff', '5564773288', 'Calle 3 ggg', 'colombia', 800000.00, 3),
(6, 'empresa dos', '66666688', 'jdhdh', '726527762', 'calle 56 4444', 'peru', 20009900.00, 9),
(7, 'piñatitas Ali ', 'ine', 'piñatas ', '8125662036', 'Juarez n.l', 'México ', 4000000.00, 11),
(8, 'sadsad', '32132', 'dsads', '4423432', 'dfdsf', 'dfgdsf', 4000000.00, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `encuesta`
--

CREATE TABLE `encuesta` (
  `id` int(11) NOT NULL,
  `pregunta` varchar(255) DEFAULT NULL,
  `valoracion` varchar(255) DEFAULT NULL,
  `usuario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gastos_personales`
--

CREATE TABLE `gastos_personales` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `importe` decimal(10,2) DEFAULT NULL,
  `tipo_gasto` varchar(100) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `gastos_personales`
--

INSERT INTO `gastos_personales` (`id`, `nombre`, `importe`, `tipo_gasto`, `id_empresa`) VALUES
(1, 'Luz', 200000.00, 'Necesario', 1),
(2, 'Agua', 180000.00, 'Necesario', 1),
(3, 'Comida', 120000.00, 'Necesario', 1),
(48, 'Renta', 500000.00, 'Necesario', 3),
(49, 'Gastos medicos', 50000.00, 'Necesario', 3),
(50, 'comida', 120000.00, 'Necesario', 3),
(51, 'gimnacio', 30000.00, 'No necesario', 3),
(52, 'Netflix', 26000.00, 'No necesario', 3),
(75, 'sadasd', 500000.00, 'Necesario', 2),
(76, 'sdf', 50000.00, 'Necesario', 2),
(77, 'sf', 120000.00, 'Necesario', 2),
(79, 'dfg', 5000.00, 'Necesario', 5),
(80, 'ggg', 80000.00, 'Necesario', 5),
(81, 'mmmm', 50000.00, 'No necesario', 5),
(85, 'hh', 900.00, 'Necesario', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mano_de_obra`
--

CREATE TABLE `mano_de_obra` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `sueldo` decimal(10,2) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mano_de_obra`
--

INSERT INTO `mano_de_obra` (`id`, `nombre`, `sueldo`, `id_empresa`) VALUES
(4, 'persona 1', 200.00, 1),
(8, 'gg', 300.00, 1),
(9, 'tttttt', 3000.00, 1),
(15, 'yy', 300.00, 2),
(19, 'jj', 300.00, 3),
(36, 'retret', 500.00, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materia_prima`
--

CREATE TABLE `materia_prima` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `costo` decimal(10,2) DEFAULT NULL,
  `unidad` varchar(20) DEFAULT NULL,
  `pro_producto` decimal(10,2) DEFAULT NULL,
  `id_producto` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `materia_prima`
--

INSERT INTO `materia_prima` (`id`, `nombre`, `costo`, `unidad`, `pro_producto`, `id_producto`) VALUES
(2, 'II', 50.00, 'libra', 1.00, 1),
(3, 'uu', 100.00, 'kilo', 3.00, 2),
(17, 'mater', 3333.00, 'bb', 3.00, 1),
(20, 'kk', 100.00, 'kilo', 3.00, 11),
(21, 'II', 50.00, 'libra', 1.00, 11),
(22, 'uu', 100.00, 'kilo', 3.00, 12),
(23, 'materia p2', 100.00, 'kilo', 3.00, 8),
(24, 'matp 1', 50.00, 'bb', 1.00, 8),
(26, 'uu', 100.00, 'kilo', 3.00, 15),
(27, 'dgfg', 454545.00, 'ff', 4.00, 16),
(28, 'ffd', 44.00, 'kk', 3.90, 17);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `proyeccion_venta` int(11) DEFAULT NULL,
  `precio_venta` decimal(10,2) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id`, `nombre`, `proyeccion_venta`, `precio_venta`, `id_empresa`) VALUES
(1, 'Queso', 790, 1500.00, 1),
(2, 'Crema de leche', 550, 1500.00, 1),
(8, 'prod 1', 790, 1500.00, 2),
(11, 'Queso', 790, 1500.00, 3),
(12, 'crema de leche', 550, 1500.00, 3),
(15, 'pro 2', 550, 1500.00, 2),
(16, 'retret', 4, 4.00, 5),
(17, 'tert', 455555, 5.00, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `username` varchar(10) DEFAULT NULL,
  `password` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `username`, `password`) VALUES
(1, 'admin', '123'),
(2, 'adriann', '123'),
(3, 'mateo', '123'),
(4, 'adri', '123'),
(6, 'Sergio ', 'erick12'),
(7, 'juan', 'juan'),
(9, 'usuario2', '123'),
(10, 'piñatitas', 'ALI.ALIG'),
(11, 'piñatitas', 'ALI.ali'),
(12, 'Rodrigo ', '123'),
(13, 'axel', '123'),
(14, 'Aleida', '123'),
(25, 'CarlosR', 'cvGhtq60');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `variables_inversion`
--

CREATE TABLE `variables_inversion` (
  `id` int(11) NOT NULL,
  `inflacion` decimal(10,2) DEFAULT NULL,
  `tasa_libre_riesgo` decimal(10,2) DEFAULT NULL,
  `id_empresa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `variables_inversion`
--

INSERT INTO `variables_inversion` (`id`, `inflacion`, `tasa_libre_riesgo`, `id_empresa`) VALUES
(38, 5.00, 6.00, 2),
(39, 5.00, 8.00, 3),
(40, 0.00, 5.00, 1),
(41, 0.00, 20.00, 8);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `activo_diferido`
--
ALTER TABLE `activo_diferido`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `activo_fijo`
--
ALTER TABLE `activo_fijo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `costos_indirectos`
--
ALTER TABLE `costos_indirectos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_empresa_usuario` (`usuario_id`);

--
-- Indices de la tabla `encuesta`
--
ALTER TABLE `encuesta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_empresa_usuario` (`usuario_id`);

--
-- Indices de la tabla `gastos_personales`
--
ALTER TABLE `gastos_personales`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `mano_de_obra`
--
ALTER TABLE `mano_de_obra`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `materia_prima`
--
ALTER TABLE `materia_prima`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `variables_inversion`
--
ALTER TABLE `variables_inversion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `activo_diferido`
--
ALTER TABLE `activo_diferido`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `activo_fijo`
--
ALTER TABLE `activo_fijo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `costos_indirectos`
--
ALTER TABLE `costos_indirectos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `empresa`
--
ALTER TABLE `empresa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `encuesta`
--
ALTER TABLE `encuesta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=134;

--
-- AUTO_INCREMENT de la tabla `gastos_personales`
--
ALTER TABLE `gastos_personales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

--
-- AUTO_INCREMENT de la tabla `mano_de_obra`
--
ALTER TABLE `mano_de_obra`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT de la tabla `materia_prima`
--
ALTER TABLE `materia_prima`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de la tabla `variables_inversion`
--
ALTER TABLE `variables_inversion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `activo_diferido`
--
ALTER TABLE `activo_diferido`
  ADD CONSTRAINT `activo_diferido_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `activo_fijo`
--
ALTER TABLE `activo_fijo`
  ADD CONSTRAINT `activo_fijo_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `costos_indirectos`
--
ALTER TABLE `costos_indirectos`
  ADD CONSTRAINT `costos_indirectos_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD CONSTRAINT `fk_empresa_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `gastos_personales`
--
ALTER TABLE `gastos_personales`
  ADD CONSTRAINT `gastos_personales_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `mano_de_obra`
--
ALTER TABLE `mano_de_obra`
  ADD CONSTRAINT `mano_de_obra_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `materia_prima`
--
ALTER TABLE `materia_prima`
  ADD CONSTRAINT `materia_prima_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`);

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
