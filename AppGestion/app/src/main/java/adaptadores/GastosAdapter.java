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

import java.util.List;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsGastoPersonal> GastosList;
    private int id_empresa;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ClsGastoPersonal item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }



    public  GastosAdapter(Context context,List<ClsGastoPersonal> gastosList,int id_empresa) {
        this.mContext = context;
        this.GastosList = gastosList;
        this.id_empresa = id_empresa;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gasto_personal, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if (GastosList != null && position < GastosList.size()) {
            ClsGastoPersonal gp = GastosList.get(position);
            holder.text_descripcion_GP.setText(gp.descripcion);
            holder.text_importeGP.setText(String.valueOf(gp.importe));
            holder.txt_id_gp.setText(gp.id);
            holder.text_tipo.setText(gp.tipo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getLayoutPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ClsGastoPersonal gastoSeleccionado = GastosList.get(adapterPosition);
                        Intent formGastos = new Intent(mContext, AgregarGastos.class);
                        formGastos.putExtra("id", gastoSeleccionado.id);
                        formGastos.putExtra("nombre", gastoSeleccionado.descripcion);
                        formGastos.putExtra("importe", gastoSeleccionado.importe);
                        formGastos.putExtra("tipo", gastoSeleccionado.tipo);
                        formGastos.putExtra("id_empresa",id_empresa);
                        mContext.startActivity(formGastos);
                    }
                }
            });
            //imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }

    @Override
    public int getItemCount() {
        return GastosList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_gp,text_descripcion_GP,text_importeGP,text_tipo;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_id_gp=itemView.findViewById(R.id.txt_id_gp);
            text_descripcion_GP=itemView.findViewById(R.id.text_descripcion_GP);
            text_importeGP=itemView.findViewById(R.id.text_importeGP);
            text_tipo=itemView.findViewById(R.id.txt_tipo);

        }
    }
}