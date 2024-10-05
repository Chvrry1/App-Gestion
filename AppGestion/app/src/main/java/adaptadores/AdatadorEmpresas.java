package adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgestion.MainActivity;
import com.example.appgestion.ui.empresa.CrearEmpresa;
import com.example.appgestion.ui.empresa.clsEmpresa;

import java.util.List;
import com.example.appgestion.R;

public class AdatadorEmpresas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_EMPRESA = 0;
    private static final int TYPE_ADD_BUTTON = 1;
    private Context mContext; // Campo para almacenar el contexto
    private List<clsEmpresa> items;

    public AdatadorEmpresas(Context context, List<clsEmpresa> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.isEmpty() || position == items.size()) {
            return TYPE_ADD_BUTTON;
        } else {
            return TYPE_EMPRESA;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_EMPRESA) {
            View view = inflater.inflate(R.layout.item_empresa, parent, false);
            return new EmpresaViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_add_button, parent, false);
            return new AddButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_EMPRESA) {
            clsEmpresa empresa = items.get(position);
            EmpresaViewHolder empresaViewHolder = (EmpresaViewHolder) holder;
            empresaViewHolder.tvNombre.setText(empresa.getNombre());
            empresaViewHolder.tvId.setText(String.valueOf(empresa.getCapital()));

            empresaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clsEmpresa empresa = items.get(holder.getAdapterPosition());
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("EmpresaPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("empresa_id", empresa.getId());
                    editor.putString("empresa_nombre", empresa.getNombre());
                    editor.putString("capital", String.valueOf(empresa.getCapital()));
                    editor.apply();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("empresa_id", empresa.getId());
                    mContext.startActivity(intent);
                }
            });
        } else {
            AddButtonViewHolder addButtonViewHolder = (AddButtonViewHolder) holder;
            addButtonViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CrearEmpresa.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (items.isEmpty()) {
            return 1; // Solo el botón de añadir
        }
        return items.size() + 1; // Elementos de empresa + botón de añadir
    }

    public static class EmpresaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombre;
        public TextView tvId;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.txt_nombreEmpresa);
            tvId = itemView.findViewById(R.id.txt_id_empresa);
        }
    }

    public static class AddButtonViewHolder extends RecyclerView.ViewHolder {
        public ImageView addButton;

        public AddButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.addButton);
        }
    }
}
