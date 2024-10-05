package adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.AgregarActivosDiferidos;
import com.example.appgestion.ui.activos.AgregarActivosFijos;
import com.example.appgestion.ui.activos.ClsActivoDiferido;
import com.example.appgestion.ui.activos.ClsActivoFijo;

import java.util.List;

public class ActivosDiferidoAdapter extends RecyclerView.Adapter<ActivosDiferidoAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsActivoDiferido> activoDiferidoList;
    private int id_empresa;

    public ActivosDiferidoAdapter(Context context, List<ClsActivoDiferido> activoDiferidoList,int id_empresa) {
        this.mContext = context;
        this.activoDiferidoList = activoDiferidoList;
        this.id_empresa=id_empresa;

    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ClsActivoDiferido item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activo_diferido, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if (activoDiferidoList != null && position < activoDiferidoList.size()) {
            ClsActivoDiferido ad = activoDiferidoList.get(position);
            holder.text_descripcion_ad.setText(ad.descripcion);
            holder.text_valor_pago.setText(String.valueOf(ad.pago_anticipado));
            //imageRequester.setImageFromUrl(holder.productImage, product.url);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getLayoutPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ClsActivoDiferido activoDiferido = activoDiferidoList.get(adapterPosition);
                        Intent formActivo = new Intent(mContext, AgregarActivosDiferidos.class);
                        formActivo.putExtra("id", activoDiferido.id);
                        formActivo.putExtra("nombre", activoDiferido.descripcion);
                        formActivo.putExtra("pago_anticipado", activoDiferido.pago_anticipado);
                        formActivo.putExtra("vigencia", activoDiferido.vigencia);
                        formActivo.putExtra("id_empresa",id_empresa);
                        mContext.startActivity(formActivo);
                    }
                    System.out.println("funciona item");
                }
            });
            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("funciona eliminar activo fijo");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activoDiferidoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_ad,text_descripcion_ad,text_valor_pago;
        ImageView eliminar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_id_ad=itemView.findViewById(R.id.txt_id_ad);
            text_descripcion_ad=itemView.findViewById(R.id.text_nombre_activod);
            text_valor_pago=itemView.findViewById(R.id.text_valor_pago);
            eliminar=itemView.findViewById(R.id.eliminar);

        }
    }
}