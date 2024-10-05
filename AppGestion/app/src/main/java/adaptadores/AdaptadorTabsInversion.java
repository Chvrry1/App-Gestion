package adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appgestion.ui.activos.ADiferidosFragment;
import com.example.appgestion.ui.activos.AFijosFragment;
import com.example.appgestion.ui.inversion.FlujoInversionFragment;
import com.example.appgestion.ui.inversion.InversionInicialFragment;

public class AdaptadorTabsInversion extends FragmentStateAdapter {



    public AdaptadorTabsInversion(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager.getPrimaryNavigationFragment());
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InversionInicialFragment();
            case 1:
                return new FlujoInversionFragment();
            default:
                return new InversionInicialFragment();
        }
    }

    @Override
    public int getItemCount() {

        return 2;
    }
}
