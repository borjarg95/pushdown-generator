package AP;

public class TransicionIn {
	
	private String estado;
	private char simbEntrada;
	private char simbCabezaPila;
	
	/**
	 * @param estado
	 * @param simbEntrada
	 * @param simbCabezaPila
	 */
	public TransicionIn(String estado, char simbEntrada, char simbCabezaPila) {
		this.estado = estado;
		this.simbEntrada = simbEntrada;
		this.simbCabezaPila = simbCabezaPila;
	}
	
	public TransicionIn() {
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public char getSimbEntrada() {
		return simbEntrada;
	}

	public void setSimbEntrada(char simbEntrada) {
		this.simbEntrada = simbEntrada;
	}
	
	public char getSimbCabezaPila() {
		return simbCabezaPila;
	}

	public void setSimbCabezaPila(char simbCabezaPila) {
		this.simbCabezaPila = simbCabezaPila;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + simbCabezaPila;
		result = prime * result + simbEntrada;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TransicionIn))
			return false;
		TransicionIn other = (TransicionIn) obj;
		if (this.estado == null) {
			if (other.getEstado() != null)
				return false;
		} else if (!estado.equals(other.getEstado()))
			return false;
		if (simbCabezaPila != other.getSimbCabezaPila())
			return false;
		if (simbEntrada != other.getSimbEntrada())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "("+ estado + ","+simbEntrada+","+simbCabezaPila+")";
	}
}
