package gt.edu.umg.desarrolloweb.consulta.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * BoletaDto
 */

@JsonTypeName("Boleta")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-14T01:11:32.116280200-06:00[America/Guatemala]", comments = "Generator version: 7.12.0")
public class BoletaDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String folio;

  private String periodo;

  private BigDecimal consumoM3;

  private BigDecimal monto;

  private BigDecimal montoPagado;

  private BigDecimal saldo;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaEmision;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaVencimiento;

  /**
   * Gets or Sets estado
   */
  public enum EstadoEnum {
    PENDIENTE("PENDIENTE"),
    
    VENCIDA("VENCIDA"),
    
    PAGADA("PAGADA"),
    
    ANULADA("ANULADA");

    private String value;

    EstadoEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static EstadoEnum fromValue(String value) {
      for (EstadoEnum b : EstadoEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EstadoEnum estado;

  public BoletaDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BoletaDto(String folio, String periodo, BigDecimal consumoM3, BigDecimal monto, BigDecimal montoPagado, BigDecimal saldo, LocalDate fechaEmision, LocalDate fechaVencimiento, EstadoEnum estado) {
    this.folio = folio;
    this.periodo = periodo;
    this.consumoM3 = consumoM3;
    this.monto = monto;
    this.montoPagado = montoPagado;
    this.saldo = saldo;
    this.fechaEmision = fechaEmision;
    this.fechaVencimiento = fechaVencimiento;
    this.estado = estado;
  }

  public BoletaDto folio(String folio) {
    this.folio = folio;
    return this;
  }

  /**
   * Get folio
   * @return folio
   */
  @NotNull 
  @Schema(name = "folio", example = "B-2026-000011", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("folio")
  public String getFolio() {
    return folio;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public BoletaDto periodo(String periodo) {
    this.periodo = periodo;
    return this;
  }

  /**
   * Formato AAAA-MM
   * @return periodo
   */
  @NotNull 
  @Schema(name = "periodo", example = "2026-09", description = "Formato AAAA-MM", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("periodo")
  public String getPeriodo() {
    return periodo;
  }

  public void setPeriodo(String periodo) {
    this.periodo = periodo;
  }

  public BoletaDto consumoM3(BigDecimal consumoM3) {
    this.consumoM3 = consumoM3;
    return this;
  }

  /**
   * Get consumoM3
   * @return consumoM3
   */
  @NotNull @Valid 
  @Schema(name = "consumoM3", example = "21.0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("consumoM3")
  public BigDecimal getConsumoM3() {
    return consumoM3;
  }

  public void setConsumoM3(BigDecimal consumoM3) {
    this.consumoM3 = consumoM3;
  }

  public BoletaDto monto(BigDecimal monto) {
    this.monto = monto;
    return this;
  }

  /**
   * Total de la boleta en quetzales
   * @return monto
   */
  @NotNull @Valid 
  @Schema(name = "monto", example = "66.0", description = "Total de la boleta en quetzales", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("monto")
  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public BoletaDto montoPagado(BigDecimal montoPagado) {
    this.montoPagado = montoPagado;
    return this;
  }

  /**
   * Suma de pagos no revertidos
   * @return montoPagado
   */
  @NotNull @Valid 
  @Schema(name = "montoPagado", example = "66.0", description = "Suma de pagos no revertidos", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("montoPagado")
  public BigDecimal getMontoPagado() {
    return montoPagado;
  }

  public void setMontoPagado(BigDecimal montoPagado) {
    this.montoPagado = montoPagado;
  }

  public BoletaDto saldo(BigDecimal saldo) {
    this.saldo = saldo;
    return this;
  }

  /**
   * Lo que aun se debe (0 si esta pagada o anulada)
   * @return saldo
   */
  @NotNull @Valid 
  @Schema(name = "saldo", example = "0.0", description = "Lo que aun se debe (0 si esta pagada o anulada)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("saldo")
  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

  public BoletaDto fechaEmision(LocalDate fechaEmision) {
    this.fechaEmision = fechaEmision;
    return this;
  }

  /**
   * Get fechaEmision
   * @return fechaEmision
   */
  @NotNull @Valid 
  @Schema(name = "fechaEmision", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fechaEmision")
  public LocalDate getFechaEmision() {
    return fechaEmision;
  }

  public void setFechaEmision(LocalDate fechaEmision) {
    this.fechaEmision = fechaEmision;
  }

  public BoletaDto fechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
    return this;
  }

  /**
   * Get fechaVencimiento
   * @return fechaVencimiento
   */
  @NotNull @Valid 
  @Schema(name = "fechaVencimiento", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fechaVencimiento")
  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public BoletaDto estado(EstadoEnum estado) {
    this.estado = estado;
    return this;
  }

  /**
   * Get estado
   * @return estado
   */
  @NotNull 
  @Schema(name = "estado", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("estado")
  public EstadoEnum getEstado() {
    return estado;
  }

  public void setEstado(EstadoEnum estado) {
    this.estado = estado;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoletaDto boleta = (BoletaDto) o;
    return Objects.equals(this.folio, boleta.folio) &&
        Objects.equals(this.periodo, boleta.periodo) &&
        Objects.equals(this.consumoM3, boleta.consumoM3) &&
        Objects.equals(this.monto, boleta.monto) &&
        Objects.equals(this.montoPagado, boleta.montoPagado) &&
        Objects.equals(this.saldo, boleta.saldo) &&
        Objects.equals(this.fechaEmision, boleta.fechaEmision) &&
        Objects.equals(this.fechaVencimiento, boleta.fechaVencimiento) &&
        Objects.equals(this.estado, boleta.estado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(folio, periodo, consumoM3, monto, montoPagado, saldo, fechaEmision, fechaVencimiento, estado);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoletaDto {\n");
    sb.append("    folio: ").append(toIndentedString(folio)).append("\n");
    sb.append("    periodo: ").append(toIndentedString(periodo)).append("\n");
    sb.append("    consumoM3: ").append(toIndentedString(consumoM3)).append("\n");
    sb.append("    monto: ").append(toIndentedString(monto)).append("\n");
    sb.append("    montoPagado: ").append(toIndentedString(montoPagado)).append("\n");
    sb.append("    saldo: ").append(toIndentedString(saldo)).append("\n");
    sb.append("    fechaEmision: ").append(toIndentedString(fechaEmision)).append("\n");
    sb.append("    fechaVencimiento: ").append(toIndentedString(fechaVencimiento)).append("\n");
    sb.append("    estado: ").append(toIndentedString(estado)).append("\n");
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

