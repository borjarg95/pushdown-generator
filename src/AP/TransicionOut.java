package AP;

import java.util.List;

public class TransicionOut {

	private String estadoSalida;
	private List<Character> nuevaCabezaPila;
	
	/**
	 * @return the estadoSalida
	 */
	public String getEstadoSalida() {
		return estadoSalida;
	}
	/**
	 * @param estadoSalida the estadoSalida to set
	 */
	public void setEstadoSalida(String estado) {
		this.estadoSalida = estado;
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
	 * @param estadoSalida
	 * @param nuevaCabezaPila
	 */
	public TransicionOut(String estado, List<Character> nuevaCabezaPila) {
		super();
		this.estadoSalida = estado;
		this.nuevaCabezaPila = nuevaCabezaPila;
	}

	/**
	 * Constructor vacio.
	 */
	public TransicionOut() {
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estadoSalida == null) ? 0 : estadoSalida.hashCode());
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
		if (estadoSalida == null) {
			if (other.estadoSalida != null) {
				return false;
			}
		} else if (!estadoSalida.equals(other.estadoSalida)) {
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
		return "("+estadoSalida + ", " + nuevaCabezaPila +")";
	}
	
	
}
