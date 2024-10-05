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
import com.example.appgestion.ui.encuesta.ClsEncuesta;
import com.example.appgestion.ui.inventario.AgregarManoObra;
import com.example.appgestion.ui.inventario.ClsManoObra;

import java.util.List;

public class EncuestaAdapter extends RecyclerView.Adapter<EncuestaAdapter.ViewHolder> {
    private Context mContext;
    private List<ClsEncuesta> encuestaList;

    public EncuestaAdapter(Context context, List<ClsEncuesta> encuestaList) {
        this.mContext = context;
        this.encuestaList = encuestaList;

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
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encuesta, parent, false);
        return new ViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // TODO: Put ViewHolder binding code here in MDC-102
        if (encuestaList != null && position < encuestaList.size()) {
            ClsEncuesta preguntas = encuestaList.get(position);
            holder.pregunta_encuesta.setText(preguntas.getPregunta());
            //imageRequester.setImageFromUrl(holder.productImage, product.url);



        }
    }

    @Override
    public int getItemCount() {
        return encuestaList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id_in,pregunta_encuesta;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            pregunta_encuesta=itemView.findViewById(R.id.pregunta_encuesta);


        }
    }

}