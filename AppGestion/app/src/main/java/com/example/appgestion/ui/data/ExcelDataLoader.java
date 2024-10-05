package com.example.appgestion.ui.data;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.MainActivity;
import com.example.appgestion.R;
import com.example.appgestion.ui.inventario.AgregarProductos;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ExcelDataLoader {

    private final Context context;
    private final ExecutorService executor;
    private final Handler handler;
    private int empresaId;
    private static final String url = "http://192.168.0.112/app_gestion/agregarCP.php";

    public ExcelDataLoader(Context context, ExecutorService executor, Handler handler, int empresaId) {
        this.context = context;
        this.executor = executor;
        this.handler = handler;
        this.empresaId = empresaId;
    }

    public void readExcelFile(Uri uri, MenuItem importExcelMenuItem) {
        // Mostrar un AlertDialog para confirmar si el usuario quiere continuar
        new AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("Por favor, verifica el formato del archivo Excel antes de continuar. ¿Estás seguro de que deseas continuar?")
                .setPositiveButton("Continuar", (dialog, which) -> {
                    // Crear y mostrar un ProgressDialog con un círculo de progreso
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Procesando archivo Excel...");
                    progressDialog.setCancelable(false); // Evita que el usuario lo cierre accidentalmente
                    progressDialog.show();

                    executor.execute(() -> {
                        Workbook workbook = null;
                        InputStream inputStream = null;
                        List<List<String>> results;
                        List<String> results2 = new ArrayList<>();;
                        int rowSelected = 0;
                        try {
                            ContentResolver contentResolver = context.getContentResolver();
                            inputStream = contentResolver.openInputStream(uri);
                            workbook = new XSSFWorkbook(inputStream);

                            // Procesar la hoja de Excel
                            Sheet sheet = workbook.getSheetAt(1);
                            results = readCustomRange(sheet, 3, 1, 2, "Sueldo");
                            for (List<String> item : results) {
                                String name = item.get(0);
                                Double importe = Double.parseDouble(item.get(1));

                                createCostosIndirectos(name, importe, empresaId);
                            }
                            results = readCustomRange(sheet, 3, 4, 5, "Total");
                            for (List<String> item : results) {
                                String name = item.get(0);
                                Double importe = Double.parseDouble(item.get(1));
                                createGastoPersonal(name, importe, "Necesario", empresaId);
                            }
                            results = readCustomRange(sheet, 3, 6, 7, "Total");
                            for (List<String> item : results) {
                                String name = item.get(0);
                                Double importe = Double.parseDouble(item.get(1));
                                createGastoPersonal(name, importe, "No necesario", empresaId);
                            }

                            sheet = workbook.getSheetAt(2);
                            results = readCustomRange(sheet, 4, 0, 4, "Empty cell");
                            for (List<String> item : results) {
                                String name = item.get(0);
                                int unidades = (int) Double.parseDouble(item.get(1));
                                Double valor = Double.parseDouble(item.get(2));
                                int vida =  (int) Double.parseDouble(item.get(4));

                                createActivoFijo(name,unidades,valor,vida, empresaId);
                            }
                            rowSelected = findTextInColumn(sheet, 20, 0, "Activo Diferido") + 1;
                            System.out.println(rowSelected);
                            results = readCustomRange(sheet, rowSelected, 0, 4, "Total");
                            for (List<String> item : results) {
                                String name = item.get(0);
                                Double pago = Double.parseDouble(item.get(3));
                                String vigencia = item.get(4);

                                createActivoDiferido(name,pago,vigencia, empresaId);
                            }
                            sheet = workbook.getSheetAt(4);
                            results = readCustomRange(sheet, 1, 2, 2, "Empty cell");
                            for (List<String> sublist : results) {
                                results2.add(sublist.get(0));
                            }
                            sheet = workbook.getSheetAt(3);
                            results = readCustomRange(sheet, 2, 1, 3, "Empty cell");
                            while (results2.size() < results.size()) {
                                results2.add("0");
                            }

                            int productCount = results.size();
                            rowSelected = findTextInColumn(sheet, 3, 0, "Producto 1:") + 1;
                            System.out.println(rowSelected);

                            for (int i = 0; i < productCount; i++) {
                                if (i > 0) {
                                    rowSelected = findTextInColumn(sheet, rowSelected, 0, "Producto " + (i + 1)) + 1;
                                }

                                List<String> productData = results.get(i);
                                String productName = productData.get(0);
                                int proyeccionVenta = (int) Double.parseDouble(productData.get(2));
                                double precioVenta = Double.parseDouble(results2.get(i));

                                int finalRowSelected = rowSelected;
                                Sheet finalSheet = sheet;
                                createProducto(productName, proyeccionVenta, precioVenta, empresaId, new ProductCallback() {
                                    @Override
                                    public void onSuccess(boolean success, int productId) {
                                        if (success) {
                                            List<List<String>> materialResults = readCustomRange(finalSheet, finalRowSelected, 0, 3, "Empty cell");

                                            for (List<String> materialData : materialResults) {
                                                String materialName = materialData.get(0);
                                                double materialCost = Double.parseDouble(materialData.get(1));
                                                String materialUnit = materialData.get(2);
                                                double materialQuantity = Double.parseDouble(materialData.get(3));

                                                createMateriaPrima(materialName, materialCost, materialUnit, materialQuantity, productId);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                    }
                                });
                            }
                            results = readCustomRange(sheet, 2, 6, 7, "Total");
                            for (List<String> item : results) {
                                String name = item.get(0);
                                Double pago = Double.parseDouble(item.get(1));

                                createManoDeObra(name,pago, empresaId);
                            }

                            handler.post(progressDialog::dismiss);
                            handler.post(() -> {
                                ((MainActivity) context).recreate();
                                Toast.makeText(context, "El archivo Excel se ha procesado correctamente", Toast.LENGTH_SHORT).show();
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Error al leer el archivo Excel", Toast.LENGTH_SHORT).show();
                            });
                        } finally {
                            try {
                                if (workbook != null) workbook.close();
                                if (inputStream != null) inputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    public int findTextInColumn(Sheet sheet, int startRow, int column, String searchText) {
        for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell cell = row.getCell(column);
                if (cell != null) {
                    String cellValue = getCellValue(cell);
                    if (cellValue.contains(searchText)) {
                        return rowIndex;
                    }
                }
            }
        }
        return -1;
    }

    private List<List<String>> readCustomRange(Sheet sheet, int startRow, int startCol, int endCol, String stopWord) {
        List<List<String>> data = new ArrayList<>();
        boolean foundStopWord = false;
        int rowIndex = startRow;

        while (!foundStopWord) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                break;
            }

            List<String> rowData = new ArrayList<>();
            boolean hasData = false;
            boolean isStopWordRow = false;

            for (int colIndex = startCol; colIndex <= endCol; colIndex++) {
                Cell cell = row.getCell(colIndex);
                String cellValue = getCellValue(cell);

                if (cellValue.equals("Empty cell")) {
                    rowData.add("0");
                } else {
                    rowData.add(cellValue);
                    hasData = true;
                }

                if (colIndex == startCol && cellValue.equals(stopWord)) {
                    foundStopWord = true;
                    isStopWordRow = true;
                    break;
                }
            }

            if (hasData && !isStopWordRow) {
                data.add(rowData);
            }

            rowIndex++;
        }

        return data;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "Empty cell";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.format("%.2f", cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.format("%.2f", cell.getNumericCellValue());
                }
            default:
                return "Empty cell";
        }
    }

    private void createCostosIndirectos(final String name, final Double importe, final int idEmpresa) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
        }, volleyError -> Toast.makeText(context, "Error al guardar: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<>();
                datos.put("nombre_concepto", name);
                datos.put("importe_mensual", String.valueOf(importe));
                datos.put("id_empresa", String.valueOf(idEmpresa));
                return datos;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createGastoPersonal(final String name, final Double importe, final String tipoGasto, final int idEmpresa) {
        String url = "http://192.168.0.112/app_gestion/agregarGP.php";  // URL del API para agregar

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
        }, volleyError -> Toast.makeText(context, "Error al agregar gasto: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("importe", String.valueOf(importe));
                params.put("tipo_gasto", tipoGasto);
                params.put("id_empresa", String.valueOf(idEmpresa));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createActivoDiferido(final String name, final Double pago, final String vigencia, final int idEmpresa) {
        String url = "http://192.168.0.112/app_gestion/agregarAD.php";  // URL del API para agregar activos diferidos

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
        }, volleyError -> Toast.makeText(context, "Error al agregar activo: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("pago_anticipado", String.valueOf(pago));
                params.put("vigencia", vigencia);
                params.put("id_empresa", String.valueOf(idEmpresa));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createActivoFijo(final String name, final Integer unidades, final Double valorUnitario, final Integer vidaUtil, final int idEmpresa) {
        String url = "http://192.168.0.112/app_gestion/agregarAF.php";  // URL del API para agregar activos fijos

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

        }, volleyError -> Toast.makeText(context, "Error al agregar activo fijo: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("unidades", String.valueOf(unidades));
                params.put("valor_unitario", String.valueOf(valorUnitario));
                params.put("vida_util", String.valueOf(vidaUtil));
                params.put("id_empresa", String.valueOf(idEmpresa));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createProducto(final String name, final Integer proyeccion_venta, final Double precio_venta, final int idEmpresa, final ProductCallback callback) {
        String url = "http://192.168.0.112/app_gestion/agregarIN.php";  // URL del API para agregar productos

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int productId = jsonObject.getInt("id_producto");  // Extrae el id del producto del JSON
                callback.onSuccess(true, productId);  // Llama al callback con el id del producto
            } catch (JSONException e) {
                callback.onFailure("Error al procesar la respuesta del servidor: " + e.getMessage());
            }
        }, volleyError -> {
            callback.onFailure("Error al agregar producto: " + volleyError.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("proyeccion_venta", String.valueOf(proyeccion_venta));
                params.put("precio_venta", String.valueOf(precio_venta));
                params.put("id_empresa", String.valueOf(idEmpresa));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createMateriaPrima(final String name, final Double costo, final String unidadMedida, final Double proProducto, final int idProducto) {
        String url = "http://192.168.0.112/app_gestion/agregarMP.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
        }, volleyError -> {
            Toast.makeText(context, "Error al agregar materia prima: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("costo", String.valueOf(costo));
                params.put("unidad_medida", unidadMedida);
                params.put("pro_producto", String.valueOf(proProducto));
                params.put("id_producto", String.valueOf(idProducto));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createManoDeObra(final String name, final Double sueldo, final int empresaId) {
        String url = "http://192.168.0.112/app_gestion/agregarMO.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
        }, volleyError -> {
            Toast.makeText(context, "Error al agregar materia prima: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("sueldo", String.valueOf(sueldo));
                params.put("id_empresa", String.valueOf(empresaId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }




    public interface ProductCallback {
        void onSuccess(boolean success, int productId);
        void onFailure(String errorMessage);
    }

}
