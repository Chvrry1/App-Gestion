package adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appgestion.ui.activos.ADiferidosFragment;
import com.example.appgestion.ui.activos.AFijosFragment;
import com.example.appgestion.ui.inventario.ManoObraFragment;
import com.example.appgestion.ui.inventario.PuntoEquilibrioFragment;
import com.example.appgestion.ui.inventario.VistaInventarioFragment;

public class AdaptadorTabsInventario extends FragmentStateAdapter {



    public AdaptadorTabsInventario(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager.getPrimaryNavigationFragment());
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new VistaInventarioFragment();
            case 1:
                return new ManoObraFragment();
            case 2:
                return new PuntoEquilibrioFragment();
            default:
                return new VistaInventarioFragment();
        }
    }

    @Override
    public int getItemCount() {

        return 3;
    }
}
