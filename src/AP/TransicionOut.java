package AP;

import java.util.List;

public class TransicionOut {

	private String estado;
	private List<Character> nuevaCabezaPila;
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return the nuevaCabezaPila
	 */
	public List<Character> getNuevaCabezaPila() {
		return nuevaCabezaPila;
	}
	/**
	 * @param nuevaCabezaPila the nuevaCabezaPila to set
	 */
	public void setNuevaCabezaPila(List<Character> nuevaCabezaPila) {
		this.nuevaCabezaPila = nuevaCabezaPila;
	}
	/**
	 * @param estado
	 * @param nuevaCabezaPila
	 */
	public TransicionOut(String estado, List<Character> nuevaCabezaPila) {
		super();
		this.estado = estado;
		this.nuevaCabezaPila = nuevaCabezaPila;
	}

	/**
	 * Constructor vacío.
	 */
	public TransicionOut() {
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((nuevaCabezaPila == null) ? 0 : nuevaCabezaPila.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TransicionOut)) {
			return false;
		}
		TransicionOut other = (TransicionOut) obj;
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (nuevaCabezaPila == null) {
			if (other.nuevaCabezaPila != null) {
				return false;
			}
		} else if (!nuevaCabezaPila.equals(other.nuevaCabezaPila)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "("+estado + ", " + nuevaCabezaPila +")";
	}
	
	
}
