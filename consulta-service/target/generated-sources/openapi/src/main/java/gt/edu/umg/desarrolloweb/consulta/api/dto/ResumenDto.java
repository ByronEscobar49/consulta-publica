package gt.edu.umg.desarrolloweb.consulta.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ResumenDto
 */

@JsonTypeName("Resumen")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-14T01:11:32.116280200-06:00[America/Guatemala]", comments = "Generator version: 7.12.0")
public class ResumenDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private BigDecimal saldoPendiente;

  private Integer boletasPendientes;

  private Integer boletasVencidas;

  private @Nullable String ultimoPeriodo;

  public ResumenDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResumenDto(BigDecimal saldoPendiente, Integer boletasPendientes, Integer boletasVencidas) {
    this.saldoPendiente = saldoPendiente;
    this.boletasPendientes = boletasPendientes;
    this.boletasVencidas = boletasVencidas;
  }

  public ResumenDto saldoPendiente(BigDecimal saldoPendiente) {
    this.saldoPendiente = saldoPendiente;
    return this;
  }

  /**
   * Suma de saldos de boletas no anuladas (PENDIENTE y VENCIDA), en quetzales
   * @return saldoPendiente
   */
  @NotNull @Valid 
  @Schema(name = "saldoPendiente", example = "115.0", description = "Suma de saldos de boletas no anuladas (PENDIENTE y VENCIDA), en quetzales", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("saldoPendiente")
  public BigDecimal getSaldoPendiente() {
    return saldoPendiente;
  }

  public void setSaldoPendiente(BigDecimal saldoPendiente) {
    this.saldoPendiente = saldoPendiente;
  }

  public ResumenDto boletasPendientes(Integer boletasPendientes) {
    this.boletasPendientes = boletasPendientes;
    return this;
  }

  /**
   * Cantidad de boletas PENDIENTE (aun no vencen)
   * @return boletasPendientes
   */
  @NotNull 
  @Schema(name = "boletasPendientes", example = "1", description = "Cantidad de boletas PENDIENTE (aun no vencen)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("boletasPendientes")
  public Integer getBoletasPendientes() {
    return boletasPendientes;
  }

  public void setBoletasPendientes(Integer boletasPendientes) {
    this.boletasPendientes = boletasPendientes;
  }

  public ResumenDto boletasVencidas(Integer boletasVencidas) {
    this.boletasVencidas = boletasVencidas;
    return this;
  }

  /**
   * Cantidad de boletas VENCIDA
   * @return boletasVencidas
   */
  @NotNull 
  @Schema(name = "boletasVencidas", example = "1", description = "Cantidad de boletas VENCIDA", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("boletasVencidas")
  public Integer getBoletasVencidas() {
    return boletasVencidas;
  }

  public void setBoletasVencidas(Integer boletasVencidas) {
    this.boletasVencidas = boletasVencidas;
  }

  public ResumenDto ultimoPeriodo(String ultimoPeriodo) {
    this.ultimoPeriodo = ultimoPeriodo;
    return this;
  }

  /**
   * Ultimo periodo con boleta, formato AAAA-MM. Ausente si no hay boletas.
   * @return ultimoPeriodo
   */
  
  @Schema(name = "ultimoPeriodo", example = "2026-09", description = "Ultimo periodo con boleta, formato AAAA-MM. Ausente si no hay boletas.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ultimoPeriodo")
  public String getUltimoPeriodo() {
    return ultimoPeriodo;
  }

  public void setUltimoPeriodo(String ultimoPeriodo) {
    this.ultimoPeriodo = ultimoPeriodo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResumenDto resumen = (ResumenDto) o;
    return Objects.equals(this.saldoPendiente, resumen.saldoPendiente) &&
        Objects.equals(this.boletasPendientes, resumen.boletasPendientes) &&
        Objects.equals(this.boletasVencidas, resumen.boletasVencidas) &&
        Objects.equals(this.ultimoPeriodo, resumen.ultimoPeriodo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(saldoPendiente, boletasPendientes, boletasVencidas, ultimoPeriodo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResumenDto {\n");
    sb.append("    saldoPendiente: ").append(toIndentedString(saldoPendiente)).append("\n");
    sb.append("    boletasPendientes: ").append(toIndentedString(boletasPendientes)).append("\n");
    sb.append("    boletasVencidas: ").append(toIndentedString(boletasVencidas)).append("\n");
    sb.append("    ultimoPeriodo: ").append(toIndentedString(ultimoPeriodo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

