package gt.edu.umg.desarrolloweb.consulta.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PagoDto
 */

@JsonTypeName("Pago")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-14T01:11:32.116280200-06:00[America/Guatemala]", comments = "Generator version: 7.12.0")
public class PagoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String folio;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaPago;

  private BigDecimal monto;

  private String metodo;

  private String folioBoleta;

  public PagoDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PagoDto(String folio, LocalDate fechaPago, BigDecimal monto, String metodo, String folioBoleta) {
    this.folio = folio;
    this.fechaPago = fechaPago;
    this.monto = monto;
    this.metodo = metodo;
    this.folioBoleta = folioBoleta;
  }

  public PagoDto folio(String folio) {
    this.folio = folio;
    return this;
  }

  /**
   * Get folio
   * @return folio
   */
  @NotNull 
  @Schema(name = "folio", example = "R-2026-000010", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("folio")
  public String getFolio() {
    return folio;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public PagoDto fechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
    return this;
  }

  /**
   * Get fechaPago
   * @return fechaPago
   */
  @NotNull @Valid 
  @Schema(name = "fechaPago", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fechaPago")
  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public PagoDto monto(BigDecimal monto) {
    this.monto = monto;
    return this;
  }

  /**
   * Get monto
   * @return monto
   */
  @NotNull @Valid 
  @Schema(name = "monto", example = "66.0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("monto")
  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public PagoDto metodo(String metodo) {
    this.metodo = metodo;
    return this;
  }

  /**
   * Nombre del metodo de pago
   * @return metodo
   */
  @NotNull 
  @Schema(name = "metodo", example = "Efectivo", description = "Nombre del metodo de pago", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metodo")
  public String getMetodo() {
    return metodo;
  }

  public void setMetodo(String metodo) {
    this.metodo = metodo;
  }

  public PagoDto folioBoleta(String folioBoleta) {
    this.folioBoleta = folioBoleta;
    return this;
  }

  /**
   * Boleta a la que se aplico el pago
   * @return folioBoleta
   */
  @NotNull 
  @Schema(name = "folioBoleta", example = "B-2026-000011", description = "Boleta a la que se aplico el pago", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("folioBoleta")
  public String getFolioBoleta() {
    return folioBoleta;
  }

  public void setFolioBoleta(String folioBoleta) {
    this.folioBoleta = folioBoleta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagoDto pago = (PagoDto) o;
    return Objects.equals(this.folio, pago.folio) &&
        Objects.equals(this.fechaPago, pago.fechaPago) &&
        Objects.equals(this.monto, pago.monto) &&
        Objects.equals(this.metodo, pago.metodo) &&
        Objects.equals(this.folioBoleta, pago.folioBoleta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(folio, fechaPago, monto, metodo, folioBoleta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagoDto {\n");
    sb.append("    folio: ").append(toIndentedString(folio)).append("\n");
    sb.append("    fechaPago: ").append(toIndentedString(fechaPago)).append("\n");
    sb.append("    monto: ").append(toIndentedString(monto)).append("\n");
    sb.append("    metodo: ").append(toIndentedString(metodo)).append("\n");
    sb.append("    folioBoleta: ").append(toIndentedString(folioBoleta)).append("\n");
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

