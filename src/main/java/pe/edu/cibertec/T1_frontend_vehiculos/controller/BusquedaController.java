package pe.edu.cibertec.T1_frontend_vehiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.T1_frontend_vehiculos.dto.BusquedaRequestDTO;
import pe.edu.cibertec.T1_frontend_vehiculos.dto.BusquedaResponseDTO;
import pe.edu.cibertec.T1_frontend_vehiculos.viewmodel.BusquedaModel;

@Controller
@RequestMapping("/busqueda")
public class BusquedaController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        BusquedaModel busquedaModel = new BusquedaModel("00", "", "", "", "", "", "");
        model.addAttribute("busquedaModel", busquedaModel);
        return "inicio";
    }

    @PostMapping("/buscar")
    public String buscar(@RequestParam("placa") String placa, Model model){

        //validar campo
        if (placa == null || placa.trim().length() == 0 || placa.trim().length() >= 8) {
            BusquedaModel busquedaModel = new BusquedaModel("01", "Error: Debe completar correctamente la placa", "", "", "", "", "");
            model.addAttribute("busquedaModel", busquedaModel);
            return "inicio";
        }

        try {
            //invocar servicio de busqueda
            String endpoint = "http://localhost:8080/inicio/buscar";
            BusquedaRequestDTO busquedaRequestDTO = new BusquedaRequestDTO(placa);
            BusquedaResponseDTO busquedaResponseDTO = restTemplate.postForObject(endpoint, busquedaRequestDTO, BusquedaResponseDTO.class);

            if (busquedaResponseDTO.codigo().equals("00")) {
                BusquedaModel busquedaModel = new BusquedaModel("00", "" ,busquedaResponseDTO.marca(), busquedaResponseDTO.modelo()
                        , busquedaResponseDTO.numeroAsientos(), busquedaResponseDTO.precio(), busquedaResponseDTO.color());
                model.addAttribute("busquedaModel", busquedaModel);
                return "principal";
            } else {
                BusquedaModel busquedaModel = new BusquedaModel("01", "Error: No se encontraron resultados", "", "", "", "", "");
                model.addAttribute("busquedaModel", busquedaModel);
                return "inicio";
            }




        }catch (Exception e){
            BusquedaModel busquedaModel = new BusquedaModel("01", "Error: No se encontraron resultados", "", "", "", "", "");
            model.addAttribute("busquedaModel", busquedaModel);
            return "inicio";
        }


    }




}
