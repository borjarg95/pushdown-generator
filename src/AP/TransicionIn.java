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
		super();
		this.estado = estado;
		this.simbEntrada = simbEntrada;
		this.simbCabezaPila = simbCabezaPila;
	}
	public TransicionIn() {
		// TODO Auto-generated constructor stub
	}
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
	 * @return the simbEntrada
	 */
	public char getSimbEntrada() {
		return simbEntrada;
	}
	/**
	 * @param simbEntrada the simbEntrada to set
	 */
	public void setSimbEntrada(char simbEntrada) {
		this.simbEntrada = simbEntrada;
	}
	/**
	 * @return the simbCabezaPila
	 */
	public char getSimbCabezaPila() {
		return simbCabezaPila;
	}
	/**
	 * @param simbCabezaPila the simbCabezaPila to set
	 */
	public void setSimbCabezaPila(char simbCabezaPila) {
		this.simbCabezaPila = simbCabezaPila;
	}
	
	
	@Override
	public String toString() {
		return "("+ estado + ","+simbEntrada+","+simbCabezaPila+")";
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
	
	
}
