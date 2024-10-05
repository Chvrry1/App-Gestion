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
import com.example.appgestion.ui.activos.AgregarActivosFijos;
import com.example.appgestion.ui.activos.ClsActivoFijo;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;
import com.example.appgestion.ui.gastos.VolleyCallback;
import com.example.appgestion.ui.inventario.AgregarProductos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivosFijosAdapter extends RecyclerView.Adapter<ActivosFijosAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsActivoFijo> activoFijoList;
    private int id_empresa;

    public ActivosFijosAdapter(Context context, List<ClsActivoFijo> activoFijoList, int id_empresa) {
        this.mContext = context;
        this.activoFijoList = activoFijoList;
        this.id_empresa=id_empresa;

    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ClsActivoFijo item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activo_fijo, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if (activoFijoList != null && position < activoFijoList.size()) {
            ClsActivoFijo af = activoFijoList.get(position);
            holder.text_descripcion_af.setText(af.descripcion);
            holder.text_unitario_af.setText(String.valueOf(af.valor_unitario));
            holder.txt_id_af.setText(af.id);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getLayoutPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ClsActivoFijo activoFijo = activoFijoList.get(adapterPosition);
                        Intent formActivo = new Intent(mContext, AgregarActivosFijos.class);
                        formActivo.putExtra("id", activoFijo.id);
                        formActivo.putExtra("nombre", activoFijo.descripcion);
                        formActivo.putExtra("unidades", activoFijo.unidades);
                        formActivo.putExtra("valor_unitario", activoFijo.valor_unitario);
                        formActivo.putExtra("vida_util", activoFijo.vida_util);
                        formActivo.putExtra("id_empresa",id_empresa);
                        mContext.startActivity(formActivo);
                    }
                    System.out.println("funciona item");
                }
            });

            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                int adapterPosition = holder.getLayoutPosition();
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                    alertDialogBuilder.setTitle("Eliminar Activo Fijo "+af.descripcion);

                    alertDialogBuilder
                            .setMessage("¿Esta seguro de eliminar este activo?")
                            .setCancelable(false)
                            .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    delete_activo_fijo(af.id,id_empresa,new VolleyCallback(){
                                        @Override
                                        public void onSuccess(boolean success) {
                                            Toast.makeText(mContext, "Activo Fijo Eliminado", Toast.LENGTH_SHORT).show();
                                            activoFijoList.remove(adapterPosition);
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
            //imageRequester.setImageFromUrl(holder.productImage, product.url);


        }


    }

    @Override
    public int getItemCount() {
        return activoFijoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_af,text_descripcion_af,text_unitario_af;
        ImageView eliminar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_id_af=itemView.findViewById(R.id.txt_id_af);
            text_unitario_af=itemView.findViewById(R.id.text_valor_unitario);
            text_descripcion_af=itemView.findViewById(R.id.text_nombre_activof);
            eliminar = itemView.findViewById(R.id.eliminar);

        }
    }

    private void delete_activo_fijo(final String id, final int id_empresa,final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://192.168.1.5/app_gestion/deleteAF.php", response -> {
            callback.onSuccess(true);

        },volleyError -> {
            callback.onFailure("error al enviar la solicitud" + volleyError.getMessage());
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> datos=new HashMap<>();
                datos.put("id",id);
                datos.put("id_empresa", String.valueOf(id_empresa));
                return datos;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);


    }
    public interface VolleyCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }
    

}