package gt.edu.umg.desarrolloweb.consulta.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import gt.edu.umg.desarrolloweb.consulta.api.dto.BoletaDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.ContadorDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.PagoDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.ResumenDto;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * EstadoDeCuentaDto
 */

@JsonTypeName("EstadoDeCuenta")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-14T01:11:32.116280200-06:00[America/Guatemala]", comments = "Generator version: 7.12.0")
public class EstadoDeCuentaDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private ContadorDto contador;

  private String titular;

  private ResumenDto resumen;

  @Valid
  private List<@Valid BoletaDto> boletas = new ArrayList<>();

  @Valid
  private List<@Valid PagoDto> pagos = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime consultadoEn;

  public EstadoDeCuentaDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EstadoDeCuentaDto(ContadorDto contador, String titular, ResumenDto resumen, List<@Valid BoletaDto> boletas, List<@Valid PagoDto> pagos, OffsetDateTime consultadoEn) {
    this.contador = contador;
    this.titular = titular;
    this.resumen = resumen;
    this.boletas = boletas;
    this.pagos = pagos;
    this.consultadoEn = consultadoEn;
  }

  public EstadoDeCuentaDto contador(ContadorDto contador) {
    this.contador = contador;
    return this;
  }

  /**
   * Get contador
   * @return contador
   */
  @NotNull @Valid 
  @Schema(name = "contador", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contador")
  public ContadorDto getContador() {
    return contador;
  }

  public void setContador(ContadorDto contador) {
    this.contador = contador;
  }

  public EstadoDeCuentaDto titular(String titular) {
    this.titular = titular;
    return this;
  }

  /**
   * Nombre abreviado del titular (primer nombre completo, resto en iniciales)
   * @return titular
   */
  @NotNull 
  @Schema(name = "titular", example = "María F. L. C.", description = "Nombre abreviado del titular (primer nombre completo, resto en iniciales)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("titular")
  public String getTitular() {
    return titular;
  }

  public void setTitular(String titular) {
    this.titular = titular;
  }

  public EstadoDeCuentaDto resumen(ResumenDto resumen) {
    this.resumen = resumen;
    return this;
  }

  /**
   * Get resumen
   * @return resumen
   */
  @NotNull @Valid 
  @Schema(name = "resumen", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resumen")
  public ResumenDto getResumen() {
    return resumen;
  }

  public void setResumen(ResumenDto resumen) {
    this.resumen = resumen;
  }

  public EstadoDeCuentaDto boletas(List<@Valid BoletaDto> boletas) {
    this.boletas = boletas;
    return this;
  }

  public EstadoDeCuentaDto addBoletasItem(BoletaDto boletasItem) {
    if (this.boletas == null) {
      this.boletas = new ArrayList<>();
    }
    this.boletas.add(boletasItem);
    return this;
  }

  /**
   * Ultimas boletas, de la mas reciente a la mas antigua (maximo 12)
   * @return boletas
   */
  @NotNull @Valid @Size(max = 12) 
  @Schema(name = "boletas", description = "Ultimas boletas, de la mas reciente a la mas antigua (maximo 12)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("boletas")
  public List<@Valid BoletaDto> getBoletas() {
    return boletas;
  }

  public void setBoletas(List<@Valid BoletaDto> boletas) {
    this.boletas = boletas;
  }

  public EstadoDeCuentaDto pagos(List<@Valid PagoDto> pagos) {
    this.pagos = pagos;
    return this;
  }

  public EstadoDeCuentaDto addPagosItem(PagoDto pagosItem) {
    if (this.pagos == null) {
      this.pagos = new ArrayList<>();
    }
    this.pagos.add(pagosItem);
    return this;
  }

  /**
   * Ultimos pagos no revertidos, del mas reciente al mas antiguo (maximo 12)
   * @return pagos
   */
  @NotNull @Valid @Size(max = 12) 
  @Schema(name = "pagos", description = "Ultimos pagos no revertidos, del mas reciente al mas antiguo (maximo 12)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pagos")
  public List<@Valid PagoDto> getPagos() {
    return pagos;
  }

  public void setPagos(List<@Valid PagoDto> pagos) {
    this.pagos = pagos;
  }

  public EstadoDeCuentaDto consultadoEn(OffsetDateTime consultadoEn) {
    this.consultadoEn = consultadoEn;
    return this;
  }

  /**
   * Momento de la consulta (UTC)
   * @return consultadoEn
   */
  @NotNull @Valid 
  @Schema(name = "consultadoEn", description = "Momento de la consulta (UTC)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("consultadoEn")
  public OffsetDateTime getConsultadoEn() {
    return consultadoEn;
  }

  public void setConsultadoEn(OffsetDateTime consultadoEn) {
    this.consultadoEn = consultadoEn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EstadoDeCuentaDto estadoDeCuenta = (EstadoDeCuentaDto) o;
    return Objects.equals(this.contador, estadoDeCuenta.contador) &&
        Objects.equals(this.titular, estadoDeCuenta.titular) &&
        Objects.equals(this.resumen, estadoDeCuenta.resumen) &&
        Objects.equals(this.boletas, estadoDeCuenta.boletas) &&
        Objects.equals(this.pagos, estadoDeCuenta.pagos) &&
        Objects.equals(this.consultadoEn, estadoDeCuenta.consultadoEn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contador, titular, resumen, boletas, pagos, consultadoEn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstadoDeCuentaDto {\n");
    sb.append("    contador: ").append(toIndentedString(contador)).append("\n");
    sb.append("    titular: ").append(toIndentedString(titular)).append("\n");
    sb.append("    resumen: ").append(toIndentedString(resumen)).append("\n");
    sb.append("    boletas: ").append(toIndentedString(boletas)).append("\n");
    sb.append("    pagos: ").append(toIndentedString(pagos)).append("\n");
    sb.append("    consultadoEn: ").append(toIndentedString(consultadoEn)).append("\n");
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

