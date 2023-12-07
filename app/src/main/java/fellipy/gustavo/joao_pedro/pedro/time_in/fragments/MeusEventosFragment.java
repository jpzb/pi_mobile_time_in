package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.EditarPerfilActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.ListAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageDataComparator;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeusEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeusEventosFragment extends Fragment {

    private HomeViewModel hViewModel;
    public MeusEventosFragment() {
        // Required empty public constructor
    }

    public static MeusEventosFragment newInstance(){
        return new MeusEventosFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meus_eventos, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListAdapter listAdapterSubsEvents = new ListAdapter(new ImageDataComparator(),
                (HomeActivity) getActivity());
        ListAdapter listAdapterCreEvents = new ListAdapter(new ImageDataComparator(),
                (HomeActivity) getActivity());
        HomeViewModel homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        LiveData<PagingData<Evento>> subsEventsLiveData = homeViewModel.getEventsSubsLd();
        LiveData<PagingData<Evento>> creatEventsLiveData = homeViewModel.getEventsCreLd();

        ToggleButton tbtInscritos = (ToggleButton) view.findViewById(R.id.btnEventosInscritos);
        ToggleButton tbtCriados = (ToggleButton) view.findViewById(R.id.btnEventosCriados);

        tbtInscritos.setChecked(true);
        subsEventsLiveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapterSubsEvents.submitData(getViewLifecycleOwner().getLifecycle(),
                        eventoPagingData);
            }
        });

        creatEventsLiveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapterCreEvents.submitData(getViewLifecycleOwner().getLifecycle(),
                        eventoPagingData);
            }
        });
        RecyclerView rvMyEvents = (RecyclerView) view.findViewById(R.id.rvMeusEventos);

        if(tbtInscritos.isChecked()){
            rvMyEvents.setAdapter(listAdapterSubsEvents);
        }

        tbtInscritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tbtCriados.setChecked(false);
                tbtInscritos.setChecked(true);
                rvMyEvents.setAdapter(listAdapterSubsEvents);
            }
        });

        tbtCriados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tbtInscritos.setChecked(false);
                tbtCriados.setChecked(true);
                rvMyEvents.setAdapter(listAdapterCreEvents);
            }
        });

        rvMyEvents.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}