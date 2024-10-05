package adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.R;
import com.example.appgestion.ui.gastos.AgregarGastos;
import com.example.appgestion.ui.gastos.ClsGastoPersonal;
import com.example.appgestion.ui.inventario.AgregarMateriaPrima;
import com.example.appgestion.ui.inventario.ClsInventario;
import com.example.appgestion.ui.inventario.ClsManoObra;
import com.example.appgestion.ui.inventario.ClsMateriaPrima;

import java.util.List;

public class MateriaPrimaAdapter extends RecyclerView.Adapter<MateriaPrimaAdapter.ViewHolder> {
    private Context mpContext;
    private List<ClsMateriaPrima> materiaPrimaList;

    private Integer id_producto;


    public MateriaPrimaAdapter(Context context, List<ClsMateriaPrima> materiaPrimaList,Integer id_producto) {
        this.mpContext = context;
        this.materiaPrimaList = materiaPrimaList;
        this.id_producto=id_producto;


    }
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(ClsMateriaPrima item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materia_prima, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // TODO: Put ViewHolder binding code here in MDC-102
        if (materiaPrimaList != null && position < materiaPrimaList.size()) {
            ClsMateriaPrima materiaP = materiaPrimaList.get(position);
            holder.descripcion_mp.setText(materiaP.nombre);
            holder.costo_mp.setText(String.valueOf(materiaP.costo));
            holder.consumo_mp.setText(String.valueOf(materiaP.costo*materiaP.pro_producto));
            try {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent formMP = new Intent(mpContext, AgregarMateriaPrima.class);
                        formMP.putExtra("index", holder.getAdapterPosition());
                        formMP.putExtra("nombre", materiaP.nombre);
                        formMP.putExtra("costo", materiaP.costo);
                        formMP.putExtra("unidad", materiaP.unidad);
                        formMP.putExtra("pro_producto", materiaP.pro_producto);
                        formMP.putExtra("id_producto",Integer.valueOf(id_producto));
                        formMP.putExtra("id",materiaP.id);
                        mpContext.startActivity(formMP);
                        /*if(id_producto==0){
                            Intent formMP = new Intent(mpContext, AgregarMateriaPrima.class);
                            formMP.putExtra("index", holder.getAdapterPosition());
                            formMP.putExtra("nombre", materiaP.nombre);
                            formMP.putExtra("costo", materiaP.costo);
                            formMP.putExtra("unidad", materiaP.unidad);
                            formMP.putExtra("pro_producto", materiaP.pro_producto);
                            mpContext.startActivity(formMP);

                        }else {
                            int adapterPosition = holder.getLayoutPosition();
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                ClsMateriaPrima materiaPrima = materiaPrimaList.get(adapterPosition);
                                Intent formMP = new Intent(mpContext, AgregarGastos.class);
                                formMP.putExtra("nombre", materiaPrima.nombre);
                                formMP.putExtra("costo", materiaPrima.costo);
                                formMP.putExtra("unidad",materiaPrima.unidad);
                                formMP.putExtra("pro_producto",materiaPrima.pro_producto);
                                formMP.putExtra("id_producto",id_producto);
                                formMP.putExtra("id",Integer.parseInt(materiaPrima.id));
                                mpContext.startActivity(formMP);
                            }
                        }*/


                    }




                });

            }catch (Exception e){

                System.out.println("error al ingresar "+e);
            }

            //imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }

    @Override
    public int getItemCount() {
        return materiaPrimaList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_mp,descripcion_mp,consumo_mp, costo_mp;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_id_mp=itemView.findViewById(R.id.txt_id_mp);
            descripcion_mp=itemView.findViewById(R.id.nombre_materia_prima);
            consumo_mp=itemView.findViewById(R.id.consumo);
            costo_mp = itemView.findViewById(R.id.costo);


        }
    }
}