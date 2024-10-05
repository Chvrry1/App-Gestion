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
import com.example.appgestion.ui.gastos.AgregarCostos;
import com.example.appgestion.ui.gastos.AgregarGastos;
import com.example.appgestion.ui.gastos.ClsCostosIndirectos;

import java.util.List;

public class CostosAdapter extends RecyclerView.Adapter<CostosAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsCostosIndirectos> CostosList;
    private int id_empresa;

    public CostosAdapter(Context context, List<ClsCostosIndirectos> costosList, int id_empresa) {
        this.mContext = context;
        this.CostosList = costosList;
        this.id_empresa = id_empresa;

    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ClsCostosIndirectos item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_costo_indirecto, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if (CostosList != null && position < CostosList.size()) {
            ClsCostosIndirectos ci = CostosList.get(position);
            holder.text_descripcion.setText(ci.nombre_concepto);
            holder.text_importe.setText(String.valueOf(ci.importe));
            holder.text_id.setText(ci.id);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getLayoutPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ClsCostosIndirectos costosIndirectos = CostosList.get(adapterPosition);
                        Intent formCostos = new Intent(mContext, AgregarCostos.class);
                        formCostos.putExtra("id", costosIndirectos.id);
                        formCostos.putExtra("nombre", costosIndirectos.nombre_concepto);
                        formCostos.putExtra("importe", costosIndirectos.importe);
                        formCostos.putExtra("id_empresa",id_empresa);
                        mContext.startActivity(formCostos);
                    }
                }
            });
            //imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }

    @Override
    public int getItemCount() {
        return CostosList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_descripcion,text_importe,text_id;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            text_id=itemView.findViewById(R.id.txt_id_ci);
            text_importe=itemView.findViewById(R.id.text_importe_mensual_CI);
            text_descripcion=itemView.findViewById(R.id.text_nombre_concepto_CI);

        }
    }
}