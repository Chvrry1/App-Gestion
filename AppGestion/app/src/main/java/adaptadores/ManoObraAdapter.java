package adaptadores;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.AgregarActivosFijos;
import com.example.appgestion.ui.activos.ClsActivoFijo;
import com.example.appgestion.ui.inventario.AgregarManoObra;
import com.example.appgestion.ui.inventario.ClsInventario;
import com.example.appgestion.ui.inventario.ClsManoObra;

import java.util.List;

public class ManoObraAdapter extends RecyclerView.Adapter<ManoObraAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsManoObra> manoObraList;
    private int id_empresa;

    public ManoObraAdapter(Context context, List<ClsManoObra> manoObraList,int id_empresa) {
        this.mContext = context;
        this.manoObraList = manoObraList;
        this.id_empresa=id_empresa;

    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ClsManoObra item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mano_obra, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // TODO: Put ViewHolder binding code here in MDC-102
        if (manoObraList != null && position < manoObraList.size()) {
            ClsManoObra manoObra = manoObraList.get(position);
            holder.descripcion_MO.setText(manoObra.nombre);
            holder.sueldo_mensual.setText(String.valueOf(manoObra.sueldo));
            //imageRequester.setImageFromUrl(holder.productImage, product.url);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getLayoutPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        ClsManoObra manoObra = manoObraList.get(adapterPosition);
                        Intent formManoO = new Intent(mContext, AgregarManoObra.class);
                        formManoO.putExtra("id", manoObra.id);
                        formManoO.putExtra("nombre", manoObra.nombre);
                        formManoO.putExtra("sueldo", manoObra.sueldo);
                        formManoO.putExtra("id_empresa",id_empresa);
                        mContext.startActivity(formManoO);
                    }
                    System.out.println("funciona item");
                }
            });
            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return manoObraList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_in,descripcion_MO,sueldo_mensual;
        ImageView eliminar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_id_in=itemView.findViewById(R.id.txt_id_mo);
            descripcion_MO=itemView.findViewById(R.id.mano_de_obra);
            sueldo_mensual=itemView.findViewById(R.id.sueldo_mensual);
            eliminar =itemView.findViewById(R.id.eliminar_MO);


        }
    }

}