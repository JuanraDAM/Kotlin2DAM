package com.example.srodenas.example_with_catalogs.ui.views.fragments.alerts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srodenas.example_with_catalogs.databinding.FragmentAlertsBinding
import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert
import com.example.srodenas.example_with_catalogs.ui.viewmodel.alerts.AlertsViewModel
import com.example.srodenas.example_with_catalogs.ui.views.fragments.alerts.adapter.AdapterAlerts
import java.time.LocalDate

class AlertsFragment : Fragment() {
    private var binding: FragmentAlertsBinding? = null
    private val viewModelAlerts: AlertsViewModel by viewModels()
    private lateinit var layoutManager: LinearLayoutManager  // Gestor para el RecyclerView
    private lateinit var adapterAlerts: AdapterAlerts       // Adaptador de alertas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos el adaptador con una lista vacía
        adapterAlerts = AdapterAlerts(
            mutableListOf(),
            deleteAction = { pos -> deleteAlertForDialog(pos) },
            detailsAction = { pos -> detailsAlert(pos) }
        )
    }

    // Método para gestionar el borrado (se implementará según tus necesidades)
    private fun deleteAlertForDialog(pos: Int) {
        // Llamamos directamente a la función del ViewModel para eliminar la alerta en la posición indicada
        viewModelAlerts.delAlert(pos)
    }


    // Método para navegar a los detalles de la alerta seleccionada
    private fun detailsAlert(pos: Int) {
        val navController = findNavController()
        navController.navigate(AlertsFragmentDirections.actionAlertsFragmentToDetailsAlertFragment(num = pos))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configuramos el RecyclerView
        binding?.myRecyclerViewAlerts?.layoutManager = LinearLayoutManager(activity)
        layoutManager = binding?.myRecyclerViewAlerts?.layoutManager as LinearLayoutManager
        binding?.myRecyclerViewAlerts?.adapter = adapterAlerts
        Log.d("AlertsFragment", "Adapter asignado: ${binding?.myRecyclerViewAlerts?.adapter}")

        // Configuramos el clic del botón para añadir alerta
        binding?.btnAdd?.setOnClickListener {
            addAlert()
        }
        setObserverChangeViewModel()
    }

    // Método que crea y añade una alerta nueva
    private fun addAlert() {
        Log.d("AlertsFragment", "Botón btnAdd pulsado")
        val newAlert = Alert(
            id = 0,
            userId = 1, // Idealmente usar el ID del usuario logueado, ej. Profile.profile.user.id
            textShort = "Nueva Alerta",
            message = "Este es un mensaje de alerta",
            alertDate = LocalDate.now()
        )
        viewModelAlerts.addAlerts(newAlert)
    }

    // Observa los cambios en el ViewModel para actualizar el adaptador
    private fun setObserverChangeViewModel() {
        viewModelAlerts.listAlertsLiveData.observe(viewLifecycleOwner) { listAlert ->
            adapterAlerts.updateData(listAlert)
        }
        viewModelAlerts.posNewAlertLiveDate.observe(viewLifecycleOwner) { posNewAlert ->
            adapterAlerts.notifyItemInserted(posNewAlert)
            layoutManager.scrollToPositionWithOffset(posNewAlert, 20)
        }
        viewModelAlerts.posDeleteAlertLiveData.observe(viewLifecycleOwner) { posDeleteAlert ->
            adapterAlerts.notifyItemRemoved(posDeleteAlert)
            layoutManager.scrollToPositionWithOffset(posDeleteAlert, 20)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
