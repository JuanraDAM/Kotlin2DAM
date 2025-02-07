package com.example.srodenas.example_with_catalogs.ui.views.fragments.alerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.srodenas.example_with_catalogs.databinding.FragmentAlertsBinding
import com.example.srodenas.example_with_catalogs.databinding.FragmentDetailsAlertBinding
import com.example.srodenas.example_with_catalogs.domain.alerts.models.ListAlerts
import com.example.srodenas.example_with_catalogs.ui.viewmodel.alerts.DetailsAlertViewModel

class DetailsAlertFragment : Fragment() {

    private var _binding: FragmentDetailsAlertBinding? = null
    private val binding get() = _binding!!

    // Supongamos que recibes el argumento 'num' (la posición de la alerta en la lista)
    private val args: DetailsAlertFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Supón que obtienes la alerta a partir de la posición usando el ViewModel o una clase de caché
        val pos = args.num
        val alert = /* Obtén la alerta, por ejemplo: */ ListAlerts.list.alerts.get(pos)

        // Asignar los valores a la UI
        binding.txtAlertTitle.text = alert.textShort
        binding.txtAlertMessage.text = alert.message
        binding.txtAlertDate.text = "Fecha: ${alert.alertDate}" // Puedes formatear la fecha adecuadamente

        // Botón para cerrar el fragmento y volver
        binding.btnCloseDetails.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
