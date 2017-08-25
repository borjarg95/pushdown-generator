package GramaticaIC;

import main.Utils;

/**
 * @author Borjarg95
 *
 *	Este objeto se formará de la primera transformación del autómata, definiendo:
 * G = (N, ∑, S, P), siendo:
 * 	-N = Conjunto de simbolos no terminales
 *  -∑ = Símbolos terminales
 *  -S = axioma 
 *  -P = Conjunto de plantillas de producciones de la gramática
 */
public class PlantillaProduccion{
	
	private String estado1;
	private Character simbolo;
	private String estado2;
	
	/**
	 * @param estado1
	 * @param simboloPila
	 * @param estado2
	 * Para mantener la unificación de la estructura y permitir su reutilización, una plantilla es productora,
	 *  cuando estado1 y estado2 = @. Si es una producción lambda, tendría la forma: [@@@]
	 * @throws Exception 
	 */
	PlantillaProduccion(String estado1, Character simboloPila, String estado2) throws Exception{
		if (!Utils.isBlankOrNull(estado1)){
			if (!Utils.isBlankOrNull(estado2)){ //Correctamente formado y no generativo/Axioma
				this.estado1 = estado1;
				this.simbolo = simboloPila;
				this.estado2 = estado2;
			} else {
				throw new Exception("La producción ["+estado1+simboloPila+estado2+"], no está bien informada");
			}
		} else {
			if (Utils.isBlankOrNull(estado2)){ //Axioma o producción(símbolo terminal)
				this.estado1 = Character.toString(Utils.LAMBDA);
				this.simbolo = simboloPila;
				this.estado2 = Character.toString(Utils.LAMBDA);
			} else {
				throw new Exception("La producción ["+estado1+simboloPila+estado2+"], no está bien informada");
			}
		}
	}

	
	/**
	 * @return the estado1
	 */
	public String getEstado1() {
		return estado1;
	}

	/**
	 * @return the simbolo
	 */
	public Character getSimbolo() {
		return simbolo;
	}

	/**
	 * @return the estado2
	 */
	public String getEstado2() {
		return estado2;
	}

	/**
	 * @param estado1 the estado1 to set
	 */
	public void setEstado1(String estado1) {
		this.estado1 = estado1;
	}

	/**
	 * @param simbolo the simbolo to set
	 */
	public void setSimbolo(Character simbolo) {
		this.simbolo = simbolo;
	}

	/**
	 * @param estado2 the estado2 to set
	 */
	public void setEstado2(String estado2) {
		this.estado2 = estado2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estado1 == null) ? 0 : estado1.hashCode());
		result = prime * result + ((estado2 == null) ? 0 : estado2.hashCode());
		result = prime * result + ((simbolo == null) ? 0 : simbolo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PlantillaProduccion))
			return false;
		PlantillaProduccion other = (PlantillaProduccion) obj;
		if (estado1 == null) {
			if (other.estado1 != null)
				return false;
		} else if (!estado1.equals(other.estado1))
			return false;
		if (estado2 == null) {
			if (other.estado2 != null)
				return false;
		} else if (!estado2.equals(other.estado2))
			return false;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "["+ estado1 + simbolo + estado2 + "]";
	}
	
}
