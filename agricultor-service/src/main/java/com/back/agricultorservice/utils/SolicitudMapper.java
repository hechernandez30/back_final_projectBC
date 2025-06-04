package com.back.agricultorservice.utils;

import com.back.agricultorservice.dto.SolicitudRequest;
import com.spring.security.jwt.dto.CuentaRequestDto;
import com.spring.security.jwt.model.CuentaModel;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class SolicitudMapper {
    public static CuentaRequestDto convertirASolicitudBeneficio(SolicitudRequest solicitud) {
        CuentaRequestDto cuenta = new CuentaRequestDto();
        cuenta.setNumeroCuenta(solicitud.getNumeroCuenta());
        cuenta.setNitAgricultor(solicitud.getNitAgricultor());
        cuenta.setPesoAcordado(solicitud.getPesoAcordado());
        cuenta.setCantParcialidades(solicitud.getCantParcialidades());
        cuenta.setPesoCadaParcialidad(solicitud.getPesoCadaParcialidad());
        cuenta.setMedidaPeso(obtenerTextoMedida(solicitud.getMedidaPesoId())); // conversión aquí
        cuenta.setEstadoCuentaId(1); // Por defecto, por ejemplo "Cuenta Creada"
        cuenta.setObservaciones(solicitud.getObservaciones());
        return cuenta;
    }

    private static String obtenerTextoMedida(int id) {
        return switch (id) {
            case 1 -> "Tonelada";
            case 2 -> "Quintal";
            case 3 -> "Kilogramo";
            case 4 -> "Libra";
            case 5 -> "Miligramo";
            default -> "Desconocido";
        };
    }

    public static void notificarCambioEstadoSolicitud(RestTemplate restTemplate, CuentaModel cuenta) {
        String url = "http://localhost:3001/api/solicitudes/actualizar-estado/" + cuenta.getNumeroCuenta();

        Map<String, String> body = new HashMap<>();
        body.put("nuevoEstado", cuenta.getEstadoCuenta().getNombreEstado());

        restTemplate.put(url, body);
    }

}
