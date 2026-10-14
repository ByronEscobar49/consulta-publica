package gt.edu.umg.desarrolloweb.consulta.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ContadorDto
 */

@JsonTypeName("Contador")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-14T01:11:32.116280200-06:00[America/Guatemala]", comments = "Generator version: 7.12.0")
public class ContadorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String codigo;

  /**
   * Gets or Sets estado
   */
  public enum EstadoEnum {
    ACTIVO("ACTIVO"),
    
    INACTIVO("INACTIVO"),
    
    DANADO("DANADO");

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

  private String paja;

  private String ubicacion;

  public ContadorDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ContadorDto(String codigo, EstadoEnum estado, String paja, String ubicacion) {
    this.codigo = codigo;
    this.estado = estado;
    this.paja = paja;
    this.ubicacion = ubicacion;
  }

  public ContadorDto codigo(String codigo) {
    this.codigo = codigo;
    return this;
  }

  /**
   * Get codigo
   * @return codigo
   */
  @NotNull 
  @Schema(name = "codigo", example = "CTR-0001", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("codigo")
  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public ContadorDto estado(EstadoEnum estado) {
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

  public ContadorDto paja(String paja) {
    this.paja = paja;
    return this;
  }

  /**
   * Caudal contratado
   * @return paja
   */
  @NotNull 
  @Schema(name = "paja", example = "1 paja", description = "Caudal contratado", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("paja")
  public String getPaja() {
    return paja;
  }

  public void setPaja(String paja) {
    this.paja = paja;
  }

  public ContadorDto ubicacion(String ubicacion) {
    this.ubicacion = ubicacion;
    return this;
  }

  /**
   * Ubicacion del servicio (sector, aldea, zona, calle). No es la direccion de notificacion del titular.
   * @return ubicacion
   */
  @NotNull 
  @Schema(name = "ubicacion", example = "Sector Centro, zona 1, 4a calle 5-20", description = "Ubicacion del servicio (sector, aldea, zona, calle). No es la direccion de notificacion del titular.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ubicacion")
  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContadorDto contador = (ContadorDto) o;
    return Objects.equals(this.codigo, contador.codigo) &&
        Objects.equals(this.estado, contador.estado) &&
        Objects.equals(this.paja, contador.paja) &&
        Objects.equals(this.ubicacion, contador.ubicacion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codigo, estado, paja, ubicacion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContadorDto {\n");
    sb.append("    codigo: ").append(toIndentedString(codigo)).append("\n");
    sb.append("    estado: ").append(toIndentedString(estado)).append("\n");
    sb.append("    paja: ").append(toIndentedString(paja)).append("\n");
    sb.append("    ubicacion: ").append(toIndentedString(ubicacion)).append("\n");
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

