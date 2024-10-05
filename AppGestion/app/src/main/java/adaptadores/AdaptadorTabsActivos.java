package adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appgestion.R;
import com.example.appgestion.ui.activos.ADiferidosFragment;
import com.example.appgestion.ui.activos.AFijosFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class AdaptadorTabsActivos extends FragmentStateAdapter {



    public AdaptadorTabsActivos(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager.getPrimaryNavigationFragment());
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:

                return new AFijosFragment();
            case 1:
                return new ADiferidosFragment();
            default:
                return new AFijosFragment();
        }
    }

    @Override
    public int getItemCount() {

        return 2;
    }
}
