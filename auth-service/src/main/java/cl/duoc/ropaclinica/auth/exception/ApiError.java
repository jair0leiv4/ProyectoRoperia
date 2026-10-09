package cl.duoc.ropaclinica.auth.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp, int status, String error, String mensaje, Map<String, String> detalles) {}
