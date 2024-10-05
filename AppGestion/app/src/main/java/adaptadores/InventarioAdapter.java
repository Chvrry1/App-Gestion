package adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appgestion.R;
import com.example.appgestion.ui.gastos.AgregarGastos;
import com.example.appgestion.ui.gastos.ClsCostosIndirectos;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;
import com.example.appgestion.ui.inventario.AgregarProductos;
import com.example.appgestion.ui.inventario.ClsInventario;
import com.example.appgestion.ui.inventario.ClsMateriaPrima;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsInventario> inventarioList;
    private OnItemClickListener mListener;
    private int id_empresa;

    public interface OnItemClickListener {
        void onItemClick(ClsInventario item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }




    public InventarioAdapter(Context context, List<ClsInventario> inventarioList, int id_empresa) {
        this.mContext = context;
        this.inventarioList = inventarioList;
        this.id_empresa=id_empresa;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventario, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // TODO: Put ViewHolder binding code here in MDC-102
        if (inventarioList != null && position < inventarioList.size()) {
            ClsInventario inventario = inventarioList.get(position);
            holder.descripcion_prod.setText(inventario.descripcion);
            holder.unidades_prod.setText(String.valueOf("Unidades: "+inventario.proyeccion_venta));
            holder.precio_venta.setText(String.valueOf("Precio de venta: "+inventario.precio_venta));
            holder.txt_id_in.setText(inventario.id);
            //imageRequester.setImageFromUrl(holder.productImage, product.url);

            //agregar al item los calculos de la materia prima
            ClsMateriaPrima.readMP(mContext, inventario.id, new ClsMateriaPrima.VolleyCallback() {
                @Override
                public void onSuccess(List<ClsMateriaPrima> materiaPrimaList) {
                    try {
                        double consumo_mp = 0;
                        double ganancia= 0;
                        for (ClsMateriaPrima materiaPrima:materiaPrimaList){
                            consumo_mp += materiaPrima.costo*materiaPrima.pro_producto;

                        }
                        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                        simbolos.setGroupingSeparator('.');
                        simbolos.setDecimalSeparator(',');
                        DecimalFormat decimalFormat = new DecimalFormat("#,###.00",simbolos);
                        DecimalFormat porcentajeFormat = new DecimalFormat("###.##%",simbolos);
                        ganancia = inventario.precio_venta - consumo_mp;
                        holder.materia_prima_unitaria.setText("materia prima unitaria: "+decimalFormat.format(consumo_mp));
                        holder.materia_prima_prod.setText("$ "+ decimalFormat.format(consumo_mp * inventario.proyeccion_venta));
                        holder.ganancia_prod.setText("Ganancia: "+ decimalFormat.format(ganancia));
                        holder.porcentaje_ganancia.setText("Porcentaje de ganancia: "+porcentajeFormat.format(ganancia/inventario.precio_venta));
                        holder.ingreso_ventas.setText("$ "+decimalFormat.format(inventario.precio_venta*inventario.proyeccion_venta));

                    }catch (Exception e){
                        System.out.println("error al colocar la materia prima"+e);
                        System.out.println(inventario.id);
                    }


                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });

            //enviar al formulario

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getLayoutPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ClsInventario productoSeleccionado = inventarioList.get(adapterPosition);
                        Intent formProductos = new Intent(mContext, AgregarProductos.class);
                        formProductos.putExtra("id_producto", Integer.parseInt(productoSeleccionado.id));
                        formProductos.putExtra("nombre",productoSeleccionado.descripcion);
                        formProductos.putExtra("proyeccion_venta",productoSeleccionado.proyeccion_venta);
                        formProductos.putExtra("precio_venta",productoSeleccionado.precio_venta);
                        formProductos.putExtra("id_empresa",id_empresa);
                        mContext.startActivity(formProductos);
                    }
                }
            });
            holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
                int adapterPosition = holder.getLayoutPosition();
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                    alertDialogBuilder.setTitle("Eliminar Producto "+inventario.descripcion);

                    alertDialogBuilder
                            .setMessage("¿Esta seguro de eliminar este producto?")
                            .setCancelable(false)
                            .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    eliminar_prod(inventario.id,id_empresa,new DefaultCallback() {
                                        @Override
                                        public void onSuccess(boolean success) {
                                            Toast.makeText(mContext, "Producto Eliminado", Toast.LENGTH_SHORT).show();
                                            inventarioList.remove(adapterPosition);
                                            notifyItemRemoved(adapterPosition);


                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {

                                        }
                                    });

                                    //Si la respuesta es afirmativa aquí agrega tu función a realizar.
                                }
                            })
                            .setNegativeButton("cancelar",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            }).create().show();

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return inventarioList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_in,descripcion_prod,precio_venta, unidades_prod,materia_prima_unitaria,ganancia_prod,porcentaje_ganancia, materia_prima_prod,ingreso_ventas;
        ImageView btn_eliminar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_id_in=itemView.findViewById(R.id.txt_id_in);
            precio_venta=itemView.findViewById(R.id.precio_venta);
            unidades_prod=itemView.findViewById(R.id.unidades_prod);
            descripcion_prod=itemView.findViewById(R.id.descripcion_prod);
            materia_prima_unitaria=itemView.findViewById(R.id.materia_prima_unitaria);
            ganancia_prod =itemView.findViewById(R.id.ganancia_prod);
            porcentaje_ganancia=itemView.findViewById(R.id.porcentaje_ganancia);
            materia_prima_prod = itemView.findViewById(R.id.materia_prima_prod);
            ingreso_ventas=itemView.findViewById(R.id.ingreso_prod);
            btn_eliminar=itemView.findViewById(R.id.eliminar_prod);

        }
    }
    private void eliminar_prod(final String id_producto,final int id_empresa, final DefaultCallback callback) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "http://192.168.1.5/app_gestion/deleteIN.php",
                response -> {
                    // La solicitud se ha realizado con éxito, llama al método onSuccess del callback con true
                    callback.onSuccess(true);
                },
                volleyError -> {
                    // Ha ocurrido un error en la solicitud, llama al método onFailure del callback con false y el mensaje de error
                    callback.onFailure("Error al enviar la solicitud: " + volleyError.getMessage());
                }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id_producto); // Agregar el ID del gasto personal a editar
                params.put("id_empresa", String.valueOf(id_empresa));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    public interface DefaultCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
}